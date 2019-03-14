import React, {Component} from 'react';
import {Step} from '@icedesign/base';
import './Styles.scss'

export default class RunStep extends Component {

    current(index) {
        this.props.current(index)
    }

    render() {
        return (
            <Step current={1} type="dot">
                {
                    this.props.steps.map((item, index) => {
                        return (
                            <Step.Item
                                className="stepItem"
                                {...item}
                                key={index}
                                onClick={this.current.bind(this, index)}
                            />
                        )
                    })
                }
            </Step>
        )
    }
}
