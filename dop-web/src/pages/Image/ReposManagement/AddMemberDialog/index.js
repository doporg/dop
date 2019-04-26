import React,{Component} from 'react';
import {Button,Dialog} from "@icedesign/base";
import ProjectMemberForm from "../ProjectMemberForm"

export default class AddMemberDialog extends Component{

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
            refreshMemberList: this.props.refreshList,
            projectId : this.props.projectId
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

    finished() {
        this.setState({
            visible: false,
            createDialogVisible: true,
            isSubmit: false
        })

        this.state.refreshMemberList();
        console.log("finished");
    }


    render() {
        return (
            <span className={"dialog"}>
                <Button onClick={this.onOpen} type="primary">添加用户</Button>
        <Dialog
            visible={this.state.visible}
            onOk={this.onOk}
            onCancel={this.onClose}
            onClose={this.onClose}
            title="新建成员"
            style={this.state.style}
            footerAlign={this.state.footerAlign}
        >
          <ProjectMemberForm
              isSubmit={this.state.isSubmit}
              finished={this.finished.bind(this)} projectId={this.state.projectId}/>
        </Dialog>

<Dialog visible={this.state.createDialogVisible}
        onOk={this.onCreateDialogClose}
        onCancel={this.onCreateDialogClose}
        onClose={this.onCreateDialogClose}
        title="添加成功"
        style={this.state.createDialogStyle}
        footerAlign={this.state.footerAlign}>
成功添加成员到命名空间！
</Dialog>
      </span>
        );
    }
}