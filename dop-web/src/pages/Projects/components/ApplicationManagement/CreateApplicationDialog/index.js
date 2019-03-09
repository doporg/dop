/**
 *  创建应用的弹窗（未开始使用）
 *  @author Bowen
 *
 * */


import {
    Dialog,
    Button,
    Grid,
    Input,
    Form,
    Field,
    Radio
}
    from "@icedesign/base";

import React, {Component} from 'react';
import Axios from "axios";
import API from "../../../../API.js"

const FormItem = Form.Item;
const {Row, Col} = Grid;
const {Group: RadioGroup} = Radio;
const list = [
    {
        value: "FREE",
        label: "自由模式"
    },
    {
        value: "BRANCH",
        label: "分支模式"
    },
];
const style = {
    padding: "20px",
    background: "#F7F8FA",
    margin: "20px"
};
const formItemLayout = {
    labelCol: {span: 8},
    wrapperCol: {span: 16}
};


/**
 *    私密性单选按钮
 *
 * */
class ProductModeController extends Component {
    constructor(props) {
        super(props);


        //与Filed组件通信
        this.state = {
            value: typeof props.value === "undefined" ? [] : props.value
        };

        this.onChange = this.onChange.bind(this);
    }

    componentWillReceiveProps(nextProps) {
        if ("value" in nextProps) {
            this.setState({
                value: typeof nextProps.value === "undefined" ? [] : nextProps.value
            });
        }
    }


    onChange(value) {
        this.setState({
            value: value
        });
        this.props.onChange(value);
    }

    render() {
        return (
            <div>
                <RadioGroup
                    dataSource={list}
                    value={this.state.value}
                    onChange={this.onChange}
                />
            </div>
        );
    }

}

/**
 *    弹窗中的表单
 *
 * */
class ApplicationForm extends Component {
    constructor(props, context) {
        super(props, context);
        this.field = new Field(this);
        this.state = {
            projectId: props.projectId
        }
    }

    /**
     *    处理来自父组件按钮的提交信息
     *
     * */
    handleSubmit(props) {

        // 校验表单数据
        this.field.validate((errors, values) => {
            console.log(errors, values);

            // 没有异常则提交表单
            if (errors == null) {
                console.log("noerros");
                let url = API.application + '/applications';
                Axios.post(url, {}, {
                        params: {
                            projectId: this.state.projectId,
                            title: this.field.getValue('title'),
                            productMode: this.field.getValue('productMode'),
                            appDescription: this.field.getValue('description'),
                            gitUrl: this.field.getValue('gitUrl')
                        }
                    }
                )
                    .then(function (response) {
                        console.log(response);
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
        const {init, getValue} = this.field;
        return (
            <div>
                <Form
                    labelAlign={"left"}
                    style={style}
                >
                    <FormItem {...formItemLayout} validateStatus={this.field.getError("title") ? "error" : ""}
                              help={this.field.getError("title") ? "请输入名称" : ""} label="应用名称：" required>
                        <Input {...init('title', {rules: [{required: true, message: "该项不能为空"}]})}
                               placeholder="请输入项目名称"/>
                    </FormItem>
                    <FormItem {...formItemLayout} validateStatus={this.field.getError("productMode") ? "error" : ""}
                              help={this.field.getError("productMode") ? "请选择开发模式" : ""} label="开发模式：" required>
                        <ProductModeController {...init('productMode', {
                            rules: [{required: true, initValue: "BRANCH"}]
                        })}/>
                    </FormItem>
                    <FormItem {...formItemLayout} label="git仓库地址：">
                        <Input  {...init('gitUrl')} placeholder="git仓库地址"/>
                    </FormItem>
                    <FormItem {...formItemLayout} label="应用描述：">
                        <Input  {...init('description')} multiple placeholder="应用描述"/>
                    </FormItem>
                </Form>
            </div>
        )
    }
}

/**
 *    创建项目的弹窗
 *
 * */
export default class CreateApplicationDialog extends Component {


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
            style: {
                width: "30%"
            },
            createDialogStyle: {
                width: "10%"
            },
            createDialogVisible: false,
            refreshApplicationList: props.refreshApplicationList,
            projectId: props.projectId
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

        this.state.refreshApplicationList();
        console.log("finished");
    }

    render() {
        return (
            <span>
                <Button onClick={this.onOpen} type="primary">
          创建应用
        </Button>
        <Dialog
            visible={this.state.visible}
            onOk={this.onOk}
            onCancel={this.onClose}
            onClose={this.onClose}
            title="创建应用"
            style={this.state.style}
            footerAlign={this.state.footerAlign}
        >
          <ApplicationForm isSubmit={this.state.isSubmit} finished={this.finished.bind(this)}
                           projectId={this.state.projectId}/>
        </Dialog>

<Dialog visible={this.state.createDialogVisible}
        onOk={this.onCreateDialogClose}
        onCancel={this.onCreateDialogClose}
        onClose={this.onCreateDialogClose}
        title="创建成功"
        style={this.state.createDialogStyle}
        footerAlign={this.state.footerAlign}>
项目创建成功！
</Dialog>
      </span>
        );
    }


}