import React,{Component} from 'react';
import {Dialog,Button} from '@icedesign/base';



export default class DeleteNameSpaceDialog extends Component{

    constructor(props){
        super(props);
        this.state={
            isSubmit:false,
            footerAlign: "center",
            visible: false,
            style: {
                width: "30%"
            },
            createDialogStyle: {
                width: "10%"
            },
            createDialogVisible: false,
        }
    }

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

        this.state.refreshProjectList();
        console.log("finished");
    }

    render() {
        return (
            <span>

                <Dialog visible={this.state.createDialogVisible}
                        onOk={this.onCreateDialogClose}
                        onCancel={this.onCreateDialogClose}
                        onClose={this.onCreateDialogClose}
                        title="删除成功"
                        style={this.state.createDialogStyle}
                        footerAlign={this.state.footerAlign}>
                命名空间删除成功！
                </Dialog>
            </span>
        );
    }

}