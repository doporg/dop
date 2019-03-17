import React, {Component} from 'react';
import {Step, Icon} from '@icedesign/base';
import PipelineInfoStep from './PipelineInfoStep'

export default class PipelineInfoStage extends Component {
    constructor(props) {
        super(props)
        this.state = {
            currentStage: 0,
            stages:[]
        }
    }

    componentWillMount(){
        let self = this
        this.setState({
            currentStage: self.props.currentStage,
            stages: self.props.stages
        })
    }
    componentDidUpdate(prevProps, prevState) {
        if (prevState.stages !== this.state.stages) {
            console.log(11)
            this.props.onChange(this.state.stages)
        }
    }

    /**
     * stage数字显示
     * */
    stepItemRender(index) {
        return index + 1
    }
    stepItemAdd() {
        return (
            <Icon type="add"/>
        );
    }
    /**
     * 切换stage, 将新信息放到currentStage
     * */
    changeStage(currentStage) {
        this.setState({
            currentStage
        });
    }
    addStage() {
        let newStage = {
            name: "请输入名称",
            steps: []
        };
        this.state.stages.push(newStage);
        this.setState({
            currentStage: this.state.stages.length -1
        })
    }
    step(value){
        console.log(value)
        let stages = this.state.stages;
        stages[this.state.currentStage] = value;
        this.setState({
            stages
        })
    }

    render() {
        return (
            <div className="pipeline-info-stage-step">
                <Step className="pipeline-info-stage" type="circle" animation={true}
                      current={this.state.currentStage}
                >
                    {this.state.stages.map((stage, index)=>{
                        return (
                            <Step.Item key={index} title={stage.name}
                                       itemRender={this.stepItemRender}
                                       className="pipeline-info-stage-stepItem"
                                       onClick={this.changeStage.bind(this, index)}
                            />
                        )
                    })}
                    <Step.Item title="添加"
                               itemRender={this.stepItemAdd.bind(this)}
                        className="pipeline-info-stage-stepItem"
                        onClick={this.addStage.bind(this, this.state.stages.length)}
                    />
                </Step>

                <PipelineInfoStep
                    stage={this.state.stages[this.state.currentStage]}
                    onChange={this.step.bind(this)}
                />
            </div>
        )
    }
}
