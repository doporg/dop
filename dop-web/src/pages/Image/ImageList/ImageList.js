import React, { Component } from 'react';
import {Breadcrumb} from '@icedesign/base';
import ImagePagination from './ImagePagination'
import API from "../../API";
import Axios from "axios";

export default class ImageList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            namespaceId: props.location.pathname.match("[0-9]+")[0],
            repoName:props.location.pathname.split("/")[5]+"/"+props.location.pathname.split("/")[6],
            currentData: []
        };
    }

    refreshImageList(){
        let url = API.image + '/v1/repositories/'+this.state.repoName+"/images";
        let _this = this;
        Axios.get(url, {})
            .then(function (response) {
                console.log("镜像信息");
                console.log(response.data);
                _this.setState({
                    currentData: response.data,
                });

            })

    }

    componentDidMount() {
        this.refreshImageList();
    }

    render() {
        return (
            <div>
                <Breadcrumb style={{marginBottom: "10px"}}>
                    <Breadcrumb.Item link="#/image/projects">命名空间列表{this.state.namespaceId}</Breadcrumb.Item>
                    <Breadcrumb.Item link={"#/image/projects/"+this.state.namespaceId+"/repos"}>镜像仓库列表{this.state.repoName}</Breadcrumb.Item>
                </Breadcrumb>
                <ImagePagination repoName={this.state.repoName} imageData={this.state.currentData} refreshImageList={this.refreshImageList.bind(this)}/>

            </div>
        )
    };

}
