import React,{Component} from 'react';
import TopBar from '../TopBar'
import {Grid, Input, Table} from "@icedesign/base";
import DeleteImageDialog from "../deleteImageDialog";
import {Col} from "@alifd/next/lib/grid";


const {Row} = Grid;

export default class ImagePagination extends Component{

    constructor(props){
        super(props);
        this.state={
            imageData:[],
            rowSelection: {
                onChange: this.onChange.bind(this),
                selectedRowKeys: []
            },
            loading: true,
            repoName: this.props.repoName,
            refreshImageList: this.props.refreshImageList
        }
    }

    //接收父组件的参数
    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({
            imageData: nextProps.imageData
        });
    }

    onChange(names, records){
        let {rowSelection} = this.state;
        rowSelection.selectedRowKeys = names;
        console.log("onChange",rowSelection.selectedRowKeys, records);
        this.setState({rowSelection});
    }

    onSearch(value){
        this.setState({
            searchKey: value
        })
        console.log("search",value)
    }
    //image列表
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
                    extraAfter={<DeleteImageDialog repoName={this.state.repoName} deleteKeys={this.state.rowSelection.selectedRowKeys} refreshImageList={this.state.refreshImageList}/>
                    }
                />
                <Row wrap gutter="20">
                    <Col>
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
                        </Table>
                    </Col>
                </Row>
            </div>
        )
    }
}