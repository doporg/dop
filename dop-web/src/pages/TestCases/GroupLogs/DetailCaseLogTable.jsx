import React, { Component } from 'react';
import {Table, Pagination, Icon, Input, Button} from '@icedesign/base';
import Axios from "axios";
import Balloon from "@alifd/next/lib/balloon";
import Dialog from "@alifd/next/lib/dialog";
import {Col, Row} from "@alifd/next/lib/grid";
import IcePanel from '@icedesign/panel';
import IceContainer from '@icedesign/container';
import {withRouter} from "react-router-dom";
import API from "../../API";


class DetailCaseLogTable extends Component {
    static displayName = 'DetailCaseLogTable';

    constructor(props) {
        super(props);
        this.state = {
            current: 1,
            currentData: [],
            showDetailLog: false,
            detailLogData: [],
            groupLogId: this.props.match.params.groupLogId,
            groupId: this.props.match.params.groupId
        };

        this.getDetailData();
    }

    getDetailData() {
        let url = API.test + "/group/logs/" + this.state.groupLogId;
        let _this = this;
        Axios.get(url).then(function (response) {
            _this.setState({
                currentData: response.data.logs
            });
        }).catch(function (error) {
            console.log(error);
        });
    }

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

    renderType = (value, index, record) => {
        if (value === 'INTERFACE') {
            return '接口用例';
        }else {
            return '手工用例';
        }
    };

    renderOper = (value, index, record) => {
        let MoveTarget = <Icon
            type="search"
            size="small"
            style={styles.icon}
            onClick={() => {
                this.setState({
                    showDetailLog: true,
                    detailLogData: record.operationExecuteLogs
                })
            }
            }
        />;

        let info = <Icon
            type="edit"
            size="small"
            style={{...styles.icon, marginRight: '10px'}}
            onClick={() => {
                this.props.history.push('/test/editCases/' + record.caseId);
            }}
        />;

        return (
            <div>
                <Balloon.Tooltip trigger={info} triggerType="hover" align='l'>
                    查看用例详情
                </Balloon.Tooltip>

                <Balloon.Tooltip trigger={MoveTarget} triggerType="hover" align='lt'>
                    查看详细日志
                </Balloon.Tooltip>
            </div>
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

    onBack() {
        this.props.history.push('/test/groupLogs/' + this.state.groupId);
    }

    render() {
        return (
            <div style={styles.tableContainer}>
                <IceContainer>
                    <Button style={styles.button} onClick={this.onBack.bind(this)} >
                        <Icon type="arrow-left" size="xs" style={{ marginRight: '4px' }} />返回上一页
                    </Button>
                </IceContainer>

                <Table
                    dataSource={this.state.currentData}
                    // onSort={this.handleSort}
                    hasBorder={false}
                    className="custom-table"
                >
                    <Table.Column
                        width={100}
                        lock="left"
                        title="用例类型"
                        dataIndex="caseType"
                        align="center"
                        cell={this.renderType}
                    />
                    <Table.Column
                        width={200}
                        // lock="left"
                        title="用例名"
                        dataIndex="caseName"
                        align="center"
                    />
                    <Table.Column width={200} title="开始执行时间" dataIndex="begin"/>
                    <Table.Column width={200} title="结束执行时间" dataIndex="end"/>
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
                        width={200}
                        title="操作"
                        cell={this.renderOper}
                        lock="right"
                        align="center"
                    />
                </Table>

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

export default withRouter(DetailCaseLogTable);
