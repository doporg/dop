import React, {Component} from 'react';
import './RegisterTransfer.scss';
import Logo from '../../../components/Logo'
import Footer from "../../../components/Footer";


export default class LoginTransfer extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="register-body">
                <div className="header-content">
                    <Logo isDark/>
                </div>
                <div className="register-content">
                    <div className="title">
                        欢迎访问DevopsPlatform
                    </div>
                    <div className="tip">
                        正在跳转到登陆页面...
                    </div>
                </div>
                <Footer/>
            </div>
        )
    }
}
