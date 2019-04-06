import React, {Component} from 'react';
import API from "../../../API"
import Axios from "axios";
import RepoList from '../RepoList'
import {Breadcrumb} from "@icedesign/base";


export default class Repos extends Component {
    constructor(props) {
        super(props);
        this.state = {
            currentData: [],
            //总页数
            totalPage: 1,
            queryKey: props.searchKey,
            loading: true,
            id: props.location.pathname.match("[0-9]+")[0]
        };

        this.handleChange = this.handleChange.bind(this);
    }

    refreshList() {
        let url = API.image + '/v1/projects/'+this.state.id+'/repositories';
        let _this = this;
        Axios.get(url, {})
            .then(function (response) {
                console.log("镜像仓库信息");
                console.log(response.data);
                _this.setState({
                    currentData: response.data
                });

            })

    }

    handleChange(current) {
        this.refreshList(current, this.state.searchKey);
    }



    componentDidMount() {
        this.refreshList(1, "");
    }

    render() {
        return (
            <div>
                <Breadcrumb style={{marginBottom: "10px"}}>
                    <Breadcrumb.Item link="#/image/projects">所有命名空间</Breadcrumb.Item>
                </Breadcrumb>
                <RepoList currentData={this.state.currentData} projectId={this.state.id} refreshList={this.refreshList.bind(this)}/>
            </div>
        )
    }

}






