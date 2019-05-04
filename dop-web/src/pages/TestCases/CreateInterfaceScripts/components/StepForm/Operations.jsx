import React, { Component } from 'react';
import {
    Input,
    Button,
    Grid,
    Icon
} from '@icedesign/base';
import IcePanel from '@icedesign/panel';

import {  FormBinder, FormError } from '@icedesign/form-binder';
import RequestScriptForm from "./RequestScriptForm";
import WaitOperation from "./WaitOperation";

const { Row, Col } = Grid;

const styles = {
    formItem: {
        marginBottom: '20px',
        display: 'flex',
        alignItems: 'center',
    },
    formItemLabel: {
        width: '70px',
        marginRight: '10px',
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
    }
};

export default class Operations extends Component{

    constructor(props) {
        super(props);
        this.state = {
            isSubmit: false,
            requestScripts: this.props.requestScripts,
            waitOperations: this.props.waitOperations,
            operations: this.props.operations
        }
    }

    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({
            requestScripts: nextProps.requestScripts,
            waitOperations: nextProps.waitOperations,
            operations: nextProps.operations
        })
    }

    render() {
        return (
            <div>
                <Row>
                    <Col span="18"/>
                    <Col span='3'>
                        <div style={styles.buttons}>
                            <Button type="secondary" onClick={this.props.addRequestScript.bind(this)}>添加HTTP请求</Button>
                        </div>
                    </Col>
                    <Col span='3'>
                        <div style={styles.buttons}>
                            <Button type="secondary" onClick={this.props.addWaitTime.bind(this)}>添加等待操作</Button>
                        </div>
                    </Col>
                </Row>
                <hr/>
                {this.state.operations.map((operation, index) => {
                    if (operation.operationType === 'REQUEST') {
                        return (
                            <div key={Math.random()}>
                                <IcePanel status='primary' style={{marginBottom: '10px'}}>
                                    <IcePanel.Header>
                                        <Row>
                                            <Col span='23'>
                                                HTTP请求
                                            </Col>
                                            <Col span='1'>
                                                {/*<Button type="text" size='large' shape='ghost' onClick={this.props.removeOperation.bind(this,index)} style={{marginHeight: '20px'}}>*/}
                                                     <Icon type="error" onClick={this.props.removeOperation.bind(this,index)}/>
                                                {/*</Button>*/}
                                            </Col>
                                        </Row>
                                    </IcePanel.Header>
                                    <IcePanel.Body>
                                        <Row>
                                            <Col span="22">
                                                <RequestScriptForm currentScript={this.props.requestScripts[index]} isSubmit={this.state.isSubmit} cancel={this.props.cancel} order={index}/>
                                            </Col>
                                            <Col span="2">

                                            </Col>
                                        </Row>
                                    </IcePanel.Body>
                                </IcePanel>
                            </div>
                    );
                    }else if (operation.operationType === 'WAIT') {
                        return(
                            <div key={Math.random()}>
                                <IcePanel status='danger' style={{marginBottom: '10px'}}>
                                    <IcePanel.Header>
                                        <Row>
                                            <Col span='23'>
                                                等待
                                            </Col>
                                            <Col span='1'>
                                                {/*<Button size='large' onClick={this.props.removeOperation.bind(this,index)}>*/}
                                                    <Icon type="error" onClick={this.props.removeOperation.bind(this,index)}/>
                                                {/*</Button>*/}
                                            </Col>
                                        </Row>
                                    </IcePanel.Header>
                                    <IcePanel.Body>
                                        <Row>
                                            <Col span="22">
                                                <WaitOperation currentOperation={this.props.waitOperations[index]} isSubmit={this.state.isSubmit} cancel={this.props.cancel} order={index}/>
                                            </Col>
                                            <Col span="2">

                                            </Col>
                                        </Row>
                                    </IcePanel.Body>
                                </IcePanel>
                            </div>
                        );
                    }
                })}
            </div>
        );
    }
}
