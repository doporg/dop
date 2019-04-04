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
            deleteData:[],
            style: {
                width: "30%"
            },
            deleteDialogStyle: {
                width: "10%"
            },
            deleteDialogVisible: false,
        }
    }
    onDeleteNamespace = () =>{
        //删除对应的命名空间
        // let url = API.test_image + '/v1/projects'+projectId;
        // let _this = this;
        // Axios.delete(url, {
        //     headers: {
        //         "x-login-user":37
        //     }
        //
        // }).then(function (response) {
        //         console.log("删除镜像");
        // })

        this.setState({
            deleteDialogVisible: false,
            deleteData:[]
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