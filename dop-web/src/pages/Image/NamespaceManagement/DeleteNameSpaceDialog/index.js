import React,{Component} from 'react';
import {Dialog} from '@icedesign/base';
import {Button} from "@alifd/next";
import Axios from "axios";
import API from "../../../API";



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
                headers: {
                    "x-login-user":37
                }
            }).then(function (response) {
                _this.state.refreshProjectList();
                console.log("删除镜像");
                console.log(response.status);
            })
        }

        this.setState({
            deleteDialogVisible: false
        })

    }


    onOpen = () => {
        this.setState({
            deleteDialogVisible: true
        });
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