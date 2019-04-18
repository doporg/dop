import React, { Component } from 'react';
import StepForm from './components/StepForm';

export default class CreateInterfaceScripts extends Component {
  constructor(props) {
    super(props);
    const case_Id = this.props.match.params.caseId;
    this.state = {
      caseId: case_Id,
      stages: [
        {
          caseId: case_Id,
          operations: [],
          stage: 'PREPARE',
          requestScripts: [],
          waitOperations: []
        },
        {
          caseId: case_Id,
          operations: [],
          stage: 'TEST',
          requestScripts: [],
          waitOperations: []
        },
        {
          caseId: case_Id,
          operations: [],
          stage: 'DESTROY',
          requestScripts: [],
          waitOperations: []
        }
      ],
    };
  }

  render() {
    return (
      <div className="create-interface-scripts-page">
        <StepForm caseId={this.state.caseId} data={this.state.stages}/>
      </div>
    );
  }
}
