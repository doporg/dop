import React, {Component} from 'react';
import { Table, Button } from '@icedesign/base';
import { Link } from 'react-router-dom';
import API from "../../API";
import Axios from "axios/index";

const timetrans = (timestamp)=>{
    timestamp = timestamp.length === 13? parseInt(timestamp):parseInt(timestamp)*1000;
    let date = new Date(timestamp);//如果date为13位不需要乘1000
    let Y = date.getFullYear() + '-';
    let M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
    let D = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate()) + ' ';
    let h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
    let m = (date.getMinutes() <10 ? '0' + date.getMinutes() : date.getMinutes());
    return Y+M+D+h+m;
}

export default class  PipelineTable extends Component{
    constructor(props){
        super(props);
        this.state = {
            isLoading: false,
            dataSource: [],
            current: 1,
        }
    }
    componentWillMount(){
        this.getPipeline();
    }

    getPipeline(){
        let url = API.pipeline + '/pipeline/findAll';
        let self = this;
        Axios.get(url).then((response)=>{
            self.setState({
                dataSource: response.data
            });
        })
    }
    removeById(id){
        let data = {id: id};
        let url = API.pipeline + '/pipeline/remove';
        let self = this;
        Axios.post(
            url,
            data
        ).then((response)=>{
            let pipelineInfo = this.state.dataSource;
            if(response.data === 'remove'){
                pipelineInfo.splice(pipelineInfo.findIndex((value)=>{
                    return value.id === id
                }), 1);

                self.setState({
                    dataSource: pipelineInfo
                })
            }
        })
    }

    /**
     *  表格 序号栏配置
     * */
    renderIndex(value, index, record){
        return index + 1;
    };
    /**
     *  表格 创建时间
     * */
    renderCreateTime(value, index, record){
        return timetrans(value)
    }
    /**
     *  表格 操作栏配置
     * */
    renderOperation(value, index, record) {
        let router = "/pipeline/project/" + record.id;
        return (
            <div>
                <Link to = {router}>
                    <Button type="primary" size="small">查看</Button>
                </Link>
                <Button
                    type="primary"
                    shape="warning"
                    size="small"
                    style={styles.button}
                    onClick={this.removeById.bind(this, record.id)}
                >删除</Button>
            </div>
        );
    };

    render(){
        const columns = [{
            title: '序号',
            width: 5,
            dataIndex: 'index',
            cell: this.renderIndex
        },{
            title: '流水线名称',
            width: 10,
            dataIndex: 'name',
        },{
            title: '创建时间',
            width: 10,
            dataIndex: 'createTime',
            cell: this.renderCreateTime
        },{
            title: '运行状态',
            width: 20,
            dataIndex: 'status',
        },{
            title: '管理员',
            width: 8,
            dataIndex: 'admin',
        },{
            title: '操作',
            width: 10,
            dataIndex: 'operation',
            cell: this.renderOperation.bind(this)
        }];
        return(
            <div>
                <Table
                    loading={this.state.isLoading}
                    dataSource={this.state.dataSource}
                    hasBorder={false}
                >
                    {columns.map((item, index) => {
                        return (
                            <Table.Column
                                key={index}
                                title={item.title}
                                width={item.width || 'auto'}
                                dataIndex={item.dataIndex}
                                cell={item.cell || (value => value)}
                            />
                        );
                    })}
                </Table>
                {/*<Pagination*/}
                    {/*current={this.state.current}*/}
                    {/*onChange={this.handlePaginationChange}*/}
                {/*/>*/}
            </div>
        )
    }
}
const styles = {
    table: {
        marginTop: '10px',
        minHeight: '500px',
    },
    pagination: {
        margin: '20px 0',
        textAlign: 'right',
    },
    button: {
        marginLeft: '10px'
    }
};
