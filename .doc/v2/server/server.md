## 实验室服务器配置V2

### 1.1. 服务器基本信息

> 采用密钥登陆的方式

##### 1.1.1. dop-node1

- ip-47.105.117.1
- master节点
- 地域：青岛
- 内网IP（同一region内，内网ip互通）：172.31.53.159
- hosts

```
::1     localhost       localhost.localdomain   localhost6      localhost6.localdomain6
127.0.0.1       localhost       localhost.localdomain   localhost4      localhost4.localdomain4

172.31.53.159   iZm5e9ftqv2b7ykc4obcd6Z iZm5e9ftqv2b7ykc4obcd6Z
```

- 安全组
  - 端口：1~65535
  - 上行：%
  - 下行：%

##### 1.1.2 dop-node2

- ip-47.105.107.189
- node节点
- 地域：青岛
- 内网IP（同一region内，内网ip互通）：172.31.53.161
- hosts

```
::1     localhost       localhost.localdomain   localhost6      localhost6.localdomain6
127.0.0.1       localhost       localhost.localdomain   localhost4      localhost4.localdomain4

172.31.53.161   iZm5e25p95hhdcjltean29Z iZm5e25p95hhdcjltean29Z

```

- 安全组
  - 端口：1~65535
  - 上行：%
  - 下行：%

##### 1.1.3 dop-node3

- ip-47.105.104.192
- node节点
- 地域：青岛
- 内网IP（同一region内，内网ip互通）：172.31.53.160
- hosts

```
::1     localhost       localhost.localdomain   localhost6      localhost6.localdomain6
127.0.0.1       localhost       localhost.localdomain   localhost4      localhost4.localdomain4

172.31.53.160   iZm5e25p95hhdcjltean28Z iZm5e25p95hhdcjltean28Z
```

- 安全组
  - 端口：1~65535
  - 上行：%
  - 下行：%