import React, { Component } from 'react';
import IceContainer from '@icedesign/container';
import {
  Grid,
  Step,
  Feedback, Icon
} from '@icedesign/base';

import RequestStageForm from "./RequestStageForm";
import API from "../../../../API";
import Axios from "axios";
import {Link, withRouter} from "react-router-dom";

const { Row, Col } = Grid;
const Toast = Feedback.toast;

class StepForm extends Component {
  static displayName = 'StepForm';

  static propTypes = {
  };

  static defaultProps = {
  };

  constructor(props) {
    super(props);
    const case_Id = this.props.caseId;
    this.state = {
      step: 0,
      caseId: case_Id,
      stages: this.props.stages,
    };
    console.log(this.props.stages);
    this.nextStep.bind(this);
  }

  componentWillReceiveProps(nextProps, nextContext) {
    console.log(nextProps);
    /*if (nextProps.caseId !== this.props.caseId) {
      this.setState({
        stages: nextProps.stages
      });
    }*/
    this.setState({
      step: 0,
      caseId: this.state.caseId,
      stages: nextProps.stages
    }, this.nextStep);
  }

  nextStep = (stage) => {
    console.log("Next Step!");
    let newStep = this.state.step + 1;
    this.setState({
      step: newStep,
      stages: this.props.stages
    });
  };

  lastStep = (stage) => {
    let newStep = this.state.step - 1;
    this.setState({
      step: newStep,
      stages: this.props.stages
    });
  };

  postToServer = (stage) => {
    if (this.state.stages.length < 3) {
      this.state.stages.push(stage);
    }
    let url = API.test + '/interfaceCases/stages';
    let _this = this;
    let stages = this.state.stages;
    Axios.post(url, stages, {
      headers: {
        'Content-Type': 'application/json; charset=utf-8'
      }
    })
      .then(function (response) {
        Toast.success("添加测试脚本成功！");
        _this.props.history.push('/testCases');
      }).catch(function (error) {
        console.log(error);
        Toast.error(error);
      });
  };

  renderStep = (step) => {
    if (step === 0) {
      return <RequestStageForm data={this.state.stages[step]} onSubmit={this.nextStep.bind(this)} onLast={this.lastStep.bind(this)}
                               stage='PREPARE' caseId={this.state.caseId} isSubmit={false}/>;
    }

    if (step === 1) {
      return <RequestStageForm data={this.state.stages[step]} onSubmit={this.nextStep.bind(this)} onLast={this.lastStep.bind(this)}
                               stage='TEST' caseId={this.state.caseId} isSubmit={false}/>;
    }

    if (step === 2) {
      return <RequestStageForm data={this.state.stages[step]} onSubmit={this.postToServer.bind(this)}  onLast={this.lastStep.bind(this)}
                               stage='DESTROY' caseId={this.state.caseId} isSubmit={false}/>;
    }
  };

  render() {
    return (
      <div className="step-form">
        <IceContainer>
          <Row wrap>
            <Col xxs="24" s="5" l="5" style={styles.formLabel}>
              <Step
                current={this.state.step}
                direction="vertical"
                type="dot"
                animation={false}
                style={styles.step}
              >
                <Step.Item title="准备" content="" />
                <Step.Item title="测试" content="" />
                <Step.Item title="测试后" content="" />
              </Step>
            </Col>

            <Col xxs="24" s="18" l="18">
              {this.renderStep(this.state.step)}
            </Col>
          </Row>
        </IceContainer>
      </div>
    );
  }
}

const styles = {
  container: {
    paddingBottom: 0,
  },
  step: {
    marginBottom: '20px',
  },
  content: {
    height: '200px',
    justifyContent: 'center',
    alignItems: 'center',
    display: 'flex',
  },
  icon: {
    color: '#1DC11D',
    marginRight: '10px',
  },
};

export default withRouter(StepForm);
