import React, { Component } from 'react';
import IceContainer from '@icedesign/container/lib/index';
import {
  FormBinder as IceFormBinder,
  FormBinder,
  FormBinderWrapper,
  FormError as IceFormError,
  FormError
} from "@icedesign/form-binder";
import {Button, Feedback, Grid, Input, Icon} from "@icedesign/base";
import {TestSteps} from "../../../components/CreateTestCases/TestStep";

const { Row, Col } = Grid;

const styles = {
  formItem: {
    marginBottom: '20px',
    display: 'flex',
    alignItems: 'center',
    color: '#999999'
  },
  formItemLabel: {
    width: '70px',
    mariginRight: '10px',
    display: 'inline-block',
    textAlign: 'right',
  },
  formItemError: {
    marginLeft: '10px',
  },
  preview: {
    border: '1px solid #eee',
    marginTop: 20,
    padding: 10
  },
  formCommonWidth: {
    width: "550px"
  },
  detailItem: {
    padding: '15px 0px',
    display: 'flex',
    borderTop: '1px solid #EEEFF3',
  },
  detailTitle: {
    marginRight: '30px',
    textAlign: 'right',
    width: '120px',
    color: '#999999',
  },
  detailBody: {
    flex: 1,
  },
  statusProcessing: {
    color: '#64D874',
  },
};

const Toast = Feedback.toast;

export default class DetailTable extends Component {
  static displayName = 'DetailTable';

  static propTypes = {};

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.state = {
      caseDto: this.props.caseDto
    };
  }

  componentWillReceiveProps(nextProps, nextContext) {
    this.setState({
      caseDto: nextProps.caseDto
    })
  }

  render() {
    return (
      <div className="detail-table">

            <div style={styles.content}>

              <div style={styles.formItem}>
                <span style={styles.formItemLabel}>用例名称：</span>
                <FormBinder name="caseName" required message="请输入正确的用例名称" >
                  <Input placeholder="case name" style={styles.formCommonWidth}/>
                </FormBinder>
                <FormError style={styles.formItemError} name="caseName" />
              </div>

              <div style={styles.formItem}>
                <span style={styles.formItemLabel}>应用ID：</span>
                <FormBinder name="applicationId" required format="number" message="请绑定用例对应的应用id" >
                  <Input placeholder="application id" style={styles.formCommonWidth}/>
                </FormBinder>
                <FormError style={styles.formItemError} name="application id" />
              </div>

              <div style={styles.formItem}>
                <span style={styles.formItemLabel}>用例描述：</span>
                <FormBinder name="caseDesc" required  message="用例描述必须填写" >
                  <Input multiple placeholder="用例描述" style={styles.formCommonWidth}/>
                </FormBinder>
                <FormError style={styles.formItemError} name="caseDesc" />
              </div>

              <div style={styles.formItem}>
                <span style={styles.formItemLabel}>前置条件：</span>
                <FormBinder name="preCondition" required  message="前置条件必须填写" >
                  <Input multiple placeholder="前置条件" style={styles.formCommonWidth}/>
                </FormBinder>
                <FormError style={styles.formItemError} name="preCondition" />
              </div>

              <div style={styles.formItem}>
                <span style={styles.formItemLabel}>测试步骤：</span>
                <FormBinder name="steps" required format="number" message="测试步骤必须填写" >
                  <Input multiple placeholder="测试步骤" style={styles.formCommonWidth}/>
                </FormBinder>
                <FormError style={styles.formItemError} name="steps" />
              </div>

              <div style={styles.formItem}>
                <span style={styles.formItemLabel}>预期结果：</span>
                <FormBinder name="predicateResult" required format="number" message="预期结果必须填写" >
                  <Input multiple placeholder="预期结果" style={styles.formCommonWidth}/>
                </FormBinder>
                <FormError style={styles.formItemError} name="predicateResult" />
              </div>
            </div>
      </div>
    );
  }
}

