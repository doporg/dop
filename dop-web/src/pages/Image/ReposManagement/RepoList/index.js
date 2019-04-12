import React, {Component} from 'react';
import {Input, Loading, Table} from '@icedesign/base';
import {Grid} from '@icedesign/base';
import {Col} from "@alifd/next/lib/grid";
import {Link} from 'react-router-dom';
import DeleteRepoDialog from "../DeleteRepoDialog";
import TopBar from "../TopBar"

const {Row} = Grid;

export default class RepoList extends Component {

    static displayName = 'RepoList';


    constructor(props) {
        super(props);
        this.state = {
            currentData: [],
            rowSelection: {
                onChange: this.onChange.bind(this),
                selectedRowKeys: []
            },
            pageSize :10,
            loading: true,
            refreshRepoList:this.props.refreshList
        };

    }


    //选择器监听
    onChange(names, records) {
        let {rowSelection} = this.state;
        rowSelection.selectedRowKeys = names;
        console.log("onChange",rowSelection.selectedRowKeys, records);
        this.setState({rowSelection});
    }


    //接收父组件的参数
    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({
            currentData: nextProps.currentData,
            loading :false
        });
    }


    onSearch(value){

    }

    nameRender=(value, index, record)=> {
        //链接到对应的镜像列表
        return <Link to={"/image/projects/"+record.projectId+"/repos/"+value+"/images"}
        >{value}</Link>
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
                    extraAfter={<DeleteRepoDialog deleteKeys={this.state.rowSelection.selectedRowKeys} refreshRepoList={this.refreshRepoList}/>
                    }
                />

                <Row wrap gutter="20">
                    <Col>
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
                    </Col>
                </Row>
            </div>
        );
    }

}