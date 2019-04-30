import {Button, Dialog,} from "@icedesign/base";
import ProjectForm from "../CreateProjectForm/CreateProjectForm"
import React, {Component} from 'react'
import "./CreateProjectDialog.scss"

/**
 *  创建项目的弹窗
 *  @author Bowen
 *
 * */
export default class CreateProjectDialog extends Component {
    constructor(props) {
        super(props);

        this.state = {
            //是否已经提交
            isSubmit: false,
            footerAlign: "center",
            visible: false,

            createDialogVisible: false,
            refreshProjectList: props.refreshProjectList
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

        this.state.refreshProjectList();
        console.log("finished");
    }

    render() {
        return (
            <span>
                <Button onClick={this.onOpen} type="primary">
          创建项目
        </Button>
        <Dialog
            visible={this.state.visible}
            onOk={this.onOk}
            onCancel={this.onClose}
            onClose={this.onClose}
            title="创建项目"
            className="dialog"
        >
          <ProjectForm
              isSubmit={this.state.isSubmit}
              finished={this.finished.bind(this)}/>
        </Dialog>

<Dialog visible={this.state.createDialogVisible}
        onOk={this.onCreateDialogClose}
        onCancel={this.onCreateDialogClose}
        onClose={this.onCreateDialogClose}
        title="创建成功"
        className="success-dialog"
        footerAlign={this.state.footerAlign}>
项目创建成功！
</Dialog>
      </span>
        );
    }


}
