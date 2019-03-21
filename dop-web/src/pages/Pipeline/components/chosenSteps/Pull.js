import React, {Component} from 'react';
import {Input} from '@icedesign/base';
import '../Styles.scss'


export default class Pull extends Component{

    /**
     *  构建-填入git地址
     * */
    buildMavenGit(value) {
        this.props.onChange(value)
    }
    render(){
        return (
            <div>
                <h3 className="chosen-task-detail-title">拉取代码</h3>
                <div
                    className="chosen-task-detail-body">
                    <span className="item">
                        <span className="must">*</span>
                        <span>Git地址: </span>
                    </span>
                    <Input
                        onChange={this.buildMavenGit.bind(this)}
                        className="input"
                        value={this.props.gitUrl}
                    />
                </div>
            </div>
        )
    }
}
