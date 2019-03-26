/* eslint-disable react/no-unused-state, no-plusplus */
import React, { Component } from 'react';
import { Table, Switch, Icon, Button, Grid, Pagination } from '@icedesign/base';
import IceContainer from '@icedesign/container';
import CreateManualCaseDialog from "../CreateTestCases/CreateManualCaseDialog";

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
      createdCaseNeedRefresh: false
    };
  }

  formChange = (value) => {
    console.log('changed value', value);
    this.setState({
      formValue: value,
    });
  };

  getData = () => {
    const result = [];
    for (let i = 0; i < 10; i++) {
      result.push({
        id: i + 1,
        name: `李晓红${i + 1}`,
        university: '浙江大学',
        college: '计算机',
        class: i + 1,
        phone: `1876666123${i}`,
        role: '管理员',
      });
    }
    return result;
  };

  onChange = (...args) => {
    console.log(...args);
  };

  handlePaginationChange = (current) => {
    this.setState({
      current,
    });
  };

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

  refreshCaseList() {
    this.setState({
      createdCaseNeedRefresh: true
    });
    console.log("createdCaseNeedRefresh");
  }

  render() {
    return (
      <IceContainer title="用例列表">
        <Row wrap style={styles.headRow}>
          <Col l="12">
            {/*<Button type="primary" style={styles.button}>*/}
              {/*<Icon type="add" size="xs" style={{ marginRight: '4px' }} />添加手工测试用例*/}
            {/*</Button>*/}
            <CreateManualCaseDialog/>

            <Button type="primary" style={{ ...styles.button, marginLeft: 10}}>
              <Icon type="add" size="xs" style={{ marginRight: '4px' }} />添加接口测试用例
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
          dataSource={this.getData()}
          rowSelection={{ onChange: this.onChange }}
        >
          <Table.Column title="用例编号" dataIndex="id" width={100} />
          <Table.Column title="用例名称" dataIndex="name" width={100} />
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
