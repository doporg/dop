import {Component, defaultProps} from 'react';
import './App.css';
import router from './router/router'
import {getTimeStamp, actuator} from './components/oauth/oauth'

class App extends Component {
    constructor() {
        super();
        this.timestamp = 0;
        this.access_token = '';
    }

    componentWillMount() {
        getTimeStamp(this);
    }

    /**
     *  获取服务器端的时间戳并返回
     *  @return timestamp 服务器服务器时间戳+请求时间
     * */


    render() {
        return router;
    }
}

export default App;
