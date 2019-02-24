/**
 *  流水线信息展示，修改
 *  @author zhangfuli
 *
 * */
import React, { Component } from 'react';
import { Button, Feedback, Loading, Form, Input} from '@icedesign/base';

const { toast } = Feedback;

const FormItem = Form.Item;

const layout = {
    labelCol: {
        fixedSpan: 4
    },
    wrapperCol: {
        span: 18
    },

};

export default class PipelineTest extends Component {
    constructor(props) {
        super(props);
        this.state = {
            visible: false
        };
    }

    setVisible(visible) {
        console.log(111)
        this.setState({
            visible
        });
    }
    handleButtonClick () {
        toast.success('欢迎使用 ICE 中后台应用解决0000000方案');
    }
    render() {
        return (<div>
            <Loading visible={this.state.visible} shape="fusion-reactor" className="next-loading">
                <Form style={{width: 500}}>
                    <FormItem label="Username" {...layout} >
                        <Input />
                    </FormItem>
                    <FormItem label="Password"  {...layout} >
                        <Input htmlType="password" placeholder="please input"/>
                    </FormItem>
                    <FormItem label="Detail" {...layout} >
                        <Input multiple  />
                    </FormItem>
                </Form>
            </Loading>
            <div style={{paddingLeft: 80}}>
                <Button onClick={this.setVisible.bind(this, true)} type="primary">Submit</Button>
                <Button onClick={this.setVisible.bind(this, false)} style={{marginLeft: 5}}>Cancel</Button>
                <Button
                    size="small"
                    type="primary"
                    onClick={this.handleButtonClick}
                >
                    点击弹出信息
                </Button>
            </div>
        </div>);
    }
}
