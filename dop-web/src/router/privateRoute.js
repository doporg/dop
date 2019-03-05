import React, {Component} from 'react';
import {Route, withRouter} from 'react-router-dom';
import LoginTransfer from '../pages/Login/Transfer/LoginTransfer'

class PrivateRoute extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isAuthenticated: !!window.sessionStorage.getItem("userId")
        }
    }

    componentWillMount() {
        if(!this.state.isAuthenticated){
            const {history} = this.props;
            setTimeout(() => {
                history.replace("/login");
            }, 1000)
        }
    }

    render() {
        let { ...rest} = this.props;
        console.log(this.props)
        console.log(rest)
        return  this.state.isAuthenticated ?
            (<Route
                key={this.props.routePath}
                {...rest}
            /> ) :
            (<LoginTransfer />)

    }
}

export default withRouter(PrivateRoute);
