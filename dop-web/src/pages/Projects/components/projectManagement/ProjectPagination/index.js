/**
 * 项目列表翻页器
 * @author Bowen
 **/
import {Pagination} from "@icedesign/base";
import React, {Component} from 'react';
import API from "../../../../API.js"
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
            current: 1,
            //当前页的数据
            currentData: [],
            //总页数
            totalPage: 1,
            // searchData: [],
            // searchKey: ""
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
                includeFinished: false,
                queryKey: this.state.searchKey
            }
        })
            .then(function (response) {
                console.log(response)
                _this.setState({
                    current: current,
                    currentData: response.data.pageList,
                    pageSize: response.data.pageSize
                });

            })
            .catch(function (error) {
                console.log(error);
            });
        // let key = this.state.key;
        // this.searchInData(key);
    }

// searchInData(key){
//         this.setState({
//             searchKey:key,
//             searchData: this.state.currentData.filter( record => {
//                 return (String(record.ctime).indexOf(key) > -1||String(record.title).indexOf(key) > -1||String(record.cuser).indexOf(key) > -1)
//             }),
//             totalCount: this.state.searchData.length
//         })
//
// }
    /**
     *
     * 接受来自父组件的刷新请求
     *
     */
    componentWillReceiveProps(nextProps, nextContext) {
        console.log(nextProps);
        let key = nextProps.searchKey;
        console.log(key);
        // this.searchInData(key);

        console.log("searching" + key);
        let _this = this;
        let url = API.application + '/projects';
        let current = this.state.current
        Axios.get(url, {
            params: {
                pageNo: 1,
                pageSize: 15,
                includeFinished: false,
                queryKey: key
            }
        })
            .then(function (response) {
                console.log(response);
                _this.setState({
                    currentData: response.data.pageList,
                    totalCount: response.data.totalCount,
                    pageSize: response.data.pageSize,
                    queryKey: key
                    // searchData: response.data.pageList
                });
            })
            .catch(function (error) {
                console.log(error);
            });


        if (nextProps.createdProjectNeedRefresh) {
            console.log("going to handle change");

            let url = API.application + '/projects';
            let _this = this;
            let current = this.state.current;
            Axios.get(url, {
                params: {
                    pageNo: current,
                    pageSize: 15,
                    includeFinished: false
                }
            })
                .then(function (response) {
                    console.log(response);
                    _this.setState({
                        currentData: response.data.pageList,
                        totalCount: response.data.totalCount,
                        pageSize: response.data.pageSize
                        // searchData: response.data.pageList
                    });
                })
                .catch(function (error) {
                    console.log(error);
                });
            nextProps.refreshFinished();
        }
    }

    /*
    *加载初始数据
     */
    componentDidMount() {

        let url = API.application + '/projects';
        let _this = this;
        let current = this.state.current;
        Axios.get(url, {
            params: {
                pageNo: current,
                pageSize: 15,
                includeFinished: false
            }
        })
            .then(function (response) {
                _this.setState({
                    currentData: response.data.pageList,
                    totalCount: response.data.totalCount,
                    pageSize: response.data.pageSize
                    // searchData: response.data.pageList
                })
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    render() {
        return (
            /*
        * 将项目列表作为翻页器的子组件，数据由翻页器传递给应用列表显示
         */
            <div>
                <ProjectList currentData={this.state.currentData}/>
                <Pagination style={styles.body} current={this.state.current} onChange={this.handleChange}
                            pageSize={this.state.pageSize} total={this.state.totalCount}/>
            </div>
        )
    }

}






