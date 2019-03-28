import React, { Component } from 'react';
import StepForm from './components/StepForm';

export default class CreateInterfaceScripts extends Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {
    return (
      <div className="create-interface-scripts-page">
        <StepForm />
      </div>
    );
  }
}
