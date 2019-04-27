import React,{Component} from 'react';
import {Grid, Input, Loading, Pagination, Table} from "@icedesign/base";
import DeleteImageDialog from "../deleteImageDialog";
import {Col} from "@alifd/next/lib/grid";
import API from "../../../API";
import Axios from "axios";
import IceContainer from '@icedesign/container';


const {Row} = Grid;

export default class ImagePagination extends Component{

    constructor(props){
        super(props);
        this.state={
            imageData:[],
            current:1,
            pageSize:10,
            totalCount:0,
            rowSelection: {
                onChange: this.onChange.bind(this),
                selectedRowKeys: []
            },
            loading: true,
            repoName: this.props.repoName,
            queryKey:""
        }
    }

    refreshImageList(current,queryKey){
        let url = API.image + '/v1/repositories/'+this.state.repoName+"/images";
        let _this = this;
        if (queryKey!==""){
            Axios.get(url, {
                params:{
                    pageNo:current,
                    pageSize:10,
                }
            })
                .then(function (response) {
                    console.log("镜像信息");
                    console.log(response.data);
                    if (response.data.totalCount!==0){
                        _this.setState({
                            imageData: response.data.pageList,
                            totalCount:response.data.totalCount,
                            loading:false
                        });
                    }else {
                        _this.setState({
                            imageData: [],
                            totalCount:0,
                            loading:false
                        });
                    }

                })
        } else {
            Axios.get(url, {
                params:{
                    pageNo:current,
                    pageSize:10,
                }
            })
                .then(function (response) {
                    console.log("镜像信息");
                    console.log(response.data);
                    if (response.data.totalCount!==0){
                        _this.setState({
                            imageData: response.data.pageList,
                            totalCount:response.data.totalCount,
                            loading:false
                        });
                    } else {
                        _this.setState({
                            imageData: [],
                            totalCount:0,
                            loading:false
                        });
                    }

                })
        }


    }


    componentWillMount() {
        this.refreshImageList(1,this.state.queryKey)
    }

    onChange(names, records){
        let {rowSelection} = this.state;
        rowSelection.selectedRowKeys = names;
        console.log("onChange",rowSelection.selectedRowKeys, records);
        this.setState({rowSelection});
    }

    onSearch(value){
        this.setState({
            queryKey: value
        })
        this.refreshImageList(this.state.current,value);
    }
    handleChange(current,e){
        this.setState({
            current:current
        });
        this.refreshImageList(current,this.state.queryKey)
    }
    //image列表
    render() {
        return (
            <div>
                <IceContainer title={"检索条件"}>
                    <Row wrap>
                        <Input placeholder={"请输入关键字"} onChange={this.onSearch.bind(this)}/>
                    </Row>
                </IceContainer>

                <IceContainer title={"镜像版本列表"}>
                    <Row wrap className="headRow">
                        <Col l="12">
                            <DeleteImageDialog repoName={this.state.repoName} deleteKeys={this.state.rowSelection.selectedRowKeys} refreshImageList={this.refreshImageList.bind(this)}/>
                        </Col>
                    </Row>
                    <Loading visible={this.state.loading} shape="dot-circle" color="#2077FF">
                        <Table dataSource={this.state.imageData}
                               rowSelection={this.state.rowSelection}
                               isLoading={this.state.isLoading}
                               primaryKey="name"
                        >

                            <Table.Column title="标签"
                                          dataIndex="name"/>

                            <Table.Column title="大小"
                                          dataIndex="size"/>

                            <Table.Column title="创建人"
                                          dataIndex="author"/>

                            <Table.Column title="docker版本"
                                          dataIndex="dockerVersion"/>
                            <Table.Column title="创建时间"
                                          dataIndex="created"/>
                            <Table.Column title="标签"
                                          dataIndex="labels"/>
                            <Table.Column title="摘要"
                                          dataIndex="digest"/>
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
    }
}