import React, {Component} from 'react';
import '../Styles.scss'


export default class Djanggo extends Component{
    render(){
        return (
            <div>
                <h3 className="chosen-task-detail-title">构建djanggo</h3>
                <div
                    className="chosen-task-detail-body">
                    默认执行 <br/>
                    'pip freeze > ./requirements.txt' <br/>
                    'pip install -r ./requirements.txt' <br/>
                    'python ./manage.py runserver' <br/>
                </div>
            </div>
        )
    }
}
