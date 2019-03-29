import React, { Component } from 'react';
import CustomTable from './CustomTable';

export default class UserTable extends Component {
  static displayName = 'UserTable';

  static propTypes = {};

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {
    return (
      <div>
        <CustomTable />
      </div>
    );
  }
}
