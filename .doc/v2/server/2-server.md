## K8S
> 由于CentOS7 默认启用防火墙服务（firewalld），而Kubernetes的Master与工作Node之间会有大量的网络通信，安全的做法是在防火墙上配置各组件需要相互通信的端口号。
  
  由于目前是实测环节，可以在安全的内部网络环境中可以关闭防火墙服务：
  
  ### 关闭 防火墙
  ```
  systemctl stop firewalld
  systemctl disable firewalld
  ```
建议在主机上禁用SELinux，让容器可以读取主机文件系统。或修改系统文件/etc/sysconfig/selinux，将SELINUX=enforcing修改成SELINUX=disabled，然后重启Linux。

### 关闭 SeLinux
```
setenforce 0
sed -i "s/SELINUX=enforcing/SELINUX=disabled/g" /etc/selinux/config
```
执行重启操作，重启后记得查看docker是否在运行中【之前设置了开机启动】。

### 关闭 swap
```
swapoff -a
yes | cp /etc/fstab /etc/fstab_bak
cat /etc/fstab_bak |grep -v swap > /etc/fstab
```
### 配置iptable管理ipv4/6请求
```
vim /etc/sysctl.d/k8s.conf
```
写入
```
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
```

使配置生效
```
sysctl --system
```
### 安装kubeadm套件
编辑源
```
vim /etc/yum.repos.d/kubernetes.repo
```
写入
```
[kubernetes]
name=Kubernetes
baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=0
repo_gpgcheck=0
gpgkey=https://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg https://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
```
下载
```
yum install -y kubelet-1.15.0 kubeadm-1.15.0 kubectl-1.15.0
```
然后执行
```
systemctl enable kubelet
```

### 集群初始化
```
kubeadm init --apiserver-advertise-address=172.31.164.123 --image-repository registry.aliyuncs.com/google_containers --kubernetes-version v1.15.0 --service-cidr=10.1.0.0/16 --pod-network-cidr=10.244.0.0/16
```
注意上面的命令中 --apiserver-advertise-address是master节点的IP地址，以上命令在master节点运行。

输出如下：
```
[init] Using Kubernetes version: v1.15.0
[preflight] Running pre-flight checks
        [WARNING SystemVerification]: this Docker version is not on the list of validated versions: 19.03.4. Latest validated version: 18.09
[preflight] Pulling images required for setting up a Kubernetes cluster
[preflight] This might take a minute or two, depending on the speed of your internet connection
[preflight] You can also perform this action in beforehand using 'kubeadm config images pull'
[kubelet-start] Writing kubelet environment file with flags to file "/var/lib/kubelet/kubeadm-flags.env"
[kubelet-start] Writing kubelet configuration to file "/var/lib/kubelet/config.yaml"
[kubelet-start] Activating the kubelet service
[certs] Using certificateDir folder "/etc/kubernetes/pki"
[certs] Generating "front-proxy-ca" certificate and key
[certs] Generating "front-proxy-client" certificate and key
[certs] Generating "etcd/ca" certificate and key
[certs] Generating "etcd/server" certificate and key
[certs] etcd/server serving cert is signed for DNS names [izm5e20kp0myoxxo8qtezdz localhost] and IPs [172.31.164.123 127.0.0.1 ::1]
[certs] Generating "apiserver-etcd-client" certificate and key
[certs] Generating "etcd/peer" certificate and key
[certs] etcd/peer serving cert is signed for DNS names [izm5e20kp0myoxxo8qtezdz localhost] and IPs [172.31.164.123 127.0.0.1 ::1]
[certs] Generating "etcd/healthcheck-client" certificate and key
[certs] Generating "ca" certificate and key
[certs] Generating "apiserver" certificate and key
[certs] apiserver serving cert is signed for DNS names [izm5e20kp0myoxxo8qtezdz kubernetes kubernetes.default kubernetes.default.svc kubernetes.default.svc.cluster.local] and IPs [10.1.0.1 172.31.164.123]
[certs] Generating "apiserver-kubelet-client" certificate and key
[certs] Generating "sa" key and public key
[kubeconfig] Using kubeconfig folder "/etc/kubernetes"
[kubeconfig] Writing "admin.conf" kubeconfig file
[kubeconfig] Writing "kubelet.conf" kubeconfig file
[kubeconfig] Writing "controller-manager.conf" kubeconfig file
[kubeconfig] Writing "scheduler.conf" kubeconfig file
[control-plane] Using manifest folder "/etc/kubernetes/manifests"
[control-plane] Creating static Pod manifest for "kube-apiserver"
[control-plane] Creating static Pod manifest for "kube-controller-manager"
[control-plane] Creating static Pod manifest for "kube-scheduler"
[etcd] Creating static Pod manifest for local etcd in "/etc/kubernetes/manifests"
[wait-control-plane] Waiting for the kubelet to boot up the control plane as static Pods from directory "/etc/kubernetes/manifests". This can take up to 4m0s
[apiclient] All control plane components are healthy after 26.002199 seconds
[upload-config] Storing the configuration used in ConfigMap "kubeadm-config" in the "kube-system" Namespace
[kubelet] Creating a ConfigMap "kubelet-config-1.15" in namespace kube-system with the configuration for the kubelets in the cluster
[upload-certs] Skipping phase. Please see --upload-certs
[mark-control-plane] Marking the node izm5e20kp0myoxxo8qtezdz as control-plane by adding the label "node-role.kubernetes.io/master=''"
[mark-control-plane] Marking the node izm5e20kp0myoxxo8qtezdz as control-plane by adding the taints [node-role.kubernetes.io/master:NoSchedule]
[bootstrap-token] Using token: mktpyg.860wkoqylh5rwo99
[bootstrap-token] Configuring bootstrap tokens, cluster-info ConfigMap, RBAC Roles
[bootstrap-token] configured RBAC rules to allow Node Bootstrap tokens to post CSRs in order for nodes to get long term certificate credentials
[bootstrap-token] configured RBAC rules to allow the csrapprover controller automatically approve CSRs from a Node Bootstrap Token
[bootstrap-token] configured RBAC rules to allow certificate rotation for all node client certificates in the cluster
[bootstrap-token] Creating the "cluster-info" ConfigMap in the "kube-public" namespace
[addons] Applied essential addon: CoreDNS
[addons] Applied essential addon: kube-proxy

Your Kubernetes control-plane has initialized successfully!

To start using your cluster, you need to run the following as a regular user:

  mkdir -p $HOME/.kube
  sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
  sudo chown $(id -u):$(id -g) $HOME/.kube/config

You should now deploy a pod network to the cluster.
Run "kubectl apply -f [podnetwork].yaml" with one of the options listed at:
  https://kubernetes.io/docs/concepts/cluster-administration/addons/

Then you can join any number of worker nodes by running the following on each as root:

kubeadm join 172.31.164.123:6443 --token mktpyg.860wkoqylh5rwo99 \
    --discovery-token-ca-cert-hash sha256:01572d11ced62f10233c1abb8c871b3c0be62ab0938397d1afbd3b5fb8bafb91 
```

token: mktpyg.860wkoqylh5rwo99 --discovery-token-ca-cert-hash sha256:01572d11ced62f10233c1abb8c871b3c0be62ab0938397d1afbd3b5fb8bafb91 
           
节点加入： kubeadm join 172.31.164.123:6443 --token mktpyg.860wkoqylh5rwo99 --discovery-token-ca-cert-hash sha256:01572d11ced62f10233c1abb8c871b3c0be62ab0938397d1afbd3b5fb8bafb91 

接下来执行如下命令，**切换成普通用户**，由输出结果中给出的。
```
  mkdir -p $HOME/.kube
  sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
  sudo chown $(id -u):$(id -g) $HOME/.kube/config
```
另外注意的是，需要对集群进行网络部署，方案多种，可以在点击此处查看。
这里选择flannel。
```
https://github.com/coreos/flannel/blob/master/Documentation/kube-flannel.yml
```
事实上还可以是这个网址下的：
```
https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml
```
二者实际上一样，只是如果访问github比较困难，可以选择第二个。


### 部署flannel
```
kubectl apply -f https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml
```
*如果出现访问被拒绝的问题，在普通用户身份下运行下面的命令*
```
export KUBECONFIG=/etc/kubernetes/admin.conf
```



kubectl 常用命令：
* kubectl get pods --all-namespaces      //查看kube-flannel 状态    running 为安装正常
* kubectl get pods --all-namespaces -o wide
* kubectl get nodes
* kubectl get cs
* kubectl get csr
* kubectl apply -f  xxxx.yaml
* kubectl create -f xxxx.yaml
* kubectl delete -f xxxxx.yaml