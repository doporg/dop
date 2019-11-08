# 使用HELM安装Jenkins

## 安装步骤
1. 添加阿里云的仓库
```
helm repo add apphub https://apphub.aliyuncs.com/
```
2. 拉取chart文件夹
```
helm fetch apphub/jenkins
```
3. 修改values.yaml
```
persistence:
  enabled: true
  ## If defined, volume.beta.kubernetes.io/storage-class: <storageClass>
  ## Default: volume.alpha.kubernetes.io/storage-class: default
  ##
  #把storageClass改成nfs
  storageClass: nfs
  accessMode: ReadWriteOnce
  size: 10Gi
```
4. 创建jenkins-pv.yaml
```
apiVersion: v1
kind: PersistentVolume
metadata:
   name: jenkins
   namespace: jenkins
spec:
   capacity:
     storage: 10Gi
   accessModes:
     - ReadWriteOnce
   persistentVolumeReclaimPolicy: Retain
   storageClassName: nfs
   volumeMode: Filesystem
   nfs:
     # server为内网ip地址 path地址要保证打开可以访问（见mysql）
     path: /root/k8s/nfsdata/jenkins
     server: 172.31.164.123                                    
```
5. 使用helm进行安装
```
 helm install -n jenkins --namespace jenkins --values values.yaml apphub/jenkins

NAME:   jenkins
LAST DEPLOYED: Fri Nov  8 10:49:07 2019
NAMESPACE: jenkins
STATUS: DEPLOYED

RESOURCES:

==> v1/Deployment
NAME     READY  UP-TO-DATE  AVAILABLE  AGE
jenkins  0/1    1           0          0s

==> v1/PersistentVolumeClaim
NAME     STATUS   VOLUME  CAPACITY  ACCESS MODES  STORAGECLASS  AGE
jenkins  Pending  nfs     0s        Filesystem

==> v1/Pod(related)
NAME                      READY  STATUS   RESTARTS  AGE
jenkins-594cbc6448-clz5v  0/1    Pending  0         0s

==> v1/Secret
NAME     TYPE    DATA  AGE
jenkins  Opaque  1     0s

==> v1/Service
NAME     TYPE          CLUSTER-IP   EXTERNAL-IP  PORT(S)                     AGE
jenkins  LoadBalancer  10.1.43.221  <pending>    80:31384/TCP,443:31660/TCP  0s

 
NOTES:

** Please be patient while the chart is being deployed **

1. Get the Jenkins URL by running:

** Please ensure an external IP is associated to the jenkins service before proceeding **
** Watch the status using: kubectl get svc --namespace jenkins -w jenkins **

  export SERVICE_IP=$(kubectl get svc --namespace jenkins jenkins --template "{{ range (index .status.loadBalancer.ingress 0) }}{{.}}{{ end }}")
  echo "Jenkins URL: http://$SERVICE_IP/"

2. Login with the following credentials

  echo Username: user
  echo Password: $(kubectl get secret --namespace jenkins jenkins -o jsonpath="{.data.jenkins-password}" | base64 --decode)

[root@dop-node1 jenkins]# echo Username: user
Username: user
[root@dop-node1 jenkins]#   echo Password: $(kubectl get secret --namespace jenkins jenkins -o jsonpath="{.data.jenkins-password}" | base64 --decode)
Password: tT9xoZkP40
```
用户名：user
密码：tT9xoZkP40

6. 创建jenkins-pv
```
 kubectl create -f jenkins-pv.yaml

```

7. 查看deployment pod svc
```
[root@dop-node1 jenkins]# kubectl get pod,deployment,svc -n jenkins
NAME                           READY   STATUS    RESTARTS   AGE
pod/jenkins-594cbc6448-clz5v   1/1     Running   0          28m

NAME                      READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/jenkins   1/1     1            1           28m

NAME              TYPE           CLUSTER-IP    EXTERNAL-IP   PORT(S)                      AGE
service/jenkins   LoadBalancer   10.1.43.221   <pending>     80:31384/TCP,443:31660/TCP   28m
```

8. 使用node3IP:nodePort访问
http://115.28.186.77:31384/


