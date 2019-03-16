import React, {Component} from 'react';

import {Radio} from "@icedesign/base";

const {Group: RadioGroup} = Radio;
const list = [
    {
        value: "FREE",
        label: "自由模式"
    },
    {
        value: "BRANCH",
        label: "分支模式"
    },
];
/**
 *    开发模式按钮
 *
 * */
export default class ProductModeController extends Component {
    constructor(props) {
        super(props);


        //与Filed组件通信
        this.state = {
            value: typeof props.value === "undefined" ? [] : props.value
        };

        this.onChange = this.onChange.bind(this);
    }

    componentWillReceiveProps(nextProps) {
        if ("value" in nextProps) {
            this.setState({
                value: typeof nextProps.value === "undefined" ? [] : nextProps.value
            });
        }
    }


    onChange(value) {
        this.setState({
            value: value
        });
        this.props.onChange(value);
    }

    render() {
        return (
            <div>
                <RadioGroup
                    dataSource={list}
                    value={this.state.value}
                    onChange={this.onChange}
                />
            </div>
        );
    }

}