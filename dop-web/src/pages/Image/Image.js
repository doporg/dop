import React, { Component } from 'react';
import "./Style.scss"
import NamespacePagination from './NamespaceManagement/NamespacePagination';
import {FormattedMessage} from 'react-intl';

export default class Image extends Component {
    render() {
        return (
            <div>
                <div className="title">
                    <FormattedMessage
                    id="image.namespace"
                    defaultMessage="命名空间"/>
                </div>
                <NamespacePagination/>
            </div>
        )
    };

}
