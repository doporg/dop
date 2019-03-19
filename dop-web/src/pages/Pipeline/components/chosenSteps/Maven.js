import React, {Component} from 'react';
import '../Styles.scss'


export default class Maven extends Component{
    render(){
        return (
            <div>
                <h3 className="chosen-task-detail-title">构建maven</h3>
                <div
                    className="chosen-task-detail-body">
                    默认执行 <br/>
                    'mvn -U -am clean package' <br/>
                </div>
            </div>
        )
    }
}
