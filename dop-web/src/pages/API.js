// 配置所有接口的API文件

// var host = "http://open.dop.clsaa.com";
var host = "http://localhost:8888";
var pipline = "/pipeline-server";
var application = "/application-server";
var permission = "/permission-server";
var code = "/code-server";
var user = "/user-server";
var image = "/image-server";
var test = "/test-server";
const API = {
    address: "http://www.dop.clsaa.com/#/",
    gateway: host,
    pipeline: host + pipline,
    application: host + application,
    permission: host + permission,
    code: host + code,
    user: host + user,
    image: host + image,
    test: host + test
};

export default API;
