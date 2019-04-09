import React, {Component} from 'react';
import API from "../../../API"
import Axios from "axios";
import {Input,Table,Grid,Pagination,Loading,Switch,Feedback} from '@icedesign/base';
import {Link} from 'react-router-dom';
import TopBar from '../TopBar';
import DeleteNamespaceDialog from '../DeleteNameSpaceDialog';
import CreateNamespaceDialog from '../CreateNamespaceDialog';


const {Row} = Grid;
const Toast = Feedback.toast;

const styles = {
    body: {
        position: 'relative',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
    }
};

export default class NamespacePagination extends Component {
    //构造方法
    constructor(props) {
        super(props);
        this.state = {
            current: 0,
            currentData: [],
            rowSelection: {
                onChange: this.onChange.bind(this),
                selectedRowKeys: []
            },
            //总页数
            pageSize :10,
            totalCount: 0,
            queryKey: props.searchKey,
            loading: true
        };
        this.handleChange = this.handleChange.bind(this);
    }

    //选择器监听
    onChange(projectIds, records) {
        let {rowSelection} = this.state;
        rowSelection.selectedRowKeys = projectIds;
        console.log("onChange",rowSelection.selectedRowKeys, records);
        this.setState({rowSelection});
    }


    //获取最新数据并刷新
    refreshList(current, searchKey) {
        console.log(this.state.current);
        let countUrl = API.image + '/v1/statistics';
        let url = API.image + '/v1/projects';
        let _this = this;
        //获取数量
        Axios.get(countUrl, {})
            .then(function (response) {
                console.log("统计信息");
                console.log(response.data.publicProjectCount+response.data.privateProjectCount);
                _this.setState({
                    totalCount:response.data.publicProjectCount+response.data.privateProjectCount
                })
            })
        //获取数据
        Axios.get(url, {
            params:{
                page:current,
                pageSize: this.state.pageSize

            }

        })
            .then(function (response) {
                console.log("镜像信息");
                console.log(response.data.length);
                _this.setState({
                    currentData: response.data,
                    loading: false,
                    current: current
                });
            })

    }

    //初始化
    componentWillMount() {
        this.refreshList();
    }

    //搜索框内容变化
    onSearch(value) {
        console.log("search ",value)
        this.refreshList(1,value);
    }

    //分页器监听
    handleChange(current,e){
        this.refreshList(current,this.state.pageSize)
    }


    //链接跳转到对应的命名空间
    idRender = function (id) {
        return <Link to={"/image/projects/" + id + "/repos"}
        >{id}</Link>
    };
    //命名空间公开状态监听
    renderSwitch = (value,index,record) => {
        if (value==="true"){
            return <Switch onChange={(checked)=>{
                let namespaceId = record.projectId;
                let url = API.image+"/v1/projects/"+namespaceId+"/metadatas/public"
                //修改命名空间状态
                let temp = "";
                if (checked){
                    temp = "true";
                }else {
                    temp = "false";
                }
                Axios.put(url,{}, {
                    params:{
                        "publicStatus":temp
                    }
                }).then(function (response) {
                    Toast.success("修改状态成功！");
                    console.log(response.status);
                }).catch(function (error) {
                    console.log(error);
                    Toast.error("修改失败,请确认权限后重试！");
                });
            }
            } defaultChecked={true} />
        }else {
            return <Switch onChange={(checked)=>
            {
                let namespaceId = record.projectId;
                let url = API.image+"/v1/projects/"+namespaceId+"/metadatas/public"
                //修改命名空间状态
                let temp = "";
                if (checked){
                    temp = "true";
                }else {
                    temp = "false";
                }
                Axios.put(url, {},{
                    params:{
                        "publicStatus":temp
                    }
                }).then(function (response) {
                    Toast.success("修改状态成功！");
                    console.log(response.status);
                }).catch(function (error) {
                    console.log(error);
                    Toast.error("修改失败,请确认权限后重试！");
                });
            }
            } defaultChecked={false}/>
        }

    };

    render() {
        return (
            <div>
                <TopBar
                    extraBefore={
                        <Input
                            size="large"
                            placeholder="请输入关键字进行搜索"
                            style={{width: '240px'}}
                            // hasClear
                            onChange={this.onSearch.bind(this)}
                        />
                    }
                    extraDelete={
                        <DeleteNamespaceDialog deleteKeys={this.state.rowSelection.selectedRowKeys} refreshProjectList={this.refreshList.bind(this)}/>
                    }
                    extraAfter={< CreateNamespaceDialog refreshProjectList={this.refreshList.bind(this)}/>
                    }
                />
                <Loading visible={this.state.loading} shape="dot-circle" color="#2077FF">

                    <Row wrap gutter="20">
                        <Table dataSource={this.state.currentData}
                               rowSelection={this.state.rowSelection}
                               isLoading={this.state.isLoading}
                               primaryKey='projectId'>
                            <Table.Column cell={this.idRender}
                                          title="命名空间ID"
                                          dataIndex="projectId"/>

                            <Table.Column title="命名空间名称"
                                          dataIndex="name"/>

                            <Table.Column title="私有/公开"
                                          dataIndex="metadata.public" width={100} cell={this.renderSwitch}/>

                            <Table.Column title="镜像仓库数量"
                                          dataIndex="repoCount"/>
                            <Table.Column title="创建时间"
                                          dataIndex="creationTime"/>
                        </Table>
                    </Row>
                    <Pagination style={styles.body}
                                current={this.state.current}
                                onChange={this.handleChange.bind(this)}
                                pageSize={this.state.pageSize}
                                total={this.state.totalCount}/>
                </Loading>



            </div>
        )
    }
}