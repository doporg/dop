## 1.Detailed introduction

This chapter will introduce the detailed functions and core business processes of each module in detail

#### 1.1 Application management service
> Application management serviceare used to organize and manage applications (each application can be viewed as a service), which stores all the metadata needed in an application DevOps process, so basically all other modules interact with the application management modules, the most closely connected of which is the pipeline service. In addition, the application management module can configure the deployment environment for the application (currently only Kubernetes deployment is supported, so the Kubernetes cluster can be simply configured through the application management module).

#####  1.1.1 Functions overview
![TIM截图20190620163702](https://user-images.githubusercontent.com/17808702/59833958-b942cf00-9379-11e9-8870-0dffc0fa27a0.png)

#### 1.2 Pipeline management service

>Pipeline management serviceis used to manage all pipeline in DOP. Its pipeline that relies on Jenkins is simpler and more convenient to use than Jenkins, and the data of each module is broken through in multiple links. We can manage and use both the data required by pipeline operation and the results generated after operation.

#####  1.2.1 Functions overview
![TIM截图20190620184746](https://user-images.githubusercontent.com/17808702/59843893-121b6300-938c-11e9-9ba8-aa049709a77d.png)

#### 1.3 Test management service

>The test management service is used to manage test cases. Manual tests and automatic interface test cases can be defined, and the interface test cases can be automatically executed according to the defined policies.

#####  1.3.1 Functions overview
![TIM截图20190620181702](https://user-images.githubusercontent.com/17808702/59841801-ac2cdc80-9387-11e9-889e-3dfa6115f744.png)

#### 1.4 Code management service

>The code management service is used for the management of application code, which relies on GitLab as the underlying implementation and provides a subset of GitLab functions.

#####  1.4.1 Functions overview
![TIM图片20190620185615](https://user-images.githubusercontent.com/17808702/59844310-23b13a80-938d-11e9-91d3-6eb2a4b3eeae.png)



#### 1.5 Container image management service

>The container image management service is used to manage Docker images, which rely on Harbor as the underlying implementation.

#####  1.5.1 Functions overview
![TIM截图20190620183244](https://user-images.githubusercontent.com/17808702/59842879-029b1a80-938a-11e9-8412-461dc34165c5.png)

#### 1.6 Container image management service

>Authority management service is a basic layer service used for authority management of various business services. Currently, it has been able to realize functional authority based on RBAC and data authority management based on rules.The business side can configure and verify permissions by calling relevant interfaces.

#####  1.6.1 Functions overview
![TIM截图20190620184414](https://user-images.githubusercontent.com/17808702/59843616-83a6e180-938b-11e9-937b-27ec2f708992.png)

