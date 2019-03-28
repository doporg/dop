import React, { Component } from 'react';
import {
    FormBinderWrapper,
    FormBinder,
    } from '@icedesign/form-binder';
import {
    Input,
    Grid,
} from '@icedesign/base';

export default class WaitOperation extends Component{

    constructor(props) {
        super(props);
        this.state = {
            value: {
                waitTime: '',
                order: this.props.order
            }
        }
    }

    validateFormAndPost = () => {
        let noError = true;
        this.refs.form.validateAll((errors, values) => {
            if (errors != null) {
                noError = false;
            }
        });
        if (noError) {
            this.doSubmit(this.state.value);
        }
    };

    doSubmit = (content) => {
        this.props.submitWait(content);
    };

    componentWillReceiveProps(nextProps, nextContext) {
        console.log("Operation props change!");
        if (nextProps.isSubmit) {
            this.validateFormAndPost();
        }
    }

    render() {
       return (
           <FormBinderWrapper
               value={this.state.value}
               ref="form">
               <FormBinder name="waitTime">
                  <Input style={{width: '60%'}} placeholder="请输入等待时长，以毫秒为单位"> </Input>
               </FormBinder>
           </FormBinderWrapper>
       );
    }
}
