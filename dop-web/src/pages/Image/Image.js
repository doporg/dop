import React, { Component } from 'react';
import "./Style.scss"
import NamespacePagination from './NamespaceManagement/NamespacePagination';

export default class Image extends Component {
    render() {
        return (
            <div>
                <div className="title">命名空间</div>
                <NamespacePagination/>
            </div>
        )
    };

}
