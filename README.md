# 1.介绍

## 1.1.什么是DOP?

DOP是一个DevOps平台，你可以在DOP平台完成DevOps的整个流程(编码->构建->部署->测试)。

## 1.2.架构

### 1.2.1.整体架构

DOP整体上来看是一个多层的系统，其架构如图所示：

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-23-165203.png)

**基础设施层**：平台管理的应用、及平台本身的服务都将部署在kubernetes中，一些工具会直接部署在虚拟机或物理机上。

**持久层**：使用MySQL和MongoDB，使用ceph进行分布式文件存储，对kubernetes中有状态应用的数据进行存储。

**中间件层**：使用阿里的RocketMQ作为消息队列，Redis作为缓存数据库。

**工具层**：接下来在屏幕的右边是工具层。工具层主要包括一些开源的工具，这些工具提供一些基础的能力。业务层通过业务逻辑对这些能力进行组合整理最终提供给用户。比如jenkins提供流水线操作的能力（流水线的并行、等待）

**基础服务层**：屏幕的左边是基础服务层，从这层开始往后，都是我们自己实现的一些服务。包括服务发现、审计、登录、权限管理、用户管理、消息服务，他们为业务层服务提供一些通用的功能。

**业务层**：包括测试、代码管理、应用管理、流水线管理、容器镜像管理等服务，分别对应我们前面提到的用户使用的各种各样的功能。

**接入层**：接入层是一个API网关，主要处理客户端OAuth2.0认证和用户状态认证 两个认证流程。

**接口层**：包括用户使用的WebUI 统一登录 RESTful API。外部系统在通过客户端OAuth2.0认证后可以调用平台API。

**DOP系统使用了微服务架构，我们构建了基础层服务为微服务架构和基本业务逻辑提供支持，并且按照业务上下文将业务划分为测试、代码、应用管理、流水线等微服务，服务之间多通过HTTP协议通信，少数通过消息队列通信**。

### 1.2.1.基础层服务

#### 基础层服务列表

**基础层服务为微服务架构和基本业务逻辑提供支持，如下表所示：**

****

| 序号 | 名称          | 服务及其路径        | 前端及其路径                  | 开发人员                                                     | 功能 |
| ---- | ------------ | ------------------- | :---------------------------- | ------------------------------------------------------------ | -------- |
| 1    | 服务发现      | dop/discovery-server | URL:IP:8761 | 任贵杰([clsaa](https://github.com/clsaa))，实验室2018级 | 服务治理。所有服务的IP、端口信息都会注册到服务发现，服务之间相互调用时会从服务发现中心拉取服务IP、端口列表，通过客户端负载均衡选择实例进行调用。 |
| 2    | API网关      | dop/gateway-server | dop/dop-web/src/pages/Pipeline | 任贵杰([clsaa](https://github.com/clsaa))，实验室2018级 | 转发HTTP请求。将前端的HTTP请求转发到后端服务，转发过程中会进行OAuth2.0校验、用户登录token校验。 |
| 3    | 用户服务 | dop/user-server    | dop/dop-web/src/pages/Code   | 任贵杰([clsaa](https://github.com/clsaa))，实验室2018级 | 用户、账号相关业务逻辑 |
| 4    | 登录服务     | dop/login-server   | dop/dop-web/src/pages/Login  | 任贵杰([clsaa](https://github.com/clsaa))，实验室2018级 | 实现统一登录功能 |
| 5 | 权限管理 | dop/permission-server | dop/dop-web/src/pages/Permissions | 李质颖([552000264](https://github.com/552000264))，实验室2019级 | 权限管理，包括功能权限(基于RBAC)和数据权限(基于规则) |
| 6    | 操作审计服务  | dop/audit-server   | 无 | 任贵杰([clsaa](https://github.com/clsaa))，实验室2018级 | 对用户的操作进行审计，记录用户重要操作，业务方可查询功能使用记录 |
| 7 | 消息服务 | dop/message-server | 无 | 任贵杰([clsaa](https://github.com/clsaa))，实验室2018级 | 实现发送邮件的功能 |

#### API网关的OAuth2.0认证

> OAuth 2.0 是一个行业的标准授权协议。OAuth 2.0 专注于简化客户端开发人员，同时为 Web 应用程序，桌面应用程序，手机和客厅设备提供特定的授权流程。
>
> 它的最终目的是为第三方应用颁发一个有时效性的令牌 token。使得第三方应用能够通过该令牌获取相关的资源。常见的场景就是：第三方登录。当你想要登录某个论坛，但没有账号，而这个论坛接入了如 QQ、Facebook 等登录功能，在你使用 QQ 登录的过程中就使用的 OAuth 2.0 协议。
>
> 如果你想了解更多，其官方网址为：https://oauth.net/2/。下面我们来了解下 OAuth 协议的基本原理

OAuth2.0标准参考[RFC6749](https://tools.ietf.org/html/rfc6749)

**DOP项目当前使用OAuth2.0客户端模式，客户端(当前为dop-web或未来任何客户端)需要使用DOP后端服务的API都先要先接入OAuth2.0认证**（DOP颁发应用的Key和Secret）；在客户端请求DOP后端服务前，需要先调用API网关接口，进行OAuth2.0认证（获取AccessToken）；在客户端请求DOP后端服务时，携带之前获取的AccessToken，API网关会进行OAuth2.0验证(验证AccessToken)。具体可参考[DOP项目OAuth2.0接入文档](https://github.com/doporg/dop/blob/master/.doc/coding/4.OAth2.0%E6%8E%A5%E5%85%A5.md)，文档中有详细的文字说明及代码示例。

#### API网关的请求转发

> API 网关作为后端服务系统的唯一入口。它封装了系统内部架构，为每个客户端提供一个定制的 API 。由它负责服务请求路由、组合及协议转换。API 网关还有其它职责，如客户端验证、用户登录验证、负载均衡。

当你的客户端(web、微信小程序、APP)通过了OAuth2.0认证后，就可以通过API网关访问已注册在服务发现中心的后端服务（通过拉取某个服务的IP:PORT列表，进行客户端负载均衡后，选择一个合适的服务实例，转发HTTP请求）。DOP项目中网关的基本使用说明可参考[DOP项目API网关基本使用说明](https://github.com/doporg/dop/blob/master/.doc/coding/4.OAth2.0%E6%8E%A5%E5%85%A5.md#42%E7%BD%91%E5%85%B3%E5%9F%BA%E6%9C%AC%E4%BD%BF%E7%94%A8)

#### API网关与统一登录

API网关与用户登录、校验及用户注册的逻辑关系如下图所示：

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-24-Jietu20190128-045817.jpg)

* 上图中黄线所代表的流程为用户注册流程(填入邮箱等信息->进入邮箱点击链接->账户被创建)
* 上图中蓝线所代表的流程为注册后，用户使用用户名/密码登录的流程，用户凭证校验通过后会返回JWT token作为用户已登录的凭证。用户登录后调用后端接口时，应在http请求头中添加键值对："x-login-token:JWTtoken"
* 上图中绿线所代表的流程为登录后，客户端调用后端API时用户登录状态检验的流程，此流程中API网关会将此token转换为用户的userId，并将用户的userId放到http请求头"x-login-user"。具体可以参考[接入统一登录的业务服务如何获取用户id](https://github.com/doporg/dop/blob/master/.doc/coding/1.编码规范.md#1313接入统一登录的业务服务如何获取用户id)。

### 1.2.2.业务层服务

#### 业务服务列表

业务层服务为用户使用的基本功能提供支持，如下表所示：

序号 | 名称 | 服务及其路径 | 项目类型 | 开发人员 | 功能 
-|-|-|:-|-|-
1 | 应用管理模块 | dop/application-server | dop/dop-web/src/pages/Projects |郑博文([zyriix](https://github.com/Zyriix))，实验室2019级|项目管理、应用管理、部署环境管理（Kubernetes集群配置）、部署历史
2 | 流水线管理模块 | dop/pipeline-server |dop/dop-web/src/pages/Pipeline|张富利([zhangfuli](https://github.com/zhangfuli))，实验室2019级|流水线管理与执行
3 | 代码管理模块 | dop/code-server |dop/dop-web/src/pages/Code|王舒瑶([waszqt](https://github.com/waszqt))，南大2019届|代码管理(简易版GitLab/GitHub)
4 | 镜像管理模块 | dop/image-server |dop/dop-web/src/pages/Image|辛志庭([ZhiTingXin](https://github.com/ZhiTingXin))，南大2019届|容器镜像管理(简易版Harbor/DockerHub)
5 | 测试管理模块 | dop/test-server |dop/dop-web/src/pages/TestCases|曾锡豪([151250010](https://github.com/151250010))，南大2019届|自动化接口测试管理与执行、手工测试管理

#### 业务模块与DevOps流程

业务模块与DevOps流程的关系，如下图所示：

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-24-013749.png)

#### 单业务服务架构

单个业务服务的架构，如下图所示：

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-24-014534.jpg)

每个服务都使用贫血模型，被设计为分层结构。controller对API网关转发来的请求进行处理，service层相对比较复杂，它可能会使用持久层对数据库进行操作或使用通用处理层调用API。

