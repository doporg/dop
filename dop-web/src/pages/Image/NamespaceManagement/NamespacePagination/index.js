import React, {Component} from 'react';
import API from "../../../API"
import Axios from "axios";
import {Input,Table,Grid} from '@icedesign/base';
import {Link} from 'react-router-dom';
import TopBar from '../TopBar';
import DeleteNamespaceDialog from '../DeleteNameSpaceDialog';
import CreateNamespaceDialog from '../CreateNamespaceDialog';


const {Row} = Grid;
export default class NamespacePagination extends Component {
    //构造方法
    constructor(props) {
        super();
        this.state = {
            currentData: [],
            rowSelection: {
                onChange: this.onChange.bind(this),
                selectedRowKeys: []
            },
            //总页数
            totalPage: 1,
            queryKey: props.searchKey,
            loading: true
        };
    }

    onChange(projectIds, records) {
        let {rowSelection} = this.state;
        rowSelection.selectedRowKeys = projectIds;
        console.log("onChange",rowSelection.selectedRowKeys, records);
        this.setState({rowSelection});
    }

    //获取最新数据并刷新
    refreshList(state, searchKey) {
        let url = API.test_image + '/v1/projects';
        let _this = this;
        Axios.get(url, {
            headers: {
                "x-login-user": 37
            }

        })
            .then(function (response) {
                console.log("镜像信息");
                console.log(response.data);
                _this.setState({
                    currentData: response.data
                });

            })

    }

    componentWillMount() {
        this.refreshList();
    }

    //搜索框内容变化
    onSearch(value) {
        this.setState({
            searchKey: value
        })
    }


    //链接 跳转到对应的命名空间
    idRender = function (id) {
        return <Link to={"/image/projects/" + id + "/repos"}
        >{id}</Link>
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

                        <Table.Column title="命名空间是否公开"
                                      dataIndex="metadata.public"/>

                        <Table.Column title="镜像仓库数量"
                                      dataIndex="repoCount"/>
                    </Table>
                </Row>

            </div>
        )
    }
}