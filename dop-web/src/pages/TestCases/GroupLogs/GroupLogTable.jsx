import React, { Component } from 'react';
import {Table, Pagination, Icon, Input, Button} from '@icedesign/base';
import Axios from "axios";
import Balloon from "@alifd/next/lib/balloon";
import Dialog from "@alifd/next/lib/dialog";
import {Col, Row} from "@alifd/next/lib/grid";
import IcePanel from '@icedesign/panel';
import API from "../../API";
import {withRouter} from "react-router-dom";
import IceContainer from '@icedesign/container';


class GroupLogTable extends Component {
    static displayName = 'GroupLogTable';

    constructor(props) {
        super(props);
        this.state = {
            current: 1,
            currentData: [],
            total: 0,
            groupId: this.props.match.params.groupId,
            showDetailLog: false,
            detailLogData: [],
            caseGroupDto: {
                caseUnits: []
            }
        };

        this.handlePagination = this.handlePagination.bind(this);
        this.refreshList(1);
    }

    getGroupInfo(){
        let url = API.test + "/group/" + this.state.groupId;
        let _this = this;
        Axios.get(url).then(function (response) {
            _this.setState({
                caseGroupDto: response.data
            });
        }).catch(function (error) {
            console.log(error);
        });
    }

    refreshList(current) {
        if (!current) {
            current = 1;
        }
        let url = API.test + '/group/logs/page/' + this.state.groupId;
        let _this = this;
        Axios.get(url, {
            params: {
                pageSize: 15,
                pageNo: current
            }
        }).then(function (response) {
            _this.setState({
                current: current,
                total: response.data.totalCount,
                currentData: response.data.pageList,
            }, _this.getGroupInfo);
        }).catch(function (error) {
            console.log(error);
        });
    }

    handlePagination = (current) => {
        this.refreshList(current);
    };

    handleSort = (dataIndex, order) => {
        const dataSource = this.state.currentData.sort((a, b) => {
            const result = a[dataIndex] - b[dataIndex];
            if (order === 'asc') {
                return result > 0 ? 1 : -1;
            }
            return result > 0 ? -1 : 1;
        });

        this.setState({
            dataSource,
        });
    };

    renderState = (value, index, record) => {
        if (value) {
            return (
                <div style={styles.state}>
                    <span style={styles.circle} />
                    <span style={styles.stateText}>成功</span>
                </div>
            );
        }else {
            return (
                <div style={styles.state}>
                    <span style={styles.circleFail} />
                    <span style={styles.stateTextFail}>失败</span>
                </div>
            )
        }
    };

    renderTime = (value, index, record) => {
        return value.replace('T', ' ');
    };

    renderRun = (value, index, record) => {
        let totalUnits = this.state.caseGroupDto.caseUnits.length;
        return value.length + '/' + totalUnits;
    };


    renderOper = (value, index, record) => {
        let MoveTarget = <Icon
            type="search"
            size="small"
            style={styles.icon}
            onClick={() => {
                this.props.history.push('/test/groupLogs/detail/' + record.id + "/" + this.state.groupId);
            }
            }
        />;
        return (
            <Balloon.Tooltip trigger={MoveTarget} triggerType="hover" align='l'>
                用例详细执行日志
            </Balloon.Tooltip>
        );
    };


    onOk = () => {

    };

    onClose = () => {
        this.setState({
            showDetailLog: false
        })
    };

    renderOperationLog = () => {

    };

    panel = (type) => {
        if (type === 'REQUEST') {
            return 'primary';
        }
        if (type === 'WAIT') {
            return 'danger';
        }
    };

    renderHeader = (operationLog) => {
        let stage = '准备阶段';
        let stageEnum = operationLog.stage;
        if (stageEnum === 'TEST') {
            stage = '测试阶段';
        }
        if (stageEnum === 'DESTROY') {
            stage = '测试后阶段';
        }

        let begin = operationLog.begin.replace('T', ' ');
        let end = operationLog.end.replace('T', ' ');
        return stage + ' ' + operationLog.operationType + ' ' + begin + ' to ' + end;
    };

    render() {
        return (
            <div style={styles.tableContainer}>
                <IceContainer title={'【' + this.state.caseGroupDto.groupName + '】的执行日志'}>
                    {this.state.caseGroupDto.comment}
                </IceContainer>

                <hr/>

                <Table
                    dataSource={this.state.currentData}
                    onSort={this.handleSort}
                    hasBorder={false}
                    className="custom-table"
                >
                    <Table.Column
                        width={100}
                        lock="left"
                        title="序列号"
                        dataIndex="id"
                        sortable
                        align="center"
                    />
                    <Table.Column width={200} title="开始执行时间" dataIndex="ctime" cell={this.renderTime}/>
                    <Table.Column
                        width={200}
                        title="测试负责人"
                        dataIndex="createUserName"
                    />
                    <Table.Column
                        width={100}
                        title="状态"
                        dataIndex="success"
                        cell={this.renderState}
                    />
                    <Table.Column
                        width={100}
                        title="已执行用例"
                        dataIndex='logs'
                        cell={this.renderRun}
                    />
                    <Table.Column
                        width={100}
                        title="所有用例日志"
                        cell={this.renderOper}
                        lock="right"
                        align="center"
                    />
                </Table>
                <Pagination
                    style={styles.pagination}
                    current={this.state.current}
                    onChange={this.handlePagination}
                    total={this.state.total}
                />

                <Dialog title="用例执行过程"
                        visible={this.state.showDetailLog}
                        isFullScreen
                        style={{width: '800px'}}
                        onOk={this.onClose}
                        onCancel={this.onClose}
                        onClose={this.onClose}>

                    {this.state.detailLogData.map((operationLog, index) => {
                        return (
                            <IcePanel status={this.panel(operationLog.operationType)} style={{marginBottom: '10px'}}>
                                <IcePanel.Header>
                                    <Row>
                                        <Col span='23'>
                                            {this.renderHeader(operationLog)}
                                        </Col>
                                        <Col span='1'>
                                        </Col>
                                    </Row>
                                </IcePanel.Header>
                                <IcePanel.Body>
                                    <Row>
                                        <Col span="22" style={{whiteSpace: 'pre-line'}}>
                                            {operationLog.executeInfo}
                                        </Col>
                                        <Col span="2">

                                        </Col>
                                    </Row>
                                </IcePanel.Body>
                            </IcePanel>
                        );
                    })}

                </Dialog>
            </div>
        );
    }
}

const styles = {
    icon: {
        color: '#2c72ee',
        cursor: 'pointer',
    },
    tableContainer: {
        background: '#fff',
        paddingBottom: '10px',
    },
    pagination: {
        margin: '20px 0',
        textAlign: 'center',
    },
    editIcon: {
        color: '#999',
        cursor: 'pointer',
    },
    circle: {
        display: 'inline-block',
        background: '#28a745',
        width: '8px',
        height: '8px',
        borderRadius: '50px',
        marginRight: '4px',
    },
    stateText: {
        color: '#28a745',
    },
    circleFail: {
        display: 'inline-block',
        background: '#e72b00',
        width: '8px',
        height: '8px',
        borderRadius: '50px',
        marginRight: '4px',
    },
    stateTextFail: {
        color: '#e72b00',
    },
};

export default withRouter(GroupLogTable);
