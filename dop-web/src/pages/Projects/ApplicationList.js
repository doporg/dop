/**
 * 应用列表（没开始用）
 * @author Bowen
 */

import React, {Component} from 'react';
import API from "../API";
import Axios from "axios";
import TopBar from "./Projects";
import {Input} from "@icedesign/base";

export default class ApplicationList extends Component {
    static displayName = 'ApplicationList';

    componentWillMount() {
        this.Get()
    }

    Get() {
        let url = API.axiosGetTest + "/getTest";
        Axios.get(url).then((response) => {
            // console.log(response)
        }).catch()
    }


    render() {
        return (
            <div>
                <TopBar
                    extraBefore={
                        <Input
                            size="large"
                            placeholder="请输入关键字进行搜索"
                            style={{width: '240px'}}
                        />
                    }
                    extraAfter={< CreateProjectDialog/>
                    }
                />
                <Pagination/>
            </div>

        );
    }
}
