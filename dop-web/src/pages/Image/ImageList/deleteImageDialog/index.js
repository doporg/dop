import React,{Component} from 'react';
import {Dialog,Feedback} from '@icedesign/base';
import {Button} from "@alifd/next";
import Axios from "axios";
import API from "../../../API";


const Toast = Feedback.toast;
export default class DeleteImageDialog extends Component{


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
            repoName:this.props.repoName,
            refreshImageList: this.props.refreshImageList
        }
    }
    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({
            deleteKeys: nextProps.deleteKeys,
            repoName:nextProps.repoName
        });

    }
    onDeleteRepo = () =>{
        console.log(this.state.repoName);
        //删除对应的镜像
        let url ="";
        let _this = this;
        for (let i = 0; i < this.state.deleteKeys.length; i++) {
            url = API.image + "/v1/repositories/"+this.state.repoName+"/images/"+this.state.deleteKeys[i];
            Axios.delete(url, {})
                .then(function (response) {
                    Toast.success("删除成功");
                    _this.state.refreshImageList(1,"");
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
            Toast.error("请先选择镜像后再删除！");
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
                <Button onClick={this.onOpen} type="primary" warning>删除镜像</Button>

                <Dialog visible={this.state.deleteDialogVisible}
                        onOk={this.onDeleteRepo}
                        onCancel={this.onDeleteLogClose}
                        onClose={this.onDeleteLogClose}
                        style={this.state.deleteDialogStyle}
                        footerAlign={this.state.footerAlign}>
                确定要删除已选择的镜像吗？
                </Dialog>
            </span>
        );
    }

}