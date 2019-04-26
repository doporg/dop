import React, { Component } from 'react';
import IceContainer from '@icedesign/container';
import {
  Input,
  Button,
  Checkbox,
  Select,
  DatePicker,
  Switch,
  Radio,
  Grid, Feedback,
} from '@icedesign/base';
import Axios from "axios";
import {Link, withRouter} from "react-router-dom";
import API from "../../../../API";
import {FormBinder, FormBinderWrapper, FormError} from '@icedesign/form-binder';

const { Row, Col } = Grid;

// FormBinder 用于获取表单组件的数据，通过标准受控 API value 和 onChange 来双向操作数据
const CheckboxGroup = Checkbox.Group;
const RadioGroup = Radio.Group;
const { RangePicker } = DatePicker;

// Switch 组件的选中等 props 是 checked 不符合表单规范的 value 在此做转换
const SwitchForForm = (props) => {
  const checked = props.checked === undefined ? props.value : props.checked;

  return (
    <Switch
      {...props}
      checked={checked}
      onChange={(currentChecked) => {
        if (props.onChange) props.onChange(currentChecked);
      }}
    />
  );
};

const Toast = Feedback.toast;

class CreateActivityForm extends Component {
  static displayName = 'CreateActivityForm';

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.state = {
      value: {
        caseName: '',
        caseDesc: '',
        preCondition: '',
        applicationId: '',
        steps: '',
        predicateResult: '',
        status: 'NEW'
      },
    };
  }

  onFormChange = (value) => {
    this.setState({
      value,
    });
  };

  reset = () => {
    this.setState({
      value: {
        caseName: '',
        caseDesc: '',
        preCondition: '',
        applicationId: '',
        steps: '',
        predicateResult: '',
        status: 'NEW'
      },
    });
  };

  back = () => {
    this.props.history.push('/testCases');
  };

  submit = () => {
    let _this = this;
    let noError = true;

    this.refs.form.validateAll((error, value) => {
      if (error != null) {
        noError = false;
      }
    });

    if (noError) {
      let url = API.test + '/interfaceCases';
      Axios.post(url, this.state.value)
          .then(function (response) {
            Toast.success("添加接口测试用例成功！");
            let route = '/test/createInterfaceScripts/' + response.data;
            _this.props.history.push(route);
          }).catch(function (error) {
        console.log(error);
        Toast.error("创建用例失败!\n请确保相同应用下的测试用例名称是唯一的");
      });
    }
  };

  render() {
    return (
      <div className="create-activity-form">
        <IceContainer title="创建接口测试用例" style={styles.container}>
            <FormBinderWrapper
                value={this.state.value}
                ref="form"
            >
            <div>
              <Row style={styles.formItem}>
                <Col xxs="6" s="2" l="2" style={styles.formLabel}>
                  用例名称：
                </Col>

                <Col s="12" l="10">
                  <FormBinder
                    name="caseName"
                    required
                    message="用例名称必须填写"
                  >
                    <Input style={{ width: '100%' }} />
                  </FormBinder>
                  <FormError name="caseName" />
                </Col>
              </Row>

              <Row style={styles.formItem}>
                <Col xxs="6" s="2" l="2" style={styles.formLabel}>
                  应用ID：
                </Col>

                <Col s="12" l="10">
                  <FormBinder
                      name="applicationId"
                      required
                      message="必须输入关联的应用ID"
                  >
                    <Input style={{ width: '100%' }} />
                  </FormBinder>
                  <FormError name="applicationId" />
                </Col>
              </Row>

              <Row>
                <Col xxs="6" s="2" l="2" style={styles.formLabel}>
                  用例描述：
                </Col>
                <Col s="12" l="10">
                  <FormBinder name="caseDesc" required message="用例描述必须填写">
                    <Input multiple style={{ width: '100%' }} />
                  </FormBinder>
                  <FormError name="caseDesc" />
                </Col>
              </Row>

              <Row>
                <Col xxs="6" s="2" l="2" style={styles.formLabel}>
                  前置条件：
                </Col>
                <Col s="12" l="10">
                  <FormBinder name="preCondition">
                    <Input multiple style={{ width: '100%' }} placeholder="执行当前用例的测试步骤的必备前提条件，如部署被测服务系统" />
                  </FormBinder>
                </Col>
              </Row>

              <Row>
                <Col xxs="6" s="2" l="2" style={styles.formLabel}>
                  测试步骤：
                </Col>
                <Col s="12" l="10">
                  <FormBinder name="steps">
                    <Input multiple style={{ width: '100%' }} />
                  </FormBinder>
                </Col>
              </Row>

              <Row>
                <Col xxs="6" s="2" l="2" style={styles.formLabel}>
                  预期结果：
                </Col>
                <Col s="12" l="10">
                  <FormBinder name="predicateResult">
                    <Input multiple style={{ width: '100%' }} />
                  </FormBinder>
                </Col>
              </Row>

              <Row style={styles.btns}>
                <Col xxs="6" s="2" l="2" style={styles.formLabel}>
                  {' '}
                </Col>
                <Col s="12" l="10">
                  <Button style={{marginRight: '20px'}} onClick={this.back}>
                    取消
                  </Button>

                  <Button onClick={this.submit} type='secondary'>
                    创建并编写脚本
                    {/* <Link to='/test/createInterfaceScripts'>
                    </Link>*/}
                  </Button>
                  <Button style={styles.resetBtn} onClick={this.reset}>
                    重置
                  </Button>
                </Col>
              </Row>
            </div>
            </FormBinderWrapper>
        </IceContainer>
      </div>
    );
  }
}

const styles = {
  container: {
    paddingBottom: 0,
  },
  formItem: {
    height: '28px',
    lineHeight: '28px',
    marginBottom: '25px',
  },
  formLabel: {
    textAlign: 'right',
  },
  btns: {
    margin: '25px 0',
  },
  resetBtn: {
    marginLeft: '20px',
  },
};

export default withRouter(CreateActivityForm);
