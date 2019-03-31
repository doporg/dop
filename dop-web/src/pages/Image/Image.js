import React, { Component } from 'react';
import {Breadcrumb, Input} from '@icedesign/base';
import TopBar from './NamespaceManagement/TopBar';
import Pagination from './NamespaceManagement/NamespacePagination'
import CreateNamespaceDialog from './NamespaceManagement/CreateNamespaceDialog'

export default class Image extends Component {
    constructor() {
        super();
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
    onSearch(value) {
        this.setState({
            searchKey: value
        })
    }


    componentWillMount(){
    }

    render() {
        return (
            <div>
                <Breadcrumb style={{marginBottom: "10px"}}>
                    <Breadcrumb.Item link="#/image">所有命名空间</Breadcrumb.Item>
                </Breadcrumb>
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
                    extraAfter={< CreateNamespaceDialog refreshProjectList={this.refreshProjectList.bind(this)}/>
                    }
                />

                <Pagination createdProjectNeedRefresh={this.state.createdProjectNeedRefresh}
                            refreshFinished={this.refreshFinished.bind(this)}
                            searchKey={this.state.searchKey}/>

            </div>
        )
    };

}
