import {Button, Dialog, Field, Form, Input, Loading} from "@icedesign/base";

import React, {Component} from 'react';
import Axios from "axios";
import API from "../../../../API.js"
import "./CreateApplicationVariable.scss"

const FormItem = Form.Item;




const formItemLayout = {
    labelCol: {span: 8},
    wrapperCol: {span: 16}
};


/**
 *  创建应用变量的弹窗
 *  @author Bowen
 *
 * */

class ApplicationVariableForm extends Component {
    constructor(props, context) {
        super(props, context);
        this.field = new Field(this);
        this.state = {
            appId: props.appId,
            loading: false
        }
    }

    /**
     *    处理来自父组件按钮的提交信息
     *
     * */
    handleSubmit(props) {

        // 校验表单数据
        let _this = this;
        this.setState({
            loading: true
        })
        this.field.validate((errors, values) => {
            console.log(errors, values);

            // 没有异常则提交表单
            if (errors === null) {
                console.log("noerros");
                let postUrl = API.gateway + "/application-server/app/" + this.state.appId + "/variable";
                Axios.post(postUrl, {
                        "varKey": _this.field.getValue('key'),
                        "varValue": _this.field.getValue('value')
                    }
                )
                    .then(function (response) {
                        console.log(response);
                        _this.setState({
                            loading: false
                        })
                        props.finished();
                    })
                    .catch(function (error) {
                        console.log(error);
                    });

            }
        });

        //
        // console.log("handleSubmit");
    }

    componentWillReceiveProps(nextProps, nextContext) {
        if (nextProps.isSubmit) {
            this.handleSubmit(nextProps);
            // console.log((nextProps));

        }
    }

    render() {
        const {init} = this.field;
        return (
            <Loading visible={this.state.loading} shape="dot-circle" color="#2077FF"
            >
                <div>
                    <Form
                        labelAlign={"left"}
                        className="form"
                    >
                        <FormItem
                            {...formItemLayout}
                            validateStatus={this.field.getError("key") ? "error" : ""}
                            help={this.field.getError("key") ? "请输入Key" : ""}
                            label="Key："
                            required>
                            <Input {...init('key', {rules: [{required: true, message: "该项不能为空"}]})}
                                   placeholder="Key值"/>
                        </FormItem>

                        <FormItem
                            {...formItemLayout}
                            validateStatus={this.field.getError("value") ? "error" : ""}
                            label="Value："
                            required>
                            <Input  {...init('value', {rules: [{required: true, message: "该项不能为空"}]})}
                                    placeholder="value值"/>
                        </FormItem>
                    </Form>
                </div>
            </Loading>
        )
    }
}

/**
 *    创建应用变量的弹窗
 *
 * */
export default class CreateApplicationVariableDialog extends Component {


    onClose = () => {
        this.setState({
            visible: false
        });
    };

    onOk = () => {
        this.setState({
            isSubmit: true
            // visible: false
        });
    }


    onOpen = () => {
        this.setState({
            visible: true
        });
    };
    onCreateDialogClose = () => {
        this.setState({
            createDialogVisible: false
        });
    };

    constructor(props) {
        super(props);

        this.state = {
            //是否已经提交
            isSubmit: false,
            footerAlign: "center",
            visible: false,

            createDialogVisible: false,
            refreshApplicationVariableList: props.refreshApplicationVariableList,
            appId: props.appId
        }
    };

    /**
     *    回调函数传给子组件表单用于创建完成后修改提交状态和关闭弹窗
     *
     * */
    finished() {
        this.setState({
            visible: false,
            createDialogVisible: true,
            isSubmit: false
        })

        this.state.refreshApplicationVariableList();
        console.log("finished");
    }

    render() {
        return (
            <span>
                <Button onClick={this.onOpen}
                        type="primary"

                >
          新建变量
        </Button>
        <Dialog
            visible={this.state.visible}
            onOk={this.onOk}
            onCancel={this.onClose}
            onClose={this.onClose}
            title="新建变量"
            className="dialog"
            footerAlign={this.state.footerAlign}
        >
          <ApplicationVariableForm isSubmit={this.state.isSubmit} finished={this.finished.bind(this)}
                                   appId={this.state.appId}/>
        </Dialog>

<Dialog visible={this.state.createDialogVisible}
        onOk={this.onCreateDialogClose}
        onCancel={this.onCreateDialogClose}
        onClose={this.onCreateDialogClose}
        title="新建成功"
        className="success-dialog"
        footerAlign={this.state.footerAlign}>
新建成功！
</Dialog>
      </span>
        );
    }


}