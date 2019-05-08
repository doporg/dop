import React,{Component} from 'react';
import {Button,Dialog} from "@icedesign/base";
import ProjectMemberForm from "../ProjectMemberForm"
import {injectIntl,FormattedMessage} from "react-intl";
 class AddMemberDialog extends Component{

    constructor(props) {
        super(props);

        this.state = {
            //是否已经提交
            isSubmit: false,
            footerAlign: "center",
            visible: false,
            style: {
                width: "35%"
            },
            createDialogStyle: {
                width: "15%"
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
                <Button onClick={this.onOpen} type="primary">
                    <FormattedMessage id="image.addMember"
                                      defaultMessage="添加成员"/>
                </Button>
        <Dialog language={this.props.intl.locale==='zh-CN'?'zh-cn':'en-us'}
            visible={this.state.visible}
            onOk={this.onOk}
            onCancel={this.onClose}
            onClose={this.onClose}
            title={this.props.intl.messages["image.addMember.title"]}
            style={this.state.style}
            footerAlign={this.state.footerAlign}
        >
          <ProjectMemberForm
              isSubmit={this.state.isSubmit}
              finished={this.finished.bind(this)} projectId={this.state.projectId}/>
        </Dialog>

<Dialog language={this.props.intl.locale==='zh-CN'?'zh-cn':'en-us'}
        visible={this.state.createDialogVisible}
        onOk={this.onCreateDialogClose}
        onCancel={this.onCreateDialogClose}
        onClose={this.onCreateDialogClose}
        title={this.props.intl.messages["image.addMember.addSuccess"]}
        style={this.state.createDialogStyle}
        footerAlign={this.state.footerAlign}>
    <FormattedMessage id="image.addMember.successMessage"
                      defaultMessage="成功添加成员到命名空间！"/>
</Dialog>
      </span>
        );
    }
}

export default injectIntl(AddMemberDialog);