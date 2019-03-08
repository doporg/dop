import React, {Component} from 'react';
import './RegisterTransfer.scss';
import Logo from '../../../components/Logo'
import Footer from "../../../components/Footer";
import Axios from 'axios'
import API from '../../API'
import {Feedback} from "@icedesign/base";
import {RSA} from "../index";
const {toast} = Feedback;

export default class RegisterTransfer extends Component {

    componentWillMount(){
        let self = this;
        RSA().then((data)=>{
            self.activation()
        }).catch((error)=>{
            toast.show({
                type: "error",
                content: "权限网络请求失败",
                duration: 1000
            });
        })
    }
    activation(){
        let self = this;
        let data = self.props.location.search.substr(6);
        let url = API.gateway + '/user-server/v1/users?code=' + data;
        Axios.post(url).then((response)=>{
            if(response.status === 200){
                toast.show({
                    type: "success",
                    content: "注册成功",
                    duration: 5000
                });
            }else{
                console.log(response)
            }
        }).catch((error)=>{
            console.log(error.response)
            toast.show({
                type: "error",
                content: error.response.data.message,
                duration: 3000
            });
        });
        self.props.history.push('/login')
    }

    render() {
        return (
            <div className="register-body">
                <div className="header-content">
                    <Logo isDark/>
                </div>
                <div className="register-content">
                    <div className="title">
                        欢迎注册DevopsPlatform
                    </div>
                    <div className="tip">
                        本项目由南京大学软件学院DOP项目组开发,如遇不便请联系Q
                        <br />  <br />
                        请等待激活......
                    </div>
                </div>
                <Footer/>
            </div>
        )
    }
}
