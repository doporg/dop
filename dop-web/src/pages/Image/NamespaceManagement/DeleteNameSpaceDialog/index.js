import React,{Component} from 'react';
import {Dialog,Feedback,Button} from '@icedesign/base';
import Axios from "axios";
import API from "../../../API";
import '../../Style.scss'


const Toast = Feedback.toast;
export default class DeleteNameSpaceDialog extends Component{


    constructor(props){
        super(props);
        this.state={
            footerAlign: "center",
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
            url = API.image + '/v1/projects/'+this.state.deleteKeys[i];
            Axios.delete(url, {})
                .then(function (response) {
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
                <Button className="button" onClick={this.onOpen} type="primary" shape={"warning"}>删除命名空间</Button>

                <Dialog visible={this.state.deleteDialogVisible}
                        onOk={this.onDeleteNamespace}
                        onCancel={this.onDeleteLogClose}
                        onClose={this.onDeleteLogClose}
                        style={this.state.deleteDialogStyle}
                        footerAlign={this.state.footerAlign}>
                确定要删除命名空间？
                </Dialog>
           </span>
        );
    }

}