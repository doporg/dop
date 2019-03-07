/**
 * 应用列表（没开始用）
 * @author Bowen
 */

import React, {Component} from 'react';
import API from "../../API";
import Axios from "axios";
import TopBar from "../components/ApplicationManagement/TopBar";
import {Input} from "@icedesign/base";
import CreateApplicationDialog from "../components/ApplicationManagement/CreateApplicationDialog";
import Pagination from "../components/ApplicationManagement/ApplicationPagination";

export default class Application extends Component {
    static displayName = 'Application';

    constructor(props) {
        super(props);
        console.log(props);
        this.state = {
            projectId: props.location.state.projectId,
            searchKey: ""
        }
    }

    onSearch(value) {
        this.setState({
            searchKey: value
        })
    }

    // componentWillReceiveProps() {
    //     let projectId=this.props.location.state.projectId;
    //     console.log(this.props)
    //     console.log("componentWillMount",projectId,this.state.projectId);
    // }

    render() {
        return (
            <div>
                <TopBar
                    extraBefore={
                        <Input
                            size="large"
                            placeholder="请输入关键字进行搜索"
                            style={{width: '240px'}}
                            // hasClear
                            onChange={this.onSearch.bind(this)}
                        />
                    }

                />
                <Pagination searchKey={this.state.searchKey}
                            projectId={this.state.projectId}/>
            </div>

        );
    }
}
