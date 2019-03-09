import {Component} from 'react';
import './App.css';
import router from './router/router'
import {RSA} from "./pages/Login";
import Axios from 'axios';
import {Feedback} from "@icedesign/base";
import API from './pages/API'
const {toast} = Feedback;

class App extends Component {
    componentWillMount() {
        Axios.interceptors.response.use((response) => {
            console.log("11")
            return Promise.resolve(response);
        }, (error) => {
            if(error.status === 500 && error.message === 'pre:AccessTokenZuulFilter'){
                window.sessionStorage.removeItem('Authorization');
                window.sessionStorage.removeItem('x-login-token');
                window.sessionStorage.removeItem('x-login-user');
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
