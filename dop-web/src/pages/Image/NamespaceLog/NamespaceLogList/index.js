import React,{Component} from 'react';
import {Input, Loading, Pagination, Table} from '@icedesign/base';
import IceContainer from '@icedesign/container';
import {Grid} from "@icedesign/base/index";
import API from "../../../API";
import Axios from "axios/index";

const {Row} = Grid;
export default class NamespaceLogList extends Component{
    static displayName = "NamespaceLogList";

    constructor(props) {
        super(props);
        this.state = {
            currentData: [],
            current:1,
            totalCount:0,
            pageSize:10,
            loading: true,
            id: this.props.projectId,
            queryKey:""
        };

    }

    refreshList(current,queryKey){
        let url = API.image+"/v1/projects/"+this.state.id+"/logs"
        let _this = this;
        if(queryKey!==""){
            Axios.get(url, {
                params:{
                    page:current,
                    pageSize: this.state.pageSize,
                    username:queryKey
                }
            })
                .then(function (response) {
                    console.log("日志信息");
                    console.log(response.data);
                    if (response.data.totalCount!==0){
                        _this.setState({
                            currentData: response.data.contents,
                            totalCount:response.data.totalCount,
                            loading:false
                        });
                    } else {
                        _this.setState({
                            currentData:[],
                            totalCount:0,
                            loading:false,
                        })
                    }
                })
        }else {
            Axios.get(url, {
                params:{
                    page:current,
                    pageSize: this.state.pageSize
                }
            })
                .then(function (response) {
                    console.log("日志信息");
                    console.log(response.data);
                    if (response.data.totalCount!==0){
                        _this.setState({
                            currentData: response.data.contents,
                            totalCount:response.data.totalCount,
                            loading:false
                        });
                    } else {
                        _this.setState({
                            currentData:[],
                            totalCount:0,
                            loading:false,
                        })
                    }
                })
        }
    }

    onSearch(value){
        this.setState({
            queryKey:value
        });
        this.refreshList(this.state.current,value)
    }


    handleChange(current,e){
        this.setState({
            current:current
        });
        this.refreshList(current,this.state.queryKey)
    }


    componentWillMount() {
        this.refreshList(this.state.current,this.state.queryKey)
    }

    render() {
        return (
            <div>
                <IceContainer title={"检索条件"}>
                    <Row wrap>
                        <Input placeholder={"请输入关键字"} onChange={this.onSearch.bind(this)}/>
                    </Row>
                </IceContainer>

                <IceContainer title={"日志信息"}>
                    <Loading visible={this.state.loading} shape="dot-circle" color="#2077FF">
                        <Table dataSource={this.state.currentData}
                               isLoading={this.state.isLoading}
                               primaryKey="logId"
                        >

                            <Table.Column title="用户名称"
                                          dataIndex="username"/>

                            <Table.Column title="镜像名称"
                                          dataIndex="repoName"/>

                            <Table.Column title="标签"
                                          dataIndex="repoTag"/>
                            <Table.Column title="操作"
                                          dataIndex="operation"/>
                            <Table.Column title="时间戳"
                                          dataIndex="opTime"/>
                        </Table>
                    </Loading>

                    <Pagination className={"body"}
                                current={this.state.current}
                                onChange={this.handleChange.bind(this)}
                                pageSize={this.state.pageSize}
                                total={this.state.totalCount}
                                hideOnlyOnePage={true}/>
                </IceContainer>
            </div>
        )
    };
}