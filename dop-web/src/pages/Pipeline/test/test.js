import React, {Component} from 'react';
import {injectIntl, FormattedMessage} from 'react-intl';
import Axios from "axios/index";
import API from "../../API";




class PipelineTest extends Component {
    componentWillMount(){
        let url = API.pipeline + '/v1/resultOutput?id=5ccfae897169742b4cc87295';
        Axios.post(url).then((response) => {
        }).catch(() => {
        })
    }
    componentDidMount(){
        let url = API.pipeline + '/v1/resultOutput/notify/5ccfae897169742b4cc87295';
        Axios.post(url).then((response) => {
        }).catch(() => {
        })
    }
    render() {
        return (
            <div>
                {this.props.intl.messages["pipeline.hello"]}
                <FormattedMessage
                    id="pipeline.hello"
                    defaultMessage="你好"
                />
            </div>
        )
    }
}
export default injectIntl(PipelineTest)
