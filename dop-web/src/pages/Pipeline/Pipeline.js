/**
 *  用于流水线首页，流水线概览
 *  @author zhangfuli
 *
 * */
import React, { Component } from 'react';
import { Link } from 'react-router-dom';


export default class Pipeline extends Component {

    render(){
        return (
            <Link to='/pipeline/new'>
                <button >new</button>
            </Link>
        )
    };
}
