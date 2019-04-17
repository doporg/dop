import React, {Component} from 'react';
import {injectIntl, FormattedMessage} from 'react-intl';


class PipelineTest extends Component {
    render() {
        console.log(this.props.intl);
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
