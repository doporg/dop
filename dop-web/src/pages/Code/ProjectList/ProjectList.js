import React from 'react';
import { Table } from '@icedesign/base';
import {Link,withRouter} from 'react-router-dom';

import './ProjectList.css';

export default class ProjectList extends React.Component {

    selectClassPersonal(){

        const {sort}=this.props.match.params;
        if(sort==='personal'){
            return "menu-item-active";
        }else {
            return "menu-item";
        }
    }

    selectClassStarred(){
        const {sort}=this.props.match.params;
        if(sort==='starred'){
            return "menu-item-active";
        }else {
            return "menu-item";
        }
    }

    selectClassAll(){
        const {sort}=this.props.match.params;
        if(sort==='all'){
            return "menu-item-active";
        }else {
            return "menu-item";
        }
    }



    render(){

        return (
            <div className="container">
                <div>
                    <ul className="center-top-menu">
                        <li>
                            <Link className={this.selectClassPersonal()} to="/code/projectlist/personal">
                                <span>你的项目</span>
                            </Link>
                        </li>
                        <li>
                            <Link className={this.selectClassStarred()} to="/code/projectlist/starred">
                                <span>星标项目</span>
                            </Link>
                        </li>
                        <li>
                            <Link className={this.selectClassAll()} to="/code/projectlist/all">
                                <span>浏览项目</span>
                            </Link>
                        </li>
                    </ul>
                    <div className="search-form">
                        <input className="input_search" placeholder="根据项目名称过滤" />
                        <button className="btn_add_project">
                            +新建项目
                        </button>
                    </div>
                </div>
            </div>
        );


    }

}







