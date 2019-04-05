import React,{Component} from 'react';
import {Dialog,Feedback} from '@icedesign/base';
import {Button} from "@alifd/next";
import Axios from "axios";
import API from "../../../API";


const Toast = Feedback.toast;
export default class DeleteNameSpaceDialog extends Component{


    constructor(props){
        super(props);
        this.state={
            footerAlign: "center",
            style: {
                width: "30%"
            },
            deleteDialogStyle: {
                width: "10%"
            },
            deleteDialogVisible: false,
            deleteKeys:[],
            refreshProjectList: this.props.refreshProjectList,
        }
    }
    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({
            deleteKeys: nextProps.deleteKeys
        });
    }
    onDeleteNamespace = () =>{
        //删除对应的命名空间
        let url ="";
        let _this = this;
        for (let i = 0; i < this.state.deleteKeys.length; i++) {
            url = API.test_image + '/v1/projects/'+this.state.deleteKeys[i];
            Axios.delete(url, {
                //之后要删除
                headers: {
                    "x-login-user":37
                }
            }).then(function (response) {
                Toast.success("删除成功");
                _this.state.refreshProjectList();
                console.log("删除镜像");
                console.log(response.status);
            }).catch(function (error) {
                console.log(error);
                Toast.error("删除失败,请确认权限后重试！");
            });
        }
        this.setState({
            deleteDialogVisible: false
        })

    }


    onOpen = () => {
        if(this.state.deleteKeys.length===0){
            Toast.error("前先选择命名空间再删除！");
        }else {
            this.setState({
                deleteDialogVisible: true
            });
        }
    };


    onDeleteLogClose =()=>{
        this.setState({
            deleteDialogVisible:false
        })
    };

    render() {
        return (
            <span>
                <Button onClick={this.onOpen} type="primary" warning>删除命名空间</Button>

                <Dialog visible={this.state.deleteDialogVisible}
                        onOk={this.onDeleteNamespace}
                        onCancel={this.onDeleteLogClose}
                        onClose={this.onDeleteLogClose}
                        style={this.state.createDialogStyle}
                        footerAlign={this.state.footerAlign}>
                确定要删除命名空间？
                </Dialog>
            </span>
        );
    }

}