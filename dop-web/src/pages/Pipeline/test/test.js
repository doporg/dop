import React, {Component} from 'react';
import {injectIntl} from 'react-intl';
import Container from '@icedesign/container';
import {Pagination} from '@icedesign/base';
import LibTable from './LibTable';


class PipelineTest extends Component {

    constructor(props) {
        super(props);
    }


    render() {
        return (
            <div>
                <LibTable />
            </div>
        );
    }
}



export default injectIntl(PipelineTest)
