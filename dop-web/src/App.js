import {Component} from 'react';
import './App.css';
import router from './router/router'


class App extends Component {
    constructor() {
        super();
        this.timestamp = 0;
    }

    componentWillMount() {
    }

    render() {
        return router;
    }
}
export default App;
