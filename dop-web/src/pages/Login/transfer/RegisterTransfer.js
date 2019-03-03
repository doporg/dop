import React, {Component} from 'react';
import './RegisterTransfer.scss';
import Logo from '../../../components/Logo'
import Footer from "../../../components/Footer";
import {Form, Input, Button, Field, Feedback} from "@icedesign/base";


export default class RegisterTransfer extends Component {
    constructor(props) {
        super(props);
        this.field = new Field(this);
        this.state = {
            access_token: ""
        }
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
                </div>
                <Footer/>
            </div>
        )
    }
}
