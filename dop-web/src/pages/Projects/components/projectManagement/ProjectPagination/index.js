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
            queryKey: props.searchKey
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
        let tmpData = [];
        let url = API.gateway + '/application-server/project';
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
                    pageSize: response.data.pageSize
                });
                tmpData = response.data.pageList;

                console.log(tmpData)
                let getList = []
                for (let i = 0; i < tmpData.length; i++) {
                    let nameUrl = API.gateway + '/user-server/v1/users/' + tmpData[i].cuser;
                    getList.push(Axios.get(nameUrl));
                }

                Axios.all(getList).then(Axios.spread(function (...resList) {
                    console.log(resList);
                    for (let i = 0; i < resList.length; i++) {
                        if (resList[i].data != "")
                            tmpData[i].cuser = resList[1].data.name;
                    }
                    console.log(tmpData);
                    _this.setState({
                        currentData: tmpData
                    });
                }))

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
        let tmpData = [];
        console.log("searching" + key);
        let _this = this;
        let url = API.gateway + '/application-server/project';
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
                    totalCount: response.data.totalCount,
                    pageSize: response.data.pageSize,
                    queryKey: key
                    // searchData: response.data.pageList
                });
                tmpData = response.data.pageList;

                console.log(tmpData)
                let getList = []
                for (let i = 0; i < tmpData.length; i++) {
                    let nameUrl = API.gateway + '/user-server/v1/users/' + tmpData[i].cuser;
                    getList.push(Axios.get(nameUrl));
                }

                Axios.all(getList).then(Axios.spread(function (...resList) {
                    console.log(resList);
                    for (let i = 0; i < resList.length; i++) {
                        if (resList[i].data != "")
                            tmpData[i].cuser = resList[1].data.name;
                    }
                    console.log(tmpData);
                    _this.setState({
                        currentData: tmpData
                    });

                }))
            })
    }

    //
    //
    // if (nextProps.createdProjectNeedRefresh) {
    //     console.log("going to handle change");
    //     let tmpData = [];
    //     let url = API.gateway + '/application-server/project';
    //     let _this = this;
    //     let current = this.state.current;
    //     Axios.get(url, {
    //         params: {
    //             pageNo: current,
    //             pageSize: 15,
    //             includeFinished: false
    //         }
    //     })
    //         .then(function (response) {
    //             console.log(response);
    //             _this.setState({
    //                 totalCount: response.data.totalCount,
    //                 pageSize: response.data.pageSize
    //                 // searchData: response.data.pageList
    //             });
    //             tmpData = response.data.pageList;
    //
    //             console.log(tmpData)
    //             let getList = []
    //             for (let i = 0; i < tmpData.length; i++) {
    //                 let nameUrl = API.gateway + '/user-server/v1/users/' + tmpData[i].cuser;
    //                 getList.push(Axios.get(nameUrl));
    //             }
    //
    //             Axios.all(getList).then(Axios.spread(function (...resList) {
    //                 console.log(resList);
    //                 for (let i = 0; i < resList.length; i++) {
    //                     if (resList[i].data != "")
    //                         tmpData[i].cuser = resList[1].data.name;
    //                 }
    //                 console.log(tmpData);
    //                 _this.setState({
    //                     currentData: tmpData
    //                 });
    //
    //             }))
    //         }
    //
    //     nextProps.refreshFinished();
    // }


    /*
    *加载初始数据
     */
    componentDidMount() {

        let url = API.gateway + '/application-server/project';
        let _this = this;
        let current = this.state.current;
        let tmpData = [];
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
                    totalCount: response.data.totalCount,
                    pageSize: response.data.pageSize
                    // searchData: response.data.pageList
                })
                tmpData = response.data.pageList;

                console.log(tmpData)
                let getList = []
                for (let i = 0; i < tmpData.length; i++) {
                    let nameUrl = API.gateway + '/user-server/v1/users/' + tmpData[i].cuser;
                    getList.push(Axios.get(nameUrl));
                }

                Axios.all(getList).then(Axios.spread(function (...resList) {
                    console.log(resList);
                    for (let i = 0; i < resList.length; i++) {
                        if (resList[i].data != "")
                            tmpData[i].cuser = resList[1].data.name;
                    }
                    console.log(tmpData);
                    _this.setState({
                        currentData: tmpData
                    });

                }))

                console.log("didmount");
            })
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






