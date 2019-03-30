import React, {Component} from 'react';
import '../Styles.scss'


export default class Deployment extends Component{
    render(){
        return (
            <div>
                <h3 className="chosen-task-detail-title">您将在您的集群上创建部署</h3>
                <div
                    className="chosen-task-detail-body">
                    我们会根据您环境中的配置进行部署
                </div>
            </div>
        )
    }
}
