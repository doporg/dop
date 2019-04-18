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

const { Row, Col } = Grid;

export default class RequestScriptForm extends Component{

    constructor(props) {
        super(props);
        this.state = {
            // value: {
            //     rawUrl: '',
            //     httpMethod: 'GET',
            //     requestHeaders: [{}],
            //     requestBody: '',
            //     requestCheckPoints: [{}],
            //     retryTimes: '',
            //     retryInterval: '',
            //     resultParams: '',
            //     operationType: 'REQUEST',
            //     order: this.props.order
            // },
            value: this.props.currentScript
        };
    }

    validateFormAndPost = () => {
        let noError = true;
        this.refs.form.validateAll((errors, values) => {
            if (errors != null) {
                noError = false;
            }
        });
        if (noError) {
            this.doSubmit(this.state.value);
        }
        // this.props.cancel();
    };

    doSubmit = (content) => {
        // this.props.submitRequest(content);
    };

    addItem = () => {
        this.state.value.requestHeaders.push({
            name: '',
            value: ''
        });
        this.setState({ value: this.state.value });
    };

    removeItem = (index) => {
        this.state.value.requestHeaders.splice(index, 1);
        this.setState({
            value: this.state.value
        });
    };

    addCheckPoint = () => {
        this.state.value.requestCheckPoints.push({
            property: '',
            operation: '',
            value: ''
        });
        this.setState({ value: this.state.value });
    };

    removeCheckPoint = (index) => {
        this.state.value.requestCheckPoints.splice(index, 1);
        this.setState({
            value: this.state.value
        });
    };

    renderTab = (tab) => {
        if (tab === '请求头') {
            return <RequestHeader
                requestHeaders={this.state.value.requestHeaders}
                addItem={this.addItem.bind(this)}
                removeItem={this.removeItem.bind(this)}/>;
        }
        if (tab === '请求体') {
            return <Row>
                <Col>
                    <FormBinder name="requestBody" >
                        <Input multiple style={{width: '100%'}}/>
                    </FormBinder>
                </Col>
            </Row>;
        }
        if (tab === '检查点') {
            return <RequestCheckPoint
                requestCheckPoints={this.state.value.requestCheckPoints}
                addItem={this.addCheckPoint.bind(this)}
                removeItem={this.removeCheckPoint.bind(this)}/>;
        }
    };

    componentWillReceiveProps(nextProps, nextContext) {
        if (nextProps.currentScript !== this.props.currentScript) {
            this.setState({
                value: nextProps.currentScript
            })
        }
    }

    render() {
        const tabs = [
            { tab: "请求头", key: "header", content: "这里是首页内容" },
            { tab: "请求体", key: "body", content: "这里是文档内容" },
            { tab: "检查点", key: "checkPoint", content: "这里是 API 内容" }
        ];

        return (
            <div>
                <FormBinderWrapper
                    value={this.state.value}
                    ref="form">
                    <Row>
                        <Col span="4">
                            <FormBinder name="httpMethod" >
                                <Select placeholder="选择请求方法" style={{width: '100%'}}
                                >
                                    <Option value="GET">GET</Option>
                                    <Option value="POST">POST</Option>
                                    <Option value="PUT">PUT</Option>
                                    <Option value="DELETE">DELETE</Option>
                                </Select>
                            </FormBinder>
                        </Col>
                        <Col span="20">
                            <FormBinder name="rawUrl" >
                                <Input placeholder="请输入请求url" style={{width: '100%'}}/>
                            </FormBinder>
                        </Col>
                    </Row>
                </FormBinderWrapper>

                <Tab onChange={this.handleChange}>
                    {tabs.map(item => (
                        <TabPane key={item.key} tab={item.tab} onClick={this.handleClick}>
                            <FormBinderWrapper
                                value={this.state.value}
                                ref="form"
                            >
                                {this.renderTab(item.tab)}
                            </FormBinderWrapper>
                        </TabPane>
                    ))}
                </Tab>
            </div>
        );
    }



}
