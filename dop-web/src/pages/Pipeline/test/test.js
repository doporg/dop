import React, {Component} from 'react';
import {injectIntl, FormattedMessage} from 'react-intl';
import DockerImage from '../components/chosenSteps/DockerImage'




class PipelineTest extends Component {

    handleChange(exp) {
        this.setState({
            cronExpression: exp.format()
        });
        console.log(exp, exp.format());
    }
    render() {
        console.log(this.props.intl);
        return (
            <div>
                {this.props.intl.messages["pipeline.hello"]}
                <FormattedMessage
                    id="pipeline.hello"
                    defaultMessage="你好"
                />
                <DockerImage
                    onChangeApp = {(value)=>{console.log(value)}}
                    onSelectEnv = {(value)=>{console.log(value)}}
                />
            </div>
        )
    }
}
export default injectIntl(PipelineTest)
