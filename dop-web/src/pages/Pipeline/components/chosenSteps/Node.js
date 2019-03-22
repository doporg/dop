import React, {Component} from 'react';
import '../Styles.scss'


export default class Node extends Component{
    render(){
        return (
            <div>
                <h3 className="chosen-task-detail-title">构建node</h3>
                <div
                    className="chosen-task-detail-body">
                    默认执行 <br/>
                    'npm install' <br/>
                </div>
            </div>
        )
    }
}
