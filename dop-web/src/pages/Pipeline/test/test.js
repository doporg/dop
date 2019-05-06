import React, {Component} from 'react';
import {injectIntl, FormattedMessage} from 'react-intl';
import Shell from '../components/chosenSteps/Shell'




class PipelineTest extends Component {
    render() {
        return (
            <div>
                {this.props.intl.messages["pipeline.hello"]}
                <FormattedMessage
                    id="pipeline.hello"
                    defaultMessage="你好"
                />
                <Shell />
            </div>
        )
    }
}
export default injectIntl(PipelineTest)
