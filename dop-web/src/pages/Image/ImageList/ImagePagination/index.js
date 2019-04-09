import React,{Component} from 'react';
import TopBar from '../TopBar'
import {Grid, Input, Loading, Pagination, Table} from "@icedesign/base";
import DeleteImageDialog from "../deleteImageDialog";
import {Col} from "@alifd/next/lib/grid";


const {Row} = Grid;
const styles = {
    body: {
        position: 'relative',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
    }
};

export default class ImagePagination extends Component{

    constructor(props){
        super(props);
        this.state={
            imageData:[],
            current:0,
            pageSize:10,
            totalCount:12,
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
    handleChange(){

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
                            <Table.Column title="创建时间"
                                          dataIndex="created"/>
                            <Table.Column title="标签"
                                          dataIndex="labels"/>
                            <Table.Column title="摘要"
                                          dataIndex="digest"/>
                        </Table>
                    </Col>
                </Row>
                <Pagination style={styles.body}
                            current={this.state.current}
                            onChange={this.handleChange.bind(this)}
                            pageSize={this.state.pageSize}
                            total={this.state.totalCount}/>
            </div>
        )
    }
}