import React, {Component} from 'react';
import Axios from "axios";
import API from "../../../../API";
import {Dialog, Feedback, Field, Form, Table} from '@icedesign/base';
import {Grid} from '@icedesign/base';
import {Icon} from '@icedesign/base';
import {Row, Col} from "@alifd/next/lib/grid";
import CreateApplicationVariableDialog from "../CreateApplicationVariable";
import Input from "@icedesign/base/lib/input";

const FormItem = Form.Item;
const Toast = Feedback.toast;
/**
 * 应用变量管理页面
 * @author Bowen
 **/
export default class ApplicationVariable extends Component {
    static displayName = 'ApplicationVariable';

    constructor(props) {
        super(props);

        this.field = new Field(this);
        this.state = {
            appId: props.appId,
            varData: [],
            editMode: false,

        }
    }

//刷新变量列表
    refreshApplicationVariableList() {
        this.getApplicationVariableData();
    }

    //获取变量列表的值
    getApplicationVariableData() {
        let getUrl = API.gateway + "/application-server/application/" + this.state.appId + "/variable";
        let _this = this;
        Axios.get(getUrl)
            .then(function (response) {
                console
                _this.setState({
                    varData: response.data,
                    editMode: false
                })
            })

    }

    componentDidMount() {
        this.getApplicationVariableData();
    }

    deleteConfirm = (id) => {
        console.log(id)
        Dialog.confirm({
            content: "你确定要删除该变量吗？",
            title: "确认删除",
            onOk: this.onDelete.bind(this, id)
        });
    };

//删除按钮响应函数
    onDelete(id) {
        let deleteUrl = API.gateway + "/application-server/application/variable/" + id;
        let _this = this;
        Axios.delete(deleteUrl)
            .then(function (response) {
                Toast.success("删除成功")
                _this.refreshApplicationVariableList();
            })
    }

    submitConfirm = (id) => {
        console.log(id)
        Dialog.confirm({
            content: "你确定要提交修改吗？",
            title: "确认修改",
            onOk: this.onSubmit.bind(this, id)
        });
    };

//保存按钮响应函数
    onSubmit(id) {
        let putUrl = API.gateway + "/application-server/application/variable/" + id;
        let _this = this;
        Axios.put(putUrl, {
            varValue: _this.field.getValue('value')
        })
            .then(function (response) {
                Toast.success("修改成功")
                _this.refreshApplicationVariableList();
            })
    }

    //取消按钮响应函数
    onCancel() {
        this.setState({
            editMode: false
        })
    }

    //编辑图标响应函数
    onEdit() {
        this.setState({
            editMode: true
        })
    }


    render() {
        const {init} = this.field;
        const keyRender = (value, index, record) => {
            console.log(record, value);
            return <div>
                <div style={{float: "left", textAlign: "left"}}>{record.varKey}</div>
                <Icon onClick={this.deleteConfirm.bind(this, record.id)}
                      type="ashbin"
                      style={{float: "right", color: "#99ccff"}}/>
            </div>
        }
        const valueRender = (value, index, record) => {
            console.log(record);

            if (this.state.editMode) {
                return <div>
                    <Form>
                        <FormItem>
                            <Input {...init("value")} placeholder="Value"/>
                        </FormItem>
                    </Form>
                    <div onClick={this.submitConfirm.bind(this, record.id)}>保存</div>
                    <div onClick={this.onCancel.bind(this)}>取消</div>
                </div>
            } else {
                return <div>
                    <div style={{float: "left", textAlign: "left"}}>******</div>
                    <Icon type="edit"
                          onClick={this.onEdit.bind(this)}
                          style={{float: "right", color: "#81c7ff"}}/>
                </div>
            }
        }

        return (
            <div style={{width: "48%", margin: "0 auto"}}>
                    <CreateApplicationVariableDialog
                        refreshApplicationVariableList={this.refreshApplicationVariableList.bind(this)}
                        appId={this.state.appId}/>
                <div>

                    <Table style={{width: "100%"}}
                           dataSource={this.state.varData}>
                        <Table.Column cell={keyRender}
                                      title="Key"
                                      dataIndex="key"/>
                        <Table.Column cell={valueRender}
                                      title="Value"/>
                    </Table>
                </div>
            </div>
        )
    }


}