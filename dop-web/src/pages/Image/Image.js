import React, { Component } from 'react';
import {Breadcrumb} from '@icedesign/base';
import NamespacePagination from './NamespaceManagement/NamespacePagination';

export default class Image extends Component {
    render() {
        return (
            <div>
                <Breadcrumb style={{marginBottom: "10px"}}>
                    <Breadcrumb.Item link="#/image/projects">命名空间列表</Breadcrumb.Item>
                </Breadcrumb>
                <NamespacePagination/>
            </div>
        )
    };

}
