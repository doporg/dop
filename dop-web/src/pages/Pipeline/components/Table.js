import React, {Component} from 'react';
import {Table, Button, Loading, Feedback} from '@icedesign/base';
import {Link} from 'react-router-dom';
import API from "../../API";
import Axios from "axios/index";

const { toast } = Feedback;
const timetrans = (timestamp) => {
    timestamp = timestamp.length === 13 ? parseInt(timestamp, 10) : parseInt(timestamp, 10) * 1000;
    let date = new Date(timestamp);//如果date为13位不需要乘1000
    let Y = date.getFullYear() + '-';
    let M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    let D = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate()) + ' ';
    let h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
    let m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes());
    return Y + M + D + h + m;
};

export default class PipelineTable extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isLoading: false,
            dataSource: [],
            current: 1,
            visible: false
        }
    }

    componentWillMount() {
        this.getPipeline();
    }

    getPipeline() {
        let url = API.pipeline + '/pipeline/findAll';
        let self = this;
        Axios.get(url).then((response) => {
            let dataSource = response.data.sort((a, b) => {
                return (a.name - b.name)
            });
            self.setState({
                dataSource
            });
        })
    }

    removeByIdSQL(id) {
        let data = {id: id};
        let url = API.pipeline + '/pipeline/remove';
        let self = this;
        Axios.post(
            url,
            data
        ).then((response) => {
            let pipelineInfo = this.state.dataSource;
            if (response.data === 'remove') {
                pipelineInfo.splice(pipelineInfo.findIndex((value) => {
                    return value.id === id
                }), 1);
                self.setState({
                    dataSource: pipelineInfo,
                    visible: false
                });
                toast.show({
                    type: "success",
                    content: "删除成功",
                    duration: 1000
                });
            }
        })
    }

    deletePipeline(pipelineInfo) {
        let url = API.pipeline + '/pipeline/jenkins/deleteJob';
        let self = this;
        self.setState({
            visible: true
        });
        Axios({
            method: 'post',
            url: url,
            data: pipelineInfo
        }).then((response) => {
            if(response.status === 200){
                self.removeByIdSQL(pipelineInfo.id);
            }else{
                toast.show({
                    type: "error",
                    content: "删除失败",
                    duration: 1000
                });
            }
        });
    }

    /**
     *  表格 序号栏配置
     * */
    renderIndex(value, index, record) {
        return index + 1;
    };

    /**
     *  表格 创建时间
     * */
    renderCreateTime(value, index, record) {
        return timetrans(value)
    }

    /**
     *  表格 操作栏配置
     * */
    renderOperation(value, index, record) {
        let router = "/pipeline/project/" + record.id;
        return (
            <div>
                <Link to={router}>
                    <Button type="primary" size="small">查看</Button>
                </Link>
                <Button
                    type="primary"
                    shape="warning"
                    size="small"
                    style={styles.button}
                    onClick={this.deletePipeline.bind(this, record)}
                >删除</Button>
            </div>
        );
    };

    render() {
        const columns = [{
            title: '序号',
            width: 5,
            dataIndex: 'index',
            cell: this.renderIndex
        }, {
            title: '流水线名称',
            width: 10,
            dataIndex: 'name',
        }, {
            title: '创建时间',
            width: 10,
            dataIndex: 'createTime',
            cell: this.renderCreateTime
        }, {
            title: '运行状态',
            width: 20,
            dataIndex: 'status',
        }, {
            title: '管理员',
            width: 8,
            dataIndex: 'admin',
        }, {
            title: '操作',
            width: 10,
            dataIndex: 'operation',
            cell: this.renderOperation.bind(this)
        }];
        return (
            <div>
                <Loading shape="fusion-reactor" visible={this.state.visible} className="next-loading my-loading">
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
                </Loading>
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
