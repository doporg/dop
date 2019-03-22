import {Component} from 'react';
import './App.css';
import router from './router/router'
import {RSA} from "./pages/Login";
import Axios from 'axios';
import {Feedback} from "@icedesign/base";
import API from './pages/API'
import CryptoJS from 'crypto-js'
const {toast} = Feedback;


class App extends Component {
    componentWillMount() {
        if(window.sessionStorage.getItem('Authorization')){
            Axios.defaults.headers.common['Authorization'] = "Bearer " + window.sessionStorage.getItem('Authorization');
        }
        if(window.sessionStorage.getItem('x-login-token')){
            Axios.defaults.headers.common['x-login-token'] = window.sessionStorage.getItem('x-login-token');
        }

        Axios.interceptors.response.use((response) => {
            return Promise.resolve(response);
        }, (error) => {
            if (error.response) {
                console.log(11)
                toast.show({
                    type: "error",
                    content: error.response.data.message,
                    duration: 1000
                });
            } else {
                console.log('Error', error.message);
            }

            if(error.status === 500 && error.response.data.message === 'pre:AccessTokenZuulFilter'){
                window.sessionStorage.clear();
                toast.show({
                    type: "error",
                    content: "Token失效, 请重新登陆",
                    duration: 5000
                });
                setTimeout(()=>{
                    window.close();
                },5000);
                window.open(API.address + "login");
            }
            return Promise.reject(error);
        });
        setInterval(() => {
            RSA()
        }, 3540000)
    }

    render() {
        return router;
    }
}

export default App;
