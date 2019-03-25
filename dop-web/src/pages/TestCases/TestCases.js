import React, { Component } from 'react';
import UserTable from './components/UserTable';

export default class TestCases extends Component {
    constructor(props) {
        super(props);
        this.state = {};
    }

    render() {
        return (
            <div className="test-cases-page">
                <UserTable />
            </div>
        );
    }
}
