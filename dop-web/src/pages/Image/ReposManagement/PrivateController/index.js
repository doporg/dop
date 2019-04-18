import React, {Component} from 'react';
import {Radio} from "@icedesign/base";

const {Group: RadioGroup} = Radio;
const list = [
    {
        value: "developer",
        label: "开发人员"
    },
    {
        value: 'guest',
        label: "游客"
    },
    {
        value: 'projectAdmin',
        label: "项目管理员"
    }
];


/**
 *    私密性单选按钮
 *
 * */
export default class PrivateController extends Component {
    constructor(props) {
        super(props);


        //与Filed组件通信
        this.state = {
            value: typeof props.value === "undefined" ? [] : props.value
        };

        this.onChange = this.onChange.bind(this);
    }

    //与Filed组件通信
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