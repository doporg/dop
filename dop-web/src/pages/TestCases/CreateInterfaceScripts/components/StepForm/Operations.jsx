import React, { Component } from 'react';
import {
    Input,
    Button,
    Grid,
    Icon,
    Upload
} from '@icedesign/base';
import IcePanel from '@icedesign/panel';

import {  FormBinder, FormError } from '@icedesign/form-binder';
import RequestScriptForm from "./RequestScriptForm";
import WaitOperation from "./WaitOperation";
import API from "../../../../API";

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
            operations: this.props.operations,
            caseParams: []
        };
    }

    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({
            requestScripts: nextProps.requestScripts,
            waitOperations: nextProps.waitOperations,
            operations: nextProps.operations,
            caseParams: nextProps.caseParams
        })
    }

    beforeUpload(info) {
        // console.log("beforeUpload callback : ", info);
    }

    onChange(info) {
        // console.log("onChane callback : ", info);
    }

    uploadSuccess(res, file) {
        // console.log("I am success!");
        // console.log(res);
        // console.log(file);
    };

    onError = (file) => {
        let data = file.response;
        if (data) {
            let case_id = this.props.caseId;
            let case_params = data.caseParams;
            if (case_params) {
                case_params.forEach((param) => {
                    param.caseId = case_id;
                    this.props.addCaseParam(param);
                    // this.props.caseParams.push(param);
                });
            }

            let scripts = data.requestScripts;
            if (scripts) {
                scripts.forEach((script) => {
                    this.props.addRequestScriptWithContent(script);
                })
            }
        }
    };

    render() {
        let importApi = API.test + '/interfaceCases/import';
        return (
            <div>
                <Row>
                    <Col span="15"/>
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
                    <Col span='3'>
                        <Upload
                            listType="text"
                            action={importApi}
                            accept=".json"
                            beforeUpload={this.beforeUpload}
                            onChange={this.onChange}
                            showUploadList={false}
                            onSuccess={this.uploadSuccess}
                            onError={this.onError}
                        >
                            <Button type="primary" style={{ margin: "0 0 10px" }}>
                                导入API
                            </Button>
                        </Upload>
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
