import React,{Component} from "react";
import IceContainer from '@icedesign/container';
import {Grid, Input, Loading, Pagination, Table} from "@icedesign/base";
import {Col} from "@alifd/next/lib/grid";
import API from "../../../API";
import Axios from "axios";
import AddMemberDialog from "../AddMemberDialog";


const {Row} = Grid;
export default class MemberList extends Component{

    constructor(props) {
        super(props);
        this.state = {
            currentData: [],
            current:1,
            totalCount:0,
            rowSelection: {
                onChange: this.onChange.bind(this),
                selectedRowKeys: []
            },
            pageSize:10,
            loading: true,
            id: this.props.projectId,
            queryKey:""
        };

    }

    refreshList(current,queryKey) {
        let url = API.image + '/v1/projects/'+this.state.id+'/members';
        let _this = this;

        if (queryKey===""){
            //不存在搜索条件
            Axios.get(url, {
                params:{
                    pageNo:current,
                    pageSize: this.state.pageSize,
                }
            })
                .then(function (response) {
                    console.log("项目成员信息");
                    console.log(response.data);
                    if (response.data.totalCount!==0){
                        _this.setState({
                            currentData: response.data.pageList,
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
                }).catch(function (errors) {
                console.log(errors)
            })
        } else {
            //存在搜索条件
            Axios.get(url, {
                params:{
                    pageNo:current,
                    pageSize: this.state.pageSize,
                    entityName:queryKey
                }
            })
                .then(function (response) {
                    console.log("项目成员信息");
                    console.log(response.data);
                    if (response.data.totalCount!==0){
                        _this.setState({
                            currentData: response.data.pageList,
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

    //选择器监听
    onChange(names, records) {
        let {rowSelection} = this.state;
        rowSelection.selectedRowKeys = names;
        console.log("onChange",rowSelection.selectedRowKeys, records);
        this.setState({rowSelection});
    }

    //分页器监听
    handleChange(current,e){
        console.log("pagination",current);
        this.setState({
            current:current
        });
        this.refreshList(current,this.state.queryKey)
    }


    onSearch(value){
        console.log("search",value);
        this.setState({
            queryKey:value
        });
        this.refreshList(this.state.current,value)
    }

    componentWillMount() {
        this.refreshList(1,this.state.queryKey);
    }

    render() {
        return (
            <div>
                <IceContainer title={"检索条件"}>
                    <Row wrap>
                        <Input placeholder={"请输入关键字"} onChange={this.onSearch.bind(this)}/>
                    </Row>
                </IceContainer>

                <IceContainer title={"成员列表"}>
                    <Row wrap className="headRow">
                        <Col l="12">
                            <AddMemberDialog projectId={this.state.id} refreshList={this.refreshList.bind(this)}/>
                        </Col>
                    </Row>
                    <Loading visible={this.state.loading} shape="dot-circle" color="#2077FF">
                        <Table dataSource={this.state.currentData}
                               rowSelection={this.state.rowSelection}
                               isLoading={this.state.isLoading}
                               primaryKey="id"
                        >

                            <Table.Column title="用户名称"
                                          dataIndex="entityName"/>

                            <Table.Column title="角色"
                                          dataIndex="roleName"/>

                            <Table.Column title="成员类型"
                                          dataIndex="entityType"/>
                        </Table>
                    </Loading>

                    <Pagination className={"body"}
                                current={this.state.current}
                                onChange={this.handleChange.bind(this)}
                                pageSize={this.state.pageSize}
                                total={this.state.totalCount}/>
                </IceContainer>
            </div>
        );
    }

}