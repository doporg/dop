import {Component} from 'react';
import './App.css';
import router from './router/router'


class App extends Component {
    constructor() {
        super();
    }

    render() {
        return router;
    }
}
export default App;
