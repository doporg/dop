import React, {Component} from 'react';
import {injectIntl, FormattedMessage} from 'react-intl';
import Shell from '../components/chosenSteps/Shell'
import Axios from 'axios'
import API from '../../API'


class PipelineTest extends Component {
    componentWillMount() {
        Axios.post("http://localhost:13600/v1/resultOutput?id=5cda9322d65aed0001d807e0").then()
        Axios({
            url: "http://localhost:13600/v1/resultOutput/notify/5cda9322d65aed0001d807e0",
            method: "post",
            data: null,
            headers: {
                "x-login-user": 39
            }
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
                <Shell/>
            </div>
        )
    }
}

export default injectIntl(PipelineTest)
