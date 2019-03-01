import Login from './Login/Login';
import Register from './Register/Register';
import Axios from 'axios';
import API from '../API'

const RSA = ()=>{
    let url = API.gateway + '/user-server/v1/account/RSAPublicKey';
    Axios.get(url).then((response)=>{
        console.log(response)
    })
};
RSA()

export {Login, Register};
