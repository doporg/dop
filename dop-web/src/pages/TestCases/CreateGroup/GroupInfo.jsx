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
import {FormBinder, FormBinderWrapper, FormError} from '@icedesign/form-binder';
import API from "../../API";
import CaseUnit from "./CaseUnit";

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

class GroupInfo extends Component {
    static displayName = 'CreateGroup';

    static defaultProps = {};

    constructor(props) {
        super(props);
        this.state = {
            value: this.props.groupDto,
            operation: this.props.operation
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
                appId: '',
                groupName: '',
                comment: '',
                executeWay: 'PARALLEL',
                caseUnits: []
            },
        });
    };

    back = () => {
        this.props.history.push('/test/testGroups');
    };

    submit = () => {
        let _this = this;
        let noError = true;

        this.refs.form.validateAll((error, value) => {
            if (error != null) {
                noError = false;
            }
        });

        this.refs.ax.validateAll((error, value) => {
            if (error != null) {
                noError = false;
            }
        });

        if (noError) {
            let url = API.test + '/group';
            let caseUnits = this.state.value.caseUnits;
            let newUnits = [];
            caseUnits.forEach((unit) => {
                let newUnit = JSON.parse(unit);
                newUnits.push(newUnit);
            });
            let param = this.state.value;
            param.caseUnits = newUnits;
            if (this.state.operation === 'UPDATE'){
                Axios.put(url, param)
                    .then(function (response) {
                        Toast.success("编辑分组成功！");
                        _this.props.history.push('/test/testGroups');
                    }).catch(function (error) {
                    console.log(error);
                    Toast.error("编辑分组失败!请重新尝试！");
                });
            } else if(this.state.operation === 'INSERT'){
                Axios.post(url, param)
                    .then(function (response) {
                        Toast.success("添加分组成功！");
                        _this.props.history.push('/test/testGroups');
                    }).catch(function (error) {
                    console.log(error);
                    Toast.error("创建分组失败!请重新尝试！");
                });
            }
        }
    };

    addItem = () => {
        let newUnit = {
            "caseId": '',
            "caseType": 'MANUAL',
            "caseName": '请选择该应用下的测试用例'
        };
        this.state.value.caseUnits.push(JSON.stringify(newUnit));
        this.setState({ value: this.state.value });
    };

    removeItem = (index) => {
        this.state.value.caseUnits.splice(index, 1);
        this.setState({
            value: this.state.value
        });
    };

    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({
            value: nextProps.groupDto,
            operation: nextProps.operation
        })
    }

    render() {
        return (
            <div className="create-activity-form">
                <IceContainer title="创建分组" style={styles.container}>
                    <FormBinderWrapper
                        value={this.state.value}
                        ref="form"
                    >
                        <div>
                            <Row style={styles.formItem}>
                                <Col xxs="6" s="2" l="3" style={styles.formLabel}>
                                    分组名称：
                                </Col>

                                <Col s="12" l="7">
                                    <FormBinder
                                        name="groupName"
                                        required
                                        message="分组名称必须填写"
                                    >
                                        <Input style={{ width: '100%' }} />
                                    </FormBinder>
                                    <FormError name="groupName" />
                                </Col>
                            </Row>

                            <Row style={styles.formItem}>
                                <Col xxs="6" s="2" l="3" style={styles.formLabel}>
                                    应用ID：
                                </Col>

                                <Col s="12" l="7">
                                    <FormBinder
                                        name="appId"
                                        required
                                        message="必须输入关联的应用ID"
                                    >
                                        <Input style={{ width: '100%' }} />
                                    </FormBinder>
                                    <FormError name="appId" />
                                </Col>
                            </Row>

                            <Row>
                                <Col xxs="6" s="2" l="3" style={styles.formLabel}>
                                    分组描述：
                                </Col>
                                <Col s="12" l="7">
                                    <FormBinder name="comment" required message="分组描述必须填写">
                                        <Input multiple style={{ width: '100%' }} />
                                    </FormBinder>
                                    <FormError name="comment" />
                                </Col>
                            </Row>

                            <Row>
                                <Col xxs="6" s="2" l="3" style={styles.formLabel}>
                                    接口测试执行方式：
                                </Col>
                                <Col s="12" l="7">
                                    <FormBinder name="executeWay">
                                        <Select placeholder="请选择" style={{ width: '200px' }}>
                                            <Select.Option value="PARALLEL">并行</Select.Option>
                                            <Select.Option value="SERIAL">串行</Select.Option>
                                        </Select>
                                    </FormBinder>
                                </Col>
                            </Row>

                        </div>
                    </FormBinderWrapper>
                </IceContainer>

                <IceContainer title="添加用例" style={styles.container}>
                    <FormBinderWrapper
                        value={this.state.value}
                        ref="ax"
                    >

                    <CaseUnit caseUnits={this.state.value.caseUnits}
                              addItem={this.addItem.bind(this)} removeItem={this.removeItem.bind(this)}/>

                    </FormBinderWrapper>

                    <Row style={styles.btns}>
                        <Col xxs="6" s="2" l="2" style={styles.formLabel}>
                            {' '}
                        </Col>
                        <Col s="12" l="10">
                            <Button style={{marginRight: '20px'}} onClick={this.back}>
                                取消
                            </Button>

                            <Button onClick={this.submit} type='secondary'>
                                {this.state.operation === 'INSERT'? '创建' : '修改'}
                            </Button>
                            <Button style={styles.resetBtn} onClick={this.reset}>
                                重置
                            </Button>
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

export default withRouter(GroupInfo);
