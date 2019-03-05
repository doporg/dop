/**
 * 项目列表翻页器
 * @author Bowen
 **/
import {Pagination} from "@icedesign/base";
import React, {Component} from 'react';
import API from "../../../API";
import Axios from "axios";
import ProjectList from "../ProjectList";


const styles = {
    body: {
        position: 'relative',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
    }
}
export default class ProjectPagination extends Component {
    constructor(props) {
        super(props);

        this.state = {
            //当前页数
            current: props.currentPage,
            //当前页的数据
            currentData: [],
            //总页数
            totalPage: 1
        };

        this.handleChange = this.handleChange.bind(this);
    }

    /*
     *
     * 当处理改变（分页器改变、父组件请求刷新）
     */
    handleChange(current) {
        let url = API.application + '/projects';
        let _this = this;
        Axios.get(url, {
            params: {
                pageNo: current,
                pageSize: 15,
                includeFinished: false
            }
        })
            .then(function (response) {
                console.log(response)
                _this.setState({
                    current: current,
                    currentData: response.data
                });

            })
            .catch(function (error) {
                console.log(error);
            });
    }

    /**
     *
     * 接受来自父组件的刷新请求
     *
     */
    componentWillReceiveProps(nextProps, nextContext) {
        console.log(nextProps)
        if (nextProps.createdProjectNeedRefresh) {
            console.log("going to handlechange");
            this.handleChange(this.state.current)
            nextProps.refreshFinished();
        }
    }

    /*
    *加载初始数据
     */
    componentDidMount() {

        let url = API.application + '/projects';
        let _this = this;
        Axios.get(url, {
            params: {
                pageNo: this.state.current,
                pageSize: 15,
                includeFinished: false
            }
        })
            .then(function (response) {
                _this.setState({
                    currentData: response.data
                })
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    render() {
        return (
            <div>
                <ProjectList currentData={this.state.currentData}/>
                <Pagination style={styles.body} current={this.state.current} onChange={this.handleChange}/>
            </div>
        )
    }
}






