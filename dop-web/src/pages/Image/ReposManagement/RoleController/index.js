import React, {Component} from 'react';
import {Radio} from "@icedesign/base";
import "../../Style.scss"

const {Group: RadioGroup} = Radio;
const list = [
    {
        value: "2",
        label: "开发人员"
    },
    {
        value: '0',
        label: "游客"
    },
    {
        value: '1',
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