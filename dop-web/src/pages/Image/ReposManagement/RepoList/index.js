import React, {Component} from 'react';
import {Input, Loading, Pagination, Table} from '@icedesign/base';
import {Grid} from '@icedesign/base';
import {Col} from "@alifd/next/lib/grid";
import {Link} from 'react-router-dom';
import IceContainer from '@icedesign/container';
import DeleteRepoDialog from "../DeleteRepoDialog";
import "../../Style.scss"
import API from "../../../API";
import Axios from "axios";

const {Row} = Grid;

export default class RepoList extends Component {

    static displayName = 'RepoList';


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
        let url = API.image + '/v1/projects/'+this.state.id+'/repositories';
        let _this = this;

        if (queryKey===""){
            //不存在搜索条件
            Axios.get(url, {
                params:{
                    page:current,
                    pageSize: this.state.pageSize,
                }
            })
                .then(function (response) {
                    console.log("镜像仓库信息");
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
                }).catch(function (errors) {
                    console.log("error")
            })
        } else {
            //存在搜索条件
            Axios.get(url, {
                params:{
                    page:current,
                    pageSize: this.state.pageSize,
                    q:queryKey
                }
            })
                .then(function (response) {
                    console.log("镜像仓库信息");
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

    componentWillMount() {
        this.refreshList(1,this.state.queryKey)
    }



    onSearch(value){
        console.log("search",value);
        this.setState({
            queryKey:value
        });
        this.refreshList(this.state.current,value)
    }

    nameRender=(value, index, record)=> {
        //链接到对应的镜像列表
        return <Link to={"/repos/"+value+"/images"}
        >{value}</Link>
    };

    render() {
        return (
            <div>
                <IceContainer title={"检索条件"}>
                    <Row wrap>
                        <Input placeholder={"请输入关键字"} onChange={this.onSearch.bind(this)}/>
                    </Row>
                </IceContainer>

                <IceContainer title={"镜像仓库列表"}>
                    <Row wrap className="headRow">
                        <Col l="12">
                            <DeleteRepoDialog deleteKeys={this.state.rowSelection.selectedRowKeys} refreshRepoList={this.refreshList.bind(this)}/>
                        </Col>
                    </Row>
                    <Loading visible={this.state.loading} shape="dot-circle" color="#2077FF">
                        <Table dataSource={this.state.currentData}
                               rowSelection={this.state.rowSelection}
                               isLoading={this.state.isLoading}
                               primaryKey="name"
                        >

                            <Table.Column title="镜像仓库名称" cell={this.nameRender}
                                          dataIndex="name"/>

                            <Table.Column title="标签数"
                                          dataIndex="tagsCount"/>

                            <Table.Column title="下载数"
                                          dataIndex="pullCount"/>
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