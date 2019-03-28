import React, { Component } from 'react';
import {
    FormBinderWrapper,
    FormBinder,
    FormError,
} from '@icedesign/form-binder';
import {
    Input,
    Button,
    Checkbox,
    Grid,
} from '@icedesign/base';
import {TabPane} from "@icedesign/base/lib/tab";
import Tab from "@icedesign/base/lib/tab";
import RequestHeader from "./RequestHeader";
import RequestCheckPoint from "./RequestCheckPoint";
import {Option} from "@icedesign/base/lib/select";
import Select from "@icedesign/base/lib/select";
import Operations from "./Operations";

const { Row, Col } = Grid;


const styles = {
    btns: {
        marginTop: '25px',
        marginBottom: '25px',
    },
};


export default class RequestStageForm extends Component{

    constructor(props) {
        super(props);
        this.state = {
            isSubmit: false,
            value: {
                operations: [],
                stage: this.props.stage,
                requestScripts: [],
                waitOperations: []
            }
        };
    }

    addRequestScript = () => {
        let index = this.state.value.operations.length;
        this.state.value.operations.push({
            operationType: 'REQUEST',
            order: index
        });
        this.setState({isSubmit: false, value: this.state.value});
    };

    removeOperation = (index) => {
        this.state.value.operations.splice(index, 1);
        this.setState({
            value: this.state.value
        });
    };

    addWaitTime = () => {
        let index = this.state.value.operations.length;
        this.state.value.operations.push({
            operationType: 'WAIT',
            order: index
        });
        this.setState({isSubmit: false, value: this.state.value});
    };

    submitRequest = (value) => {
        console.log(value);
        this.state.value.requestScripts.push(value);
    };

    submitWait = (value) =>{
        this.state.value.waitOperations.push(value);
    };

    submit = () => {
        this.setState({
            isSubmit: true,
            value: this.state.value
        }, this.props.onSubmit(this.state.value));
        console.log("Begin to add value into stages!");
        // this.setState({
        //     isSubmit: false,
        //     value: {
        //         operations: [],
        //         stage: this.props.stage,
        //         requestScripts: [],
        //         waitOperations: []
        //     }
        // });
    };

    render() {
        return(
            <div>
                <Operations
                    isSubmit={this.state.isSubmit}
                    operations={this.state.value.operations}
                    addRequestScript={this.addRequestScript.bind(this)}
                    addWaitTime={this.addWaitTime.bind(this)}
                    removeOperation={this.removeOperation.bind(this)}
                    submitRequest={this.submitRequest.bind(this)}
                    submitWait={this.submitWait.bind(this)}
                />

                <Row>
                    <Col offset={3} style={styles.btns}>
                        <Button onClick={this.submit.bind(this)} type="primary">
                            下一步
                        </Button>
                    </Col>
                </Row>
            </div>

        )
    }
}
