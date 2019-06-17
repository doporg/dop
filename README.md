# DOP

With the popularization of micro-services and the improvement of the frequency and quality of demand delivery, the complexity and cost of operation and maintenance are greatly increased. DevOps is an important way to reduce operation and maintenance costs and improve delivery capacity. However, although there are many open source tools to support DevOps in the market, these tools are relatively independent, data is not accessible, and they are cumbersome to use and have high barriers. Manufacturers are also developing their own DevOps platforms, such as Aliyun cloud effect and Huawei cloud DevCloud, but these products are commercial products. We can not get the data generated in the whole process of DevOps, nor can we make customized development. Therefore, we decided to develop an open source project--DOP. DOP, for standard cloud effect and DevCloud, to provide easy-to-use functions involved in the DevOps process. Thus, data can be collected, customized development can be carried out, assisting the research in the field of DevOps, at the same time, the success of the research can also be fed back to DOP.

DOP is a DevOps platform, you can complete the coding, construction, deployment, testing and other aspects of the DevOps process in the DOP platform, so as to achieve the CI/CD of the project.


DOP as a whole is a multi-tier system, and its architecture is shown in the figure.

![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-17-130507.png)

**Infrastructure layer**：Platform management applications and services of the platform itself will be deployed in kubernetes, and some tools will be deployed directly on virtual machines or physical machines.

**Persistence layer**：Using MySQL and MongoDB, using CEPH to store distributed files, and storing data of stateful applications in kubernetes.

**Middleware layer**：Ali's RicketMQ is used as message queue and Redis is used as cache database.

**Tool Layer**：Next to the right is the Tool Layer. Tool layer mainly includes some open source tools, which provide some basic capabilities. Business layer combines these capabilities through business logic and finally provides them to users. For example, Jenkins provide pipeline operations (pipeline parallelism, waiting)

**Basic Services Layer**：On the left is the Basic Services Layer, from which we start to implement some of our own services. Basic layer services provide support for micro-service architecture and basic business logic, including service discovery, audit, login, privilege management, user management, message service. They provide some common functions for business layer services.


**Business layer**：Business layer services provide support for the basic functions used by users, including testing, code management, application management, pipeline management, container image management and other services, which correspond to the various functions used by users mentioned earlier.

**Access Layer**：Access Layer is an API gateway, which mainly deals with two authentication processes: client OAuth 2.0 authentication and user status authentication.

**Interface Layer**：Including the Unified Login RESTful API for Web UI used by users. The external system can call the platform API after it has been authenticated by the client OAuth 2.0.

**DOP system uses the micro-service architecture. We build the basic layer services to support the micro-service architecture and basic business logic. According to the business context, we divide the business into micro-services such as testing, code, application management, pipeline and so on. Most of the services communicate through HTTP protocol, and a few communicate through message queue.**。

The relationship between business modules and DevOps processes is shown in the following figure:

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-24-013749.png)

The architecture of a single business service, as shown in the figure below：

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-24-014534.jpg)

Each service uses an anemia model and is designed as a hierarchical structure. Controller handles requests forwarded by API gateway. Service layer is relatively complex. It may use persistence layer to operate database or use general processing layer to invoke API.
