
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

/**
 * 项目列表翻页器
 * @author Bowen
 **/
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

    //刷新数据列表

    refreshList(current, key) {
        let tmpData = [];
        let url = API.gateway + '/application-server/project';
        let _this = this;
        Axios.get(url, {
            params: {
                pageNo: current,
                pageSize: 15,
                includeFinished: false,
                queryKey: key
            }
        })
            .then(function (response) {
                console.log(response)
                _this.setState({
                    current: current,
                    pageSize: response.data.pageSize,
                    totalCount: response.data.totalCount
                });

                //获取用户数据
                tmpData = response.data.pageList;

                //使用SET数组去重，获取所有不重复的用户ID
                let userIdList = new Set();
                console.log(tmpData)
                for (let i = 0; i < tmpData.length; i++) {
                    userIdList.add(tmpData[i].cuser);
                }
                console.log("userIdList", userIdList)

                //存放所有请求URL的数组
                let getList = []
                for (let id of userIdList) {
                    let nameUrl = API.gateway + '/user-server/v1/users/' + id;
                    getList.push(Axios.get(nameUrl));
                }
                console.log("getList", getList)

                //存放最终结果的数组，使用finalList[ID]---NAME的哈希映射
                let finalList = [];

                //将所有URL请求发出
                Axios.all(getList).then(Axios.spread(function (...resList) {
                    console.log("resList", resList);
                    for (let i = 0; i < resList.length; i++) {
                        //如果该值不为空则添加到哈希表中
                        if (resList[i].data != "") {
                            finalList[resList[i].data.id] = resList[i].data.name;
                        } else {
                            finalList[resList[i].data.id] = "";
                        }
                    }
                    console.log("finalList", finalList)

                    //将所有ID置换为NAME
                    for (let i = 0; i < tmpData.length; i++) {
                        tmpData[i].cuser = finalList[tmpData[i].cuser];
                    }
                    console.log(tmpData);
                    //赋值
                    _this.setState({
                        currentData: tmpData
                    });
                }))

            })
            .catch(function (error) {
                console.log(error);
            });

    }

    /*
     *
     * 当处理改变（分页器改变、父组件请求刷新）
     */
    handleChange(current) {
        this.refreshList(current, this.state.searchKey);
    }

    /**
     *
     * 接受来自父组件的刷新请求
     *
     */
    componentWillReceiveProps(nextProps, nextContext) {
        let key = nextProps.searchKey;
        this.refreshList(1, key);
    }


    /*
    *加载初始数据
     */
    componentDidMount() {
        this.refreshList(1, "");
    }

    render() {
        return (
            /*
        * 将项目列表作为翻页器的子组件，数据由翻页器传递给应用列表显示
         */
            <div>
                <ProjectList currentData={this.state.currentData}/>
                <Pagination style={styles.body}
                            current={this.state.current}
                            onChange={this.handleChange}
                            pageSize={this.state.pageSize}
                            total={this.state.totalCount}/>
            </div>
        )
    }

}






