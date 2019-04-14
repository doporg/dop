import {Button, Dialog,} from "@icedesign/base";
import CreateApplicationForm from "../CreateApplicationForm/CreateApplicationForm"
import React, {Component} from 'react';
import "./CreateApplicationDialog.scss"


/**
 *  创建应用的弹窗
 *  @author Bowen
 *
 * */

export default class CreateApplicationDialog extends Component {

    constructor(props) {
        super(props);

        this.state = {
            //是否已经提交
            isSubmit: false,
            footerAlign: "center",
            visible: false,
            createDialogVisible: false,
            refreshApplicationList: props.refreshApplicationList,
            projectId: props.projectId
        }
    };
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
            className="dialog"
            footerAlign={this.state.footerAlign}
        >
          <CreateApplicationForm isSubmit={this.state.isSubmit} finished={this.finished.bind(this)}
                                 projectId={this.state.projectId}/>
        </Dialog>

<Dialog visible={this.state.createDialogVisible}
        onOk={this.onCreateDialogClose}
        onCancel={this.onCreateDialogClose}
        onClose={this.onCreateDialogClose}
        title="创建成功"
        className="success-dialog"
        footerAlign={this.state.footerAlign}>
应用创建成功！
</Dialog>
      </span>
        );
    }


}