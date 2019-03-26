import {
    Dialog,
    Button, Icon,
}
    from "@icedesign/base";
import React, {Component} from 'react'
import CreateManualCaseFrom from "../CreateManualCaseForm";

/**
 *  创建项目的弹窗
 *  @author Bowen
 *
 * */
export default class CreateManualCaseDialog extends Component {
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
            // refreshCaseList: props.refreshCaseList
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
    };


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
        });

        // this.state.refreshCaseList();
        console.log("finished");
    }

    render() {
        return (
            <span>
                <Button onClick={this.onOpen} type="primary">
          创建项目
        </Button>
                <Button type="primary" style={styles.button} onClick={this.onOpen()}>
              <Icon type="add" size="xs" style={{ marginRight: '4px' }} />添加手工测试用例
            </Button>
        <Dialog
            visible={this.state.visible}
            onOk={this.onOk}
            onCancel={this.onClose}
            onClose={this.onClose}
            title="创建手工测试用例"
            style={this.state.style}
            footerAlign={this.state.footerAlign}
        >
          <CreateManualCaseFrom
              isSubmit={this.state.isSubmit}
              finished={this.finished.bind(this)}/>
        </Dialog>

<Dialog visible={this.state.createDialogVisible}
        onOk={this.onCreateDialogClose}
        onCancel={this.onCreateDialogClose}
        onClose={this.onCreateDialogClose}
        title="创建成功"
        style={this.state.createDialogStyle}
        footerAlign={this.state.footerAlign}>
手工测试用例创建成功！
</Dialog>
      </span>
        );
    }
}

const styles = {
    headRow: {
        marginBottom: '10px',
    },
    icon: {
        color: '#2c72ee',
        cursor: 'pointer',
    },
    deleteIcon: {
        marginLeft: '20px',
    },
    center: {
        textAlign: 'right',
    },
    button: {
        borderRadius: '4px',
    },
    pagination: {
        marginTop: '20px',
        textAlign: 'right',
    },
};
