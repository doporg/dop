/* eslint-disable react/no-unused-state, no-plusplus */
import React, { Component } from 'react';
import {Table, Switch, Icon, Button, Grid, Pagination, Dialog} from '@icedesign/base';
import IceContainer from '@icedesign/container';
import CreateManualCaseFrom from "../CreateTestCases/CreateManualCaseForm";
import API from "../../../API";
import Axios from "axios";
import {Link} from "react-router-dom";

const { Row, Col } = Grid;

export default class CustomTable extends Component {
  static displayName = 'CustomTable';

  static propTypes = {};

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.state = {
      formValue: {},
      current: 1,
      createdCaseNeedRefresh: false,
      createManualDialogVisiable: false,
      isSubmit: false,
      total: 1,
      currentData: [{"id": "1"}]
    };

    this.handlePaginationChange = this.handlePaginationChange.bind(this);
    this.refreshList(1);
  }

  formChange = (value) => {
    console.log('changed value', value);
    this.setState({
      formValue: value,
    });
  };

  onChange = (...args) => {
    console.log(...args);
  };

  handlePaginationChange = (current) => {
    this.refreshList(current);
  };

  refreshList(current) {
    let url = API.test + '/manualCases/page';
    let _this = this;
    Axios.get(url, {
      params: {
        pageSize: 10,
        pageNo: current
      }
    }).then(function (response) {
      console.log(response);
      _this.setState({
        current: current,
        total: response.data.totalCount,
        currentData: response.data.pageList
      });
    }).catch(function (error) {
      console.log(error);
    });
  }

  renderOper = () => {
    return (
      <div style={styles.oper}>
        <Icon
          type="edit"
          size="small"
          style={{ ...styles.icon, ...styles.editIcon }}
        />
        <Icon
          type="ashbin"
          size="small"
          style={{ ...styles.icon, ...styles.deleteIcon }}
        />
      </div>
    );
  };

  onOpen = () =>{
    this.setState({
      createManualDialogVisiable: true
    })
  };

  onClose = () =>{
    this.setState({
      createManualDialogVisiable: false,
      isSubmit: false
    })
  };

  onOk = ()=>{
    this.setState({
      isSubmit: true
    })
  };

  render() {
    return (
      <IceContainer title="用例列表">
        <Row wrap style={styles.headRow}>
          <Col l="12">
            <Button type="primary" style={styles.button} onClick={this.onOpen.bind(this)} >
              <Icon type="add" size="xs" style={{ marginRight: '4px' }} />添加手工测试用例
            </Button>
            <Dialog
                visible={this.state.createManualDialogVisiable}
                onOk={this.onOk.bind(this)}
                onCancel={this.onClose.bind(this)}
                onClose={this.onClose.bind(this)}
                title="创建手工测试用例"
                isFullScreen
            >
              <CreateManualCaseFrom
                  isSubmit={this.state.isSubmit}
              />
            </Dialog>

            <Button style={{ ...styles.button, marginLeft: 10}}>
              <Link to="/test/createManualCase">
                <Icon type="add" size="xs" style={{ marginRight: '4px' }} />添加接口测试用例
              </Link>
            </Button>
          </Col>
          <Col l="12" style={styles.center}>
            <Button type="normal" style={styles.button}>
              删除
            </Button>
            <Button type="normal" style={{ ...styles.button, marginLeft: 10 }}>
              导入
            </Button>
            <Button type="normal" style={{ ...styles.button, marginLeft: 10 }}>
              修改
            </Button>
          </Col>
        </Row>

        <Table
          dataSource={this.state.currentData}
          rowSelection={{ onChange: this.onChange }}
        >
          <Table.Column title="用例编号" dataIndex="id" width={100} />
          <Table.Column title="用例名称" dataIndex="caseName" width={100} />
          <Table.Column title="类型" dataIndex="type" width={100} />
          <Table.Column title="状态" dataIndex="status" width={100} />
          <Table.Column title="执行结果" dataIndex="executeResult" width={100} />
          <Table.Column title="创建者" dataIndex="cuser" width={100} />
          <Table.Column title="执行/终止" width={100} cell={() => <Switch />} />
          <Table.Column title="操作" width={100} cell={this.renderOper} />
        </Table>
        <Pagination
          style={styles.pagination}
          current={this.state.current}
          onChange={this.handlePaginationChange}
          total={this.state.total}
        />
      </IceContainer>
    );
  }
}

const styles = {
  headRow: {
    marginBottom: '10px',
  },
  icon: {
    color: '#2c72ee',
    cursor: 'pointer',
  },
  deleteIcon: {
    marginLeft: '20px',
  },
  center: {
    textAlign: 'right',
  },
  button: {
    borderRadius: '4px',
  },
  pagination: {
    marginTop: '20px',
    textAlign: 'right',
  },
};
