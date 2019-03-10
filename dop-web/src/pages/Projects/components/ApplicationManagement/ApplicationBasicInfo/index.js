import Pagination from "../../../Application/Application";
import {Tab} from "@icedesign/base";
import React, {Component} from 'react';
import {Card} from '@icedesign/base';
import Axios from "axios";
import API from "../../../../API";
import {
    Form,
    Input,
    Button,
    Field
} from "@icedesign/base";
import Table from "@icedesign/base/lib/table";

const FormItem = Form.Item;
const formItemLayout = {
    labelCol: {span: 8},
    wrapperCol: {span: 16}
};
const style = {
    padding: "20px",
    background: "#F7F8FA",
    margin: "20px",
    width: "50%"
};
export default class ApplicationBasicInfo extends Component {

    constructor(props) {
        super(props);
        this.field = new Field(this);
        this.state = {}

        // let url = API.gateway + '/application-server/applicationDetail';
        // Axios.get(url,{
        //     params:{
        //     appId:props.appId
        //     }}
        //
        // )
        //     .then(function (response)
        // {
        //     this.state={
        //         appId:props.appId,
        //         app:response.data
        //     }
        // })
        // let userInfoUrl = API.gateway + '/user-server/v1/users/' + 23;
        // let _this=this;
        // Axios.get(userInfoUrl)
        //     .then(function (response) {
        //         _this.setState({
        //             userData:response
        //         })
        //     })

        // let basicUrl = API.gateway + '/application-server/applicationDetail';
        // Axios.get(url,{
        //     params:{
        //         appId:props.appId
        //     }}
        //
        // )
        //     .then(function (response)
        //     {
        //         this.state={
        //             appId:props.appId,
        //             app:response.data
        //         }
        //     })

    }

    render() {
        const {init, getValue} = this.field;
        return (
            <  div style={{display: "flex", flexWrap: "wrap", justifyContent: "center"}}>
                <Card
                    style={{width: 300, height: 300}}
                    title="Bowen"
                    subTitle="应用拥有人"
                    extra={<a href="#">转交应用&gt;</a>}
                >
                    <img
                        src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFAAAABQBAMAAAB8P++eAAAABGdBTUEAALGPC/xhBQAAAAFzUkdCAK7OHOkAAAAYUExURcHBwb+/v7+/v76+vujo6OHh4cnJydTU1IOqnXYAAAADdFJOUxPppyMYpxkAAAD6SURBVEjH7dfbDYIwFAbguoHRCYwTKLcBOIUBaHQAIAxQwvwSEQpyaH/FFxP+5y89vacV4uQBOQix86DsxRmDV3HE4EV4YDa4QQRWSjYILKnNzQ0jekY7Yd3B1AVDeiV3wKCHsQPWPUwdkIbYYWSgtsLAwMwKfQNjFCZWWPwBhEcNz+NoZfLfrLXZPYkD+gtd/H6H97UT5+EK0FPY1ZbABaDYygysuTEvtqg9sI9AiyV/o8xgRNj0DLtHaiuszOahxgJLGueeL8Gpa8vnPHx30yEZGKo5lBwMiEnGwIKDKQMVB+UaSGzWwO2psMGPIfxgh78A8KcC/aY8ACmMo3JtJ3ljAAAAAElFTkSuQmCC"
                        style={styles.avatar}/>
                </Card>

                <div style={style}>
                    <div>基本信息</div>
                    <Form labelAlign={"left"}
                          style={style}>
                        <FormItem{...formItemLayout} label="应用名称：">
                            <div  {...init('appTitle')} placeholder="应用名称">测试应用</div>
                        </FormItem>
                        <FormItem{...formItemLayout} label="注册时间：">
                            <div>2019-3-10 17:31</div>
                        </FormItem>
                        <FormItem{...formItemLayout} label="应用描述：">
                            <Input  {...init('testDbUrl')} multiple placeholder="应用描述"/>
                        </FormItem>
                        <Button type="primary" style={{marginRight: "5px"}}>
                            保存
                        </Button>
                        <Button>取消</Button>
                    </Form>
                </div>

                <Form labelAlign={"left"}
                      style={style}>
                    <FormItem{...formItemLayout} label="Git仓库地址：">
                        <Input  {...init('wareHouseUrl')} placeholder="Git仓库地址"/>
                    </FormItem>
                    <FormItem{...formItemLayout} label="开发数据库地址：">
                        <Input  {...init('productDbUrl')} placeholder="开发数据库地址"/>
                    </FormItem>
                    <FormItem{...formItemLayout} label="测试数据库地址：">
                        <Input  {...init('testDbUrl')} placeholder="测试数据库地址"/>
                    </FormItem>
                    <FormItem{...formItemLayout} label="开发域名：">
                        <Input  {...init('productDomain')} placeholder="开发域名"/>
                    </FormItem>
                    <FormItem{...formItemLayout} label="测试域名：">
                        <Input  {...init('testDomain')} placeholder="测试域名"/>
                    </FormItem>
                    <Button type="primary" style={{marginRight: "5px"}}>
                        保存
                    </Button>
                    <Button>取消</Button>
                </Form>
            </div>


        )
    }
}
const styles = {
    avatar: {
        maxWidth: "none",
        height: "80px",
        opacity: "1",
        width: "80px",
        margiLeft: "30px",
        marginTop: "30px",
    }
};