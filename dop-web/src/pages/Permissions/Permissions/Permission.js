/**
 *  功能点管理
 *  @author lizhiying
 *
 * */
import React, { Component } from 'react';

import Axios from 'axios';
import API from '../../API';
import {Table, Button, Dialog, Radio, Field, Feedback} from '@icedesign/base';
import Pagination from "@icedesign/base/lib/pagination";
import Form from "@icedesign/base/lib/form";
import BalloonConfirm from "@icedesign/balloon-confirm";
import Input from "@icedesign/base/lib/input";
import Select from "@icedesign/base/lib/select";
import {Nav ,Icon , Menu} from "@icedesign/base";
import '../Styles.scss'
import Search from "@icedesign/base/lib/search";


const { Item: FormItem } = Form;
const { Group: RadioGroup } = Radio;

export default class Permission extends Component {


    constructor(props) {
        super(props);
        this.field = new Field(this);
        this.state = {
            currentData : [],
            permissionSelectList:[],
            visible:false,
            deleteVisible:false,
            ruleVisible:false,
            isLoading:true,
            InputInfo:
                {
                    parentId:"",
                    name:"",
                    isPrivate:"",
                    description:"",
                },
            pageNo:1,
            pageSize:8,
            totalCount:0
        };

    }
    handleReset(e) {
        e.preventDefault();
        this.field.reset();
    }


    //取消删除操作
    onCancel = () => {
        console.log('取消删除');
        Feedback.toast.error('删除已取消！')
    }

    //弹出创建功能点弹窗
    onOpen = () => {
        this.setState({
            visible: true
        });
    };
    //关闭功能点弹窗
    onClose = reason => {
        console.log(reason);

        this.setState({
            visible: false
        });
    };

    //每次访问的刷新
    componentDidMount() {
        this.setState({isLoading:true})
        let url = API.permission + "/v1/permissions" ;
        let params=
            {
                pageNo:this.state.pageNo,
                pageSize:this.state.pageSize
            }
        Axios.get(url,{params:(params)}).then((response) => {
            this.setState({
                currentData:response.data.pageList,
                pageNo:response.data.pageNo,
                totalCount:response.data.totalCount,
                isLoading:false
            })
        }).catch((error)=>{
            // handle error
            console.log(error);
        }).then(()=>{
            // always executed
        });
    }
    onChange=currentPage=> {

        this.setState({isLoading:true})
        let url = API.permission + "/v1/permissions" ;
        let params=
            {
                pageNo:currentPage,
                pageSize:this.state.pageSize
            }
        // console.log(params)
        Axios.get(url,{params:(params)}).then((response) => {
            // handle success
            this.setState({
                currentData:response.data.pageList,
                pageNo:response.data.pageNo,
                totalCount:response.data.totalCount,
                    isLoading:false
            }
               );
        }).catch((error)=>{
            // handle error
            console.log(error);
        });
        console.log(this.state.pageNo)
    }

    //创建功能点
    handleSubmit(e) {

        e.preventDefault();
        this.field.validate((errors, values) => {
            if (errors) {
                console.log("Errors in form!!!");
                return;
            }
            //检测重复的url以及参数
            let byNameUrl=API.permission+"/v1/permissions/byName"
            let byNameParams=
                {name:values.name}

            //创建的url
            let url= API.permission + "/v1/permissions";
            let params=
                {
                    description: values.description,
                    isPrivate: values.isPrivate,
                    name:values.name,
                    parentId: 0,
                }


             //先检测是否重复
                Axios.get(byNameUrl,{params:(byNameParams)}
                ).then((response) => {
                    // handle success
                    console.log("监测重复返回的东西："+response.data.name);
                    if(response.data.name==values.name)
                    {
                        Feedback.toast.error("功能点名称重复！")
                    }
                    else
                    {
                        this.setState({
                        visible: false
                    });
                        Axios.post(url, {},{params:(params)}
                        )
                            .then((response)=>{
                                console.log("创建时的response："+response)
                                this.componentDidMount()

                            }).catch((error)=> {
                            console.log(error);
                        });

                    }
                }).catch((error)=>{
                    // handle error
                    console.log(error);
                }).then(()=>{
                    // always executed
                });
        });
    }

    //确认删除操作以及
    //删除功能点
    onConfirm = id => {

        const { dataSource } = this.state;
        let url = API.permission + "/v1/permissions/{id}" ;
        let params= {id:id}
        Axios.delete(url,{params:(params)}
        )
            .then((response)=>{
                Feedback.toast.success('成功删除！')
                this.onChange(this.state.currentPage)
            }).catch((error)=> {
            console.log(error);
        });
    };

    //功能点的搜索功能
    onSearchChange=key=>{
        this.setState({isLoading:true})
        let url=API.permission+"/v1/permissions"
        let params={
            pageNo:1,
            pageSize:this.state.permissionPageSize,
            key:key
        }
        Axios.get(url,{params:(params)}).then(response=>{
            this.setState({
                currentData:response.data.pageList,
                pageNo:response.data.pageNo,
                totalCount:response.data.totalCount,
                isLoading:false
            })
        })
    }
    render() {
        const { init, getError, getState } = this.field;
        const { Item } = Nav;
        const dialogStyle= {
            width: "50%" ,height:"7000"
        }
        //form样式定义
        const formItemLayout = {
            labelCol: {
                span: 6
            },
            wrapperCol: {
                span: 14
            }
        };
        //窗口按钮定义
        const footer = (
            <a onClick={this.onClose} href="javascript:">
                取消
            </a>
        );

        //删除操作定义
        const renderDelete = (value, index, record) => {
            return (
                <BalloonConfirm
                    onConfirm={this.onConfirm.bind(this, record.id)}
                    onCancel={this.onCancel}
                    title="您真的要删除吗？"
                >
                    <Button
                        type="primary"
                        shape="warning"
                        size="medium"
                        className="button">删除</Button>
                </BalloonConfirm>

            );
        }
        return (
        <div>
            <Button type="primary"
                    className="topButton"
                    onClick={this.onOpen} >创建功能点</Button>
            <Dialog
                title="创建功能点"
                visible={this.state.visible}
                onClose={this.onClose}
                style={dialogStyle}
                minMargin={5}
                footer={footer}
                shouldUpdatePosition={true}>

                <Form field={this.field}>

                    <FormItem
                        label="功能点名称："
                        {...formItemLayout}
                        hasFeedback
                        help={
                            getState("name") === "validating"
                                ? "校验中..."
                                : (getError("name") || []).join(", ")
                        }
                    >
                        <Input
                            maxLength={10}
                            hasLimitHint
                            placeholder="请输入名称"
                            {...init("name", {
                                rules: [
                                    { required: true, min: 1, message: "名称不能为空！" },
                                    //检验名称是否重复
                                ]
                            })}
                        />
                    </FormItem>

                    <FormItem label="是否私有：" hasFeedback {...formItemLayout}>
                        <RadioGroup
                            {...init("isPrivate", {
                                rules: [{ required: true, message: "请定义是否私有" }]
                            })}
                        >
                            <Radio value="0">公有</Radio>
                            <Radio value="1">私有</Radio>
                        </RadioGroup>
                    </FormItem>
                    <FormItem label="功能点描述：" {...formItemLayout}>
                        <Input
                            multiple
                            maxLength={30}
                            hasLimitHint
                            placeholder="请描述该功能点具体内容"
                            {...init("description", {
                                rules: [{ required: true, message: "真的不打算写点什么吗？" }]
                            })}
                        />
                    </FormItem>
                    <FormItem label="父功能点ID：" {...formItemLayout} required>
                        <Select style={{ width: 200 }} {...init("parentId")}>
                        </Select>
                    </FormItem>
                    <FormItem wrapperCol={{ offset: 6 }}>
                        <Button type="primary" onClick={this.handleSubmit.bind(this)}>
                            确定
                        </Button>
                        &nbsp;&nbsp;&nbsp;
                        <Button onClick={this.handleReset.bind(this)}>重置</Button>
                    </FormItem>

                </Form>
            </Dialog>

            <Search
                className="search"
                onChange={this.onSearchChange.bind(this)}
                dataSource={this.state.dataSource}
                placeholder="输入功能点名称搜索"
                hasIcon={false}
                autoWidth
            />
            <Table dataSource={this.state.currentData}
                   isLoading={this.state.isLoading}
                   hasBorder={false}>
                <Table.Column title="功能点ID" dataIndex="id" width="10%"/>
                <Table.Column title="功能点名称" dataIndex="name"/>
                <Table.Column title="功能点描述" dataIndex="description"/>
                <Table.Column title="创建人" dataIndex="userName"/>
                <Table.Column title="创建时间" dataIndex="ctime"/>
                <Table.Column title="删除操作" cell={renderDelete} width="10%" />

            </Table>
            <Pagination total={this.state.totalCount}
                        current={this.state.pageNo}
                        onChange={this.onChange}
                        pageSize={this.state.pageSize}
            className="pagination" />
        </div>

        );
    }
}
