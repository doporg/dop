/**
 *  用于流水线首页，流水线概览
 *  @author zhangfuli
 *
 * */
import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { Button } from '@icedesign/base';
import PipelineTable  from './components/Table'


export default class Pipeline extends Component {

    constructor(){
        super();
        this.state = {
            isLoading: false,
            dataSource: [],
            current: 1,
        };
    }
    componentWillMount(){
    }


    render(){
        return (
            <div>
                <Link to='/pipeline/new'>
                    <Button type="primary">新建流水线</Button><br /><br/>
                </Link>

                <PipelineTable/>
            </div>
        )
    };
}
