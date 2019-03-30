/* eslint-disable react/no-unused-state */
import React, { Component } from 'react';
import { Input, Select, Grid } from '@icedesign/base';
import { FormBinderWrapper, FormBinder } from '@icedesign/form-binder';
import IceContainer from '@icedesign/container';

const { Row, Col } = Grid;

export default class UserTable extends Component {
  static displayName = 'UserTable';

  static propTypes = {};

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.state = {
      formValue: {},
    };
  }

  formChange = (value) => {
    console.log('changed value', value);
    this.setState({
      formValue: value,
    });
  };

  render() {
    const { formValue } = this.state;

    return (
      <IceContainer title="搜索">
        <FormBinderWrapper value={formValue} onChange={this.formChange}>
          <Row wrap>
            <Col xxs="24" l="8" style={styles.formCol}>
              <span style={styles.label}>用例归属:</span>
              <FormBinder name="university">
                <Select placeholder="请选择" style={{ width: '200px' }}>
                  <Select.Option value="mine">我的用例</Select.Option>
                  <Select.Option value="all">所有用例</Select.Option>
                </Select>
              </FormBinder>
            </Col>

            <Col xxs="24" l="8" style={styles.formCol}>
              <span style={styles.label}>类型:</span>
              <FormBinder name="college">
                <Select placeholder="请选择" style={{ width: '200px' }}>
                  <Select.Option value="manual">手工测试</Select.Option>
                  <Select.Option value="interface">接口测试</Select.Option>
                  <Select.Option value="all">所有</Select.Option>
                </Select>
              </FormBinder>
            </Col>

            <Col xxs="24" l="8" style={styles.formCol}>
              <span style={styles.label}>所属分组:</span>
              <FormBinder name="college">
                <Select placeholder="请选择" style={{ width: '200px' }}>
                  <Select.Option value="success">分组1</Select.Option>
                  <Select.Option value="fail">分组2</Select.Option>
                  <Select.Option value="block">分组3</Select.Option>
                  <Select.Option value="all">所有</Select.Option>
                </Select>
              </FormBinder>
            </Col>

            <Col xxs="24" l="8" style={styles.formCol}>
              <span style={styles.label}>执行结果:</span>
              <FormBinder name="college">
                <Select placeholder="请选择" style={{ width: '200px' }}>
                  <Select.Option value="success">成功</Select.Option>
                  <Select.Option value="fail">失败</Select.Option>
                  <Select.Option value="block">阻塞</Select.Option>
                  <Select.Option value="all">所有</Select.Option>
                </Select>
              </FormBinder>
            </Col>

            <Col xxs="24" l="8" style={styles.formCol}>
              <span style={styles.label}>创建者:</span>
              <FormBinder name="name">
                <Input />
              </FormBinder>
            </Col>

            {/*<Col xxs="24" l="8" style={styles.formCol}>*/}
              {/*<span style={styles.label}>角色:</span>*/}
              {/*<FormBinder name="role">*/}
                {/*<Input />*/}
              {/*</FormBinder>*/}
            {/*</Col>*/}
          </Row>
        </FormBinderWrapper>
      </IceContainer>
    );
  }
}

const styles = {

};
