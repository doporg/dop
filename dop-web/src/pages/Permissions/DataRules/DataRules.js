/**
 *  数据规则
 *  @author lizhiying
 *
 * */
import React, { Component } from 'react';
import {Input,Button, Field, Table, Dialog, Feedback,Form} from "@icedesign/base";
import Pagination from "@icedesign/base/lib/pagination";
import API from "../../API";
import Axios from "axios";
import BalloonConfirm from "@icedesign/balloon-confirm";
import {Link} from "react-router-dom";
import Select from "@icedesign/base/lib/select";
const { Item: FormItem } = Form;


let roleMap=new Map()
export default class DataRules extends Component
{
    constructor(props) {
        super(props);
        this.field = new Field(this);
        this.dataField=new Field(this);
        this.state = {
            currentData : [],
            roleList:[],
            currentRuleId:0,
            userList:[{id:1,name:"测试用户1",email:"xxx@xxx.com",mtime:"xxxx/xx/xx- xx:xx:xx"},
                {id:2,name:"测试用户2",email:"xxx@xxx.com",mtime:"xxxx/xx/xx- xx:xx:xx"},
                {id:3,name:"测试用户3",email:"xxx@xxx.com",mtime:"xxxx/xx/xx- xx:xx:xx"},
                {id:22,name:"lzy",email:"552000264@qq.com",mtime:"xxxx/xx/xx- xx:xx:xx"}],

            userSelectList:[],
            isLoading:true,
            ruleVisible:false,
            dataVisible:false,
            deleteVisible:false,
            pageNo:1,
            pageSize:8,
            totalCount:0,

            rowSelection: {
                onChange: this.onSelectChange.bind(this),
                mode:"single",
                selectedRowKey: 0,

            }
        };

    }

    //每次访问的刷新
    componentDidMount() {
        this.setState({isLoading:true})
        let getRoleUrl=API.permission+"/v1/roles/roles";
            let url = API.permission + "/v1/userRules" ;
            let params=
                {
                    pageNo:this.state.pageNo,
                    pageSize:this.state.pageSize
                }
            Axios.get(url,{params:(params)}).then((response) => {
                console.log(response.data.pageList)
                this.setState({
                    pageNo:response.data.pageNo,
                    totalCount:response.data.totalCount,
                    currentData:response.data.pageList,
                    isLoading:false
                })
            }).catch((error)=>{
                // handle error
                console.log(error);
            }).then(()=>{
                // always executed
            });

    }
    //翻页
    onChange=currentPage=> {
        this.setState({isLoading:true})
        let url = API.permission + "/v1/userRules" ;
        let params=
            {
                pageNo:currentPage,
                pageSize:this.state.pageSize
            }
        // console.log(params)
        Axios.get(url,{params:(params)}).then((response) => {
            // handle success
            this.setState({
                pageNo:response.data.pageNo,
                totalCount:response.data.totalCount,
                currentData:response.data.pageList,
                isLoading:false}
            );
        }).catch((error)=>{
            // handle error
            console.log(error);
        });
        console.log(this.state.pageNo)
    }


    //选择角色
    onSelectChange(role) {
        let { rowSelection } = this.state;
        rowSelection.selectedRowKey = role;
        console.log("onSelectChange", role);
        this.setState({ rowSelection });
    }
    //重置
    handleReset(e) {
        e.preventDefault();
        this.field.reset();
    }
    //提交
    handleSubmit(e)
    {
        e.preventDefault();
        this.field.validate((errors, values)=>{
            if(errors){
                console.log("Errors in form!!!");
                return;}
            console.log(values)
            let roleId=this.state.rowSelection.selectedRowKey[0]
            console.log(roleId)

            let createUserRuleUrl=API.permission+"/v1/userRules"

            let RuleParams={fieldName: values.fieldName,rule:values.rule,roleId:roleId}

            this.setState({
                ruleVisible: false
            });
            Axios.post(createUserRuleUrl,{},{params:(RuleParams)})
                .then(response=>{
                    this.componentDidMount()
                }
                )
        })
    }

    //弹出创建数据规则窗
    onRuleOpen = () => {

        let url=API.permission+"/v1/roles/roles";
        Axios.get(url).then(response=>{

            this.setState({roleList:response.data})
        }).catch(error=>{
            console.log(error)
        })

        this.setState({
            ruleVisible: true
        });
    };
    //关闭创建数据规则弹窗
    onRuleClose = reason => {
        console.log(reason);
        this.setState({
            ruleVisible: false
        });
    };

    //确认删除操作以及
    //删除功能点
    onConfirm = id => {

        const { dataSource } = this.state;
        let url = API.permission + "/v1/userRules/{id}" ;
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
    //取消删除操作
    onCancel = () => {
        console.log('取消删除');
        Feedback.toast.error('删除已取消！')

    }

    //弹出添加用户数据窗口
    addUserData=record=>{
        console.log(record)
        //获取当前选择的规则ID并填入
        this.setState({currentRuleId:record.id})
        //将用户列表填入下拉选项中
        let tmpList=[]
        this.state.userList.forEach(item=>{
            tmpList.push({label:item.name,value:item.id})
        })
        this.setState({
            dataVisible: true,
            userSelectList:tmpList,
        });
        console.log(this.state.userSelectList)
    }
    //关闭添加用户数据窗口
    onDataClose = reason => {
        console.log(reason);
        this.setState({
            dataVisible: false
        });
    };

    //重置用户数据窗口
    handleResetData(e) {
        e.preventDefault();
        this.dataField.reset();
    }
    //提交用户数据
    handleSubmitData(e)
    {
        e.preventDefault();
        this.dataField.validate((errors, values)=>{
            if(errors){
                console.log("Errors in form!!!");
                return;}
            this.setState({dataVisible: false})
            let url=API.permission+"/v1/userData"
            let params={ruleId:this.state.currentRuleId,userId:values.userId,fieldValue:values.fieldValue}

            Axios.post(url,{},{params:(params)}).then(response=>{
                Feedback.toast.success("创建用户数据成功！")
            })

        })
    }
    render() {

        const { init } = this.field;
        const initData=this.dataField.init;
        const dialogStyle= {
            width: "60%" ,height:"70"
        }

        const formItemLayout = {
            labelCol: {
                span: 6
            },
            wrapperCol: {
                span: 14
            }
        };
        const footer = (
            <a onClick={this.onRuleClose} href="javascript:">
                取消
            </a>
        );
        const footer2 = (
            <a onClick={this.onDataClose} href="javascript:">
                取消
            </a>
        );

        //删除操作定义
        const deleteRule = (value, index, record) => {
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

        //添加用户数据
        const addUserData = (value, index, record) => {
            return (
                <Button
                    type="primary"
                    shape="normal"
                    size="medium"
                    className="button"
                    onClick={this.addUserData.bind(this,record)}>添加</Button>
            );
        }
        return (
        <div>
            <Button type="primary"
                    className="topButton"
                    onClick={this.onRuleOpen} >创建数据规则</Button>

            <Link to='/permission/roles/userwithrole'>
                <Button type="primary"
                        className="topButton"
                >查看用户权限</Button>
            </Link>
            <Dialog
                title="创建数据规则"
                visible={this.state.ruleVisible}
                onClose={this.onRuleClose}
                style={dialogStyle}
                minMargin={5}
                footer={footer}
                shouldUpdatePosition={true}>

                <Form field={this.field}>
                    <FormItem
                        label="作用域参数名称："
                        {...formItemLayout}
                        hasFeedback
                    >
                        <Input
                            maxLength={30}
                            hasLimitHint
                            placeholder="请输入名称"
                            {...init("fieldName", {
                                rules: [
                                    { required: true, min: 1, message: "名称不能为空！" },
                                ]
                            })}
                        />
                    </FormItem>

                    <FormItem label="规则：" {...formItemLayout} required>
                        <Input
                            maxLength={10}
                            hasLimitHint
                            placeholder="请输入规则"
                            {...init("rule", {
                                rules: [
                                    { required: true, min: 1, message: "请输入规则！" },
                                ]
                            })}
                        />
                    </FormItem>

                    <FormItem label="对应角色：" {...formItemLayout} required>
                        <Table
                            hasBorder={false}
                            dataSource={this.state.roleList}
                            primaryKey="id"
                            isTree
                            rowSelection={this.state.rowSelection}
                        >
                            <Table.Column title="角色ID" dataIndex="id" />
                            <Table.Column title="角色名称" dataIndex="name" />
                        </Table>
                    </FormItem>

                    <FormItem wrapperCol={{ offset: 6 }}>
                        <Button type="primary" onClick={this.handleSubmit.bind(this)}>
                            确定
                        </Button>
                        &nbsp;&nbsp;&nbsp;
                        <Button onClick={this.handleSubmit.bind(this)}>重置</Button>
                    </FormItem>

                </Form>

            </Dialog>

            <Dialog
                title="添加用户数据"
                visible={this.state.dataVisible}
                onClose={this.onDataClose}
                style={dialogStyle}
                minMargin={5}
                footer={footer2}
                shouldUpdatePosition={true}
            >
                <Form field={this.dataField}>
                    <FormItem label="用户：" {...formItemLayout} required>
                        <Select dataSource={this.state.userSelectList}
                                {...initData("userId")}>

                        </Select>
                    </FormItem>

                    <FormItem label="作用域参数值：" {...formItemLayout} required>
                        <Input
                            maxLength={10}
                            hasLimitHint
                            placeholder="请输入参数值(如想为projectId=1的项目添加，则输入1)"
                            {...initData("fieldValue", {
                                rules: [
                                    { required: true, min: 1, message: "请输入参数值！" },
                                ]
                            })}
                        />
                    </FormItem>

                    <FormItem wrapperCol={{ offset: 6 }}>
                        <Button type="primary" onClick={this.handleSubmitData.bind(this)}>
                            确定
                        </Button>
                        &nbsp;&nbsp;&nbsp;
                        <Button onClick={this.handleResetData.bind(this)}>重置</Button>
                    </FormItem>

                </Form>
            </Dialog>

            <Table
                hasBorder={false}
                isLoading={this.state.isLoading}
                dataSource={this.state.currentData}>
                <Table.Column title="规则ID" dataIndex="id" width="10%"/>
                <Table.Column title="角色名称"   dataIndex="roleName" width="10%"/>
                <Table.Column title="权限作用域参数名" dataIndex="fieldName" width="15%"/>
                <Table.Column title="规则" dataIndex="rule" width="10%"/>
                <Table.Column title="规则描述" dataIndex="description" />
                <Table.Column title="添加用户数据" cell={addUserData}  width="10%" />
                <Table.Column title="删除操作" cell={deleteRule}  width="10%" />


            </Table>
            <Pagination total={this.state.totalCount}
                        current={this.state.pageNo}
                        onChange={this.onChange}
                        pageSize={this.state.pageSize
                        }
                        className="pagination" />
        </div>
        );
    }
}