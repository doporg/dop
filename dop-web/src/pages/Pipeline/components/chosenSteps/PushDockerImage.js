import React, {Component} from 'react';
import {Input} from '@icedesign/base';
import '../Styles.scss'


export default class PushDockerImage extends Component {


    buildDockerUserName(value) {
        this.props.onUserNameChange(value)
    }

    buildRepository(value) {
        this.props.onRepositoryChange(value)
    }

    buildRepositoryVersion(value) {
        this.props.onRepositoryVersionChange(value)
    }
    buildDockerPassword(value) {
        this.props.onDockerPasswordChange(value)
    }

    render() {
        return (
            <div>
                <div>
                    <h3 className="chosen-task-detail-title">推送docker镜像</h3>
                    <div className="chosen-task-detail-body">
                        <span className="item">
                            <span className="must">*</span>
                            <span>DockerUserName: </span>
                        </span>
                        <Input
                            onChange={this.buildDockerUserName.bind(this)}
                            className="input"
                            placeholder={this.props.dockerUserName}
                        />
                        <span className="item">
                            <span className="must">*</span>
                            <span>Repository: </span>
                        </span>
                        <Input
                            onChange={this.buildRepository.bind(this)}
                            className="input"
                            placeholder={this.props.repository}
                        />
                        <span className="item">
                            <span className="must">*</span>
                            <span>Version: </span>
                        </span>
                        <Input
                            onChange={this.buildRepositoryVersion.bind(this)}
                            className="input"
                            placeholder={this.props.repositoryVersion}
                        />
                        <span className="item">
                            <span className="must">*</span>
                            <span>DockerPassWord: </span>
                        </span>
                        <Input
                            onChange={this.buildDockerPassword.bind(this)}
                            className="input"
                            placeholder={this.props.dockerPassword}
                            htmlType="password"
                        />
                    </div>
                </div>
            </div>
        )
    }
}
