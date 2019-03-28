import React, { Component } from 'react';
import IceContainer from '@icedesign/container';
import {
  Grid,
  Step,
} from '@icedesign/base';

import ItemForm from './ItemForm';
import DeliveryForm from './DeliveryForm';
import RequestStageForm from "./RequestStageForm";

const { Row, Col } = Grid;

export default class StepForm extends Component {
  static displayName = 'StepForm';

  static propTypes = {
  };

  static defaultProps = {
  };

  constructor(props) {
    super(props);
    this.state = {
      step: 0,
      stages: [],
    };
  }

  nextStep = (stage) => {
    console.log("I got a stage!");
    console.log(stage);
    this.state.stages.push(stage);
    let newStep = this.state.step + 1;
    this.setState({
      step: newStep,
      stages: this.state.stages
    });
  };

  addStage = (stage) => {
    this.state.stages.push(stage);
  };

  renderStep = (step) => {
    if (step === 0) {
      return <RequestStageForm onSubmit={this.nextStep} stage='PREPARE'/>;
    }

    if (step === 1) {
      return <RequestStageForm onSubmit={this.nextStep} stage='TEST'/>;
    }

    if (step === 2) {
      return <RequestStageForm onSubmit={this.nextStep} stage='DESTROY'/>;
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
                <Step.Item title="准备" content="测试之前的准备" />
                <Step.Item title="测试" content="正式开始测试" />
                <Step.Item title="测试后" content="测试后的操作" />
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
