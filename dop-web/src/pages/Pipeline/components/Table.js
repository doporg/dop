import React, {Component} from 'react';
import {Table, Button, Loading, Feedback} from '@icedesign/base';
import {Link} from 'react-router-dom';
import API from "../../API";
import Axios from "axios/index";
import './Styles.scss'

const {toast} = Feedback;

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
        let url = API.pipeline + '/v1/pipelines';
        let self = this;
        Axios.get(url).then((response) => {
            let dataSource = []
            let data = response.data.sort();
            for (let i = 0; i < data.length; i++) {
                if (!data[i].isDeleted) {
                    dataSource.push(data[i])
                }
            }
            self.setState({
                dataSource
            });
        })
    }

    removeByIdSQL(id) {
        let url = API.pipeline + '/v1/delete/byId?id=' + id;
        let self = this;
        Axios.put(url).then((response) => {
            let pipelineInfo = this.state.dataSource;
            if (response.status === 200) {
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
        let url = API.pipeline + '/v1/jenkins/byId?id=' + pipelineInfo.id;
        let self = this;
        self.setState({
            visible: true
        });
        Axios({
            method: 'delete',
            url: url,
        }).then((response) => {
            if (response.status === 200) {
                self.removeByIdSQL(pipelineInfo.id);
            } else {
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
    renderIndex(value, index) {
        return index + 1;
    };

    /**
     *  表格 创建时间
     * */
    renderCreateTime(value) {
        if (value) {
            return value[0] + "-" + value[1] + "-" + value[2] + "  " +
                (value[3] < 10 ? '0' + value[3] : value[3]) + ":" +
                (value[4] < 10 ? '0' + value[4] : value[4])
        }
    }

    /**
     *  表格 操作栏配置
     * */
    renderOperation(value, index, record) {
        let router = "/pipeline/project/" + record.id;
        let edit = "/pipeline/edit/" + record.id;
        return (
            <div>
                <Link to={router}>
                    <Button type="primary" size="small">查看</Button>
                </Link>
                <Link to={edit}>
                    <Button
                        type="normal"
                        size="small"
                        className="button"
                    >编辑</Button>
                </Link>

                <Button
                    type="primary"
                    shape="warning"
                    size="small"
                    className="button"
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
            dataIndex: 'ctime',
            cell: this.renderCreateTime
        }, {
            title: '创建人',
            width: 8,
            dataIndex: 'cuser',
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
