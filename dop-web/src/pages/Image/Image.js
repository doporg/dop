import React, { Component } from 'react';
import {Breadcrumb} from '@icedesign/base';
import Pagination from './NamespaceManagement/NamespacePagination';

export default class Image extends Component {
    constructor(props) {
        super(props);
        this.state = {
            createdProjectNeedRefresh: false,
        };
    }
    refreshFinished() {
        this.setState({
            createdProjectNeedRefresh: false
        })
    }

    refreshProjectList() {
        this.setState({
            createdProjectNeedRefresh: true
        });
    }


    componentWillMount(){
    }

    render() {
        return (
            <div>
                <Breadcrumb style={{marginBottom: "10px"}}>
                    <Breadcrumb.Item link="#/image/projects">所有命名空间</Breadcrumb.Item>
                </Breadcrumb>
                <Pagination createdProjectNeedRefresh={this.state.createdProjectNeedRefresh}
                refreshFinished={this.refreshFinished.bind(this)}
                searchKey={this.state.searchKey}/>

            </div>
        )
    };

}
