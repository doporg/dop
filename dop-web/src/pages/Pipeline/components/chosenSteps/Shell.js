import React, {Component} from 'react';
import '../Styles.scss'
import {Input, Select} from '@icedesign/base';
const {Combobox} = Select;



export default class Shell extends Component {
    constructor(props){
        super(props)
        this.state = {
            myScript: ["shell"]
        }
    }
    buildShell(value){
        this.props.onShellChange(value)
    }
    render() {
        return (
            <div>
                <div>
                    <h3 className="chosen-task-detail-title">自定义脚本</h3>
                    <div className="chosen-task-detail-body">
                        <span className="item">
                            <span className="must">*</span>
                            <span>脚本类型: </span>
                        </span>
                        <Combobox
                            className="input"
                            filterLocal={false}
                            placeholder="请脚本类型"
                            dataSource={this.state.myScript}
                        /> <br/>


                        <div className="textarea">
                            <div className="title">
                                <span className="must">*</span>
                                脚本内容:
                            </div>
                            <div className="area-wrap">
                                <Input
                                    multiple
                                    value={this.props.shell}
                                    className="area"
                                    onChange={this.buildShell.bind(this)}
                                />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}
