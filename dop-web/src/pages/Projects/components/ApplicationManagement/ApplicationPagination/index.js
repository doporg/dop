/**
 * 应用列表翻页器
 * @author Bowen
 **/
import {Pagination} from "@icedesign/base";
import React, {Component} from 'react';
import API from "../../../../API.js"
import Axios from "axios";
import ApplicationList from "../ApplicationList";


const styles = {
    body: {
        position: 'relative',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
    }
}
export default class ApplicationPagination extends Component {
    constructor(props) {
        super(props);

        this.state = {
            //当前页数
            current: 1,
            //当前页的数据，传递给子组件渲染
            currentData: [],
            //总页数
            totalPage: 1,
            //当前页面显示的应用所属的项目id
            projectId: props.projectId,
            // searchData: [],
            searchKey: props.searchKey
        };
        console.log(props);
        this.handleChange = this.handleChange.bind(this);
    }

    /*
     *
     * 当处理改变（分页器改变、父组件请求刷新）
     */
    handleChange(current) {
        let url = API.gateway + '/application-server/application';
        let _this = this;
        Axios.get(url, {
            params: {
                pageNo: current,
                pageSize: 15,
                projectId: this.state.projectId,
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
        let url = API.gateway + '/application-server/application';
        let current = this.state.current
        Axios.get(url, {
            params: {
                pageNo: 1,
                pageSize: 15,
                queryKey: key,
                projectId: this.state.projectId
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

        //
        if (nextProps.createdApplicationNeedRefresh) {
            console.log("going to handle change");

            let _this = this;
            let url = API.gateway + '/application-server/application';
            let current = this.state.current
            Axios.get(url, {
                params: {
                    pageNo: 1,
                    pageSize: 15,
                    queryKey: key,
                    projectId: this.state.projectId
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

        let url = API.gateway + '/application-server/application';
        let _this = this;
        let current = this.state.current;
        Axios.get(url, {
            params: {
                pageNo: current,
                pageSize: 15,
                queryKey: this.state.searchKey,
                projectId: this.state.projectId
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
        /*
        * 将应用列表作为翻页器的子组件，数据由翻页器传递给应用列表显示
         */
        return (
            <div>
                <ApplicationList currentData={this.state.currentData}/>


                <Pagination style={styles.body} current={this.state.current} onChange={this.handleChange}
                            pageSize={this.state.pageSize} total={this.state.totalCount}/>
            </div>
        )
    }

}






