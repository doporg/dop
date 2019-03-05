/**
 * 项目管理主页面
 * @author Bowen
 */

import React, { Component } from 'react';
import Axios from 'axios';
import API from '../API';
import {Button, Input} from '@icedesign/base';
import TopBar from './components/TopBar';
import ProjectList from './components/ProjectList';
import Pagination from './components/ProjectPagination'
import CreateProjectDialog from './components/CreateProjectDialog'
export default class Projects extends Component {
    static displayName = 'Projects';

    componentWillMount() {
        // this.Get()
    }

    // Get(){
    //     let url = API.axiosGetTest + "/getTest";
    //     Axios.get(url).then((response)=> {
    //           // console.log(response)
    //     }).catch()
    // }
    constructor(props) {
        super(props);
        this.state = {
            //通知子组件刷新的boolean变量
            //因为获取项目列表数据需要翻页器的数据，如果在父组件中操作比较复杂，我这里就直接传一个消息让子组件刷新一下
            createdProjectNeedRefresh: false
        }
    }


    refreshFinished() {
        this.setState({
            createdProjectNeedRefresh: false
        })
    }

//回调函数传给创建项目的窗口，创建项目成功后调用该函数刷新项目列表
    refreshProjectList() {
        this.setState({
            createdProjectNeedRefresh: true
        });
        console.log("refreshProjectList");
    }

    render() {
        return (
            <div>
                <TopBar
                    extraBefore={
                        <Input
                            size="large"
                            placeholder="请输入关键字进行搜索"
                            style={{width: '240px'}}
                        />
                    }
                    extraAfter={< CreateProjectDialog refreshProjectList={this.refreshProjectList.bind(this)}/>
                    }
                />
                <Pagination createdProjectNeedRefresh={this.state.createdProjectNeedRefresh}
                            refreshFinished={this.refreshFinished.bind(this)}/>
            </div>

        );
    }
}
