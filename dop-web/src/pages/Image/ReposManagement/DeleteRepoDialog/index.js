import React,{Component} from 'react';
import {Dialog,Feedback} from '@icedesign/base';
import {Button} from "@alifd/next";
import Axios from "axios";
import API from "../../../API";


const Toast = Feedback.toast;
export default class DeleteRepoDialog extends Component{


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
            refreshRepoList: this.props.refreshRepoList
        }
    }
    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({
            deleteKeys: nextProps.deleteKeys
        });
    }
    onDeleteRepo = () =>{

        //删除对应的镜像仓库
        let url ="";
        let _this = this;
        for (let i = 0; i < this.state.deleteKeys.length; i++) {
            url = API.image + "/v1/repositories/"+this.state.deleteKeys[i];
            Axios.delete(url, {})
                .then(function (response) {
                    Toast.success("删除成功");
                    _this.state.refreshRepoList();
                    console.log("删除镜像仓库");
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
            Toast.error("请先选择仓库后再删除！");
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
                <Button onClick={this.onOpen} type="primary" warning>删除镜像仓库</Button>

                <Dialog visible={this.state.deleteDialogVisible}
                        onOk={this.onDeleteRepo}
                        onCancel={this.onDeleteLogClose}
                        onClose={this.onDeleteLogClose}
                        style={this.state.deleteDialogStyle}
                        footerAlign={this.state.footerAlign}>
                确定要删除已选择的镜像仓库吗？
                </Dialog>
            </span>
        );
    }

}