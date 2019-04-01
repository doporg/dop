import React, {Component} from 'react';
import '../Styles.scss'
import {Feedback, Select} from "@icedesign/base/index";
import Axios from "axios/index";
import API from "../../../API";
const {toast} = Feedback;
const Option = Select.Option;


export default class DockerImage extends Component {
    constructor(props) {
        super(props);
        this.state = {
            environments: [],
            selectedEnv: null,
            dockerUser: [],
            repositories: [],
            repositoryVersions: []
        }

    }

    componentWillMount() {
        if (this.props.appId) {
            this.getEnv();
            this.getDockerUser();
        } else {
            toast.show({
                type: "error",
                content: "请先绑定一个应用",
                duration: 3000
            });
        }
        if(this.props.selectEnvId){
            this.getRepository();
        }

    }

    getEnv() {
        let url = API.application + "/app/" + this.props.appId + "/allEnv";
        let self = this;
        let environments = self.state.environments;
        Axios.get(url).then((response) => {
            if (response.status === 200) {
                for (let i = 0; i < response.data.length; i++) {
                    let environment = {
                        label: response.data[i].title,
                        value: response.data[i].id
                    };
                    environments.push(environment)

                    if(self.props.selectEnvId === response.data[i].id){
                        self.setState({
                            selectedEnv: response.data[i].title
                        })
                    }
                }
                self.setState({
                    environments: environments
                })
            }
        })
    }

    getDockerUser() {
        let url = API.user + "/v1/users/" + window.sessionStorage.getItem("user-id") + "/credential?type=DOP_INNER_HARBOR_LOGIN_EMAIL";
        let self = this;
        let dockerUser = self.state.dockerUser;
        Axios.get(url).then((response) => {
            if (response.status === 200) {
                let user = {
                    label: response.data.identifier,
                    id: response.data.id,
                    key: response.data.id,
                    credential: response.data.credential,
                    identifier: response.data.identifier,
                };
                dockerUser.push(user);
                self.setState({
                    dockerUser
                })
            }
        })
    }

    selectEnv(value) {
        this.setState({
            selectedEnv: value
        });
        console.log(value)
        if(!this.props.selectEnvId){
            this.getRepository();
        }
        this.props.onSelectEnv(value)
    }

    getRepository() {
        let url = API.application + "/app/" + this.props.appId + "/urlInfo";
        let self = this;
        let repositories = self.state.repositories;
        Axios.get(url).then((response) => {
            if (response.status === 200) {
                let repository = {
                    value: response.data.imageUrl,
                    label: response.data.imageUrl,
                    key: self.state.repositories.length
                };
                repositories.push(repository);
                console.log(repositories)
                self.setState({
                    repositories
                })
            }
        })
    }

    selectDocker(value) {
        let self = this;
        let dockerUser = this.state.dockerUser;
        for (let i = 0; i < dockerUser.length; i++) {
            if (value === dockerUser[i].id) {
                self.props.onUserNameChange(dockerUser[i].identifier);
                self.props.onDockerPasswordChange(dockerUser[i].credential)
            }
        }
    }

    selectRepository(value) {
        this.props.onRepositoryChange(value)
    }

    render() {
        return (
            <div>
                <h3 className="chosen-task-detail-title">构建docker镜像</h3>
                <div className="chosen-task-detail-body">
                     <span className="item">
                        <span className="must">*</span>
                        <span>环境设置: </span>
                    </span>
                    <Select
                        key="env"
                        onChange={this.selectEnv.bind(this)}
                        placeholder={this.state.selectedEnv?this.state.selectedEnv:"请选择环境"}
                        disabled={!this.props.appId}
                        className="input"
                        dataSource={this.state.environments}

                    />

                    <br/>

                    <span className="item">
                        <span className="must">*</span>
                        <span>DockerUserName: </span>
                    </span>
                    <Select
                        key="docker"
                        placeholder={this.props.dockerUserName?this.props.dockerUserName:"请选择docker用户"}
                        onChange={this.selectDocker.bind(this)}
                        fillProps="label"
                        className="input"
                    >
                        {this.state.dockerUser.map((user, index) => {
                            return <Option value={user.id} key={"docker" + index}>{user.label}</Option>
                        })}
                    </Select>
                    <br/>

                    <span className="item">
                        <span className="must">*</span>
                        <span>Repository: </span>
                    </span>

                    <Select
                        key="repos"
                        onChange={this.selectRepository.bind(this)}
                        placeholder={this.props.repository?this.props.repository:"请选择镜像名称"}
                        disabled={!this.state.selectedEnv}
                        className="input"
                        dataSource={this.state.repositories}
                    />
                </div>
            </div>
        )
    }
}
