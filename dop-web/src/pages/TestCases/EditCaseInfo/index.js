import React, { Component } from 'react';
import DetailTable from './components/DetailTable';
import StepForm from "../CreateInterfaceScripts/components/StepForm/StepForm";
import API from "../../API";
import Axios from "axios";
import {Feedback} from "@icedesign/base";

const Toast = Feedback.toast;

export default class EditCaseInfo extends Component {
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

    this.doGetCase(case_Id);
  }

  componentWillReceiveProps(nextProps, nextContext) {
    if (nextProps.match.params.caseId !== this.state.caseId) {
      let newCaseId = nextProps.match.params.caseId;
      this.doGetCase(newCaseId);
    }
  }

  doGetCase(caseID) {
    let url = API.test + '/interfaceCases/' + caseID;
    let _this = this;
    Axios.get(url).then(function (response) {
      let caseDto = response.data;
      if (caseDto) {
        let newStages = _this.doTrans(caseDto);
        _this.setState({
          stages: newStages
        });
        console.log(newStages);
      }else {
        Toast.error("No interface case info!");
      }
    }).catch(function (error) {
      console.log(error);
      Toast.error(error);
    });
  }

  doTrans (caseDto) {
    return caseDto.stages;
  };

  render() {
    return (
        <div className="edit-case-info-page">
          <DetailTable/>
          <StepForm caseId={this.state.caseId} stages={this.state.stages} operation='UPDATE'/>
        </div>
    );
  }
}
