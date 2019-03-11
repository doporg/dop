import React, { Component } from 'react';

import Axios from 'axios';
import API from '../API';
import {Button, Dialog, Field, Radio, Table} from "@icedesign/base";
import Pagination from "@icedesign/base/lib/pagination";
import Form from "@icedesign/base/lib/form";
import BalloonConfirm from "@icedesign/balloon-confirm";
import Input from "@icedesign/base/lib/input";
import Select, {Option} from "@icedesign/base/lib/select";


const { Item: FormItem } = Form;
/**
 *  角色管理
 *  @author lizhiying
 *
 *
 * */



export default class Role extends Component {


    constructor(props) {
        super(props);
        this.field = new Field(this);
        this.state = {
            Dialogstyle: {
                width: "50%" ,height:"7000"
            },
            currentData : [],
            visible:false,
            deletevisible:false,
            InputInfo:
                {
                    parentId:"",
                    name:"",
                },
            pageNo:1,
            pageSize:8,
            totalCount:0
        };

    }
    //每次访问的刷新
    componentDidMount() {
        let url = API.permission + "/v1/roles" ;
        let params=
            {
                pageNo:this.state.pageNo,
                pageSize:this.state.pageSize
            }
        Axios.get(url,{params:(params)}).then((response) => {
            this.setState({
                currentData:response.data.pageList,
                pageNo:response.data.pageNo,
                totalCount:response.data.totalCount})
            // Axios.all(this.getcusername(response.data.pageList)).then(()=>{
            //     this.setState({currentData:response.data.pageList})
            // })
        }).catch((error)=>{
            // handle error
            console.log(error);
        }).then(()=>{
            // always executed
        });
    }

    //重置
    handleReset(e) {
        e.preventDefault();
        this.field.reset();
    }
    //提交
    handleSubmit(e)
    {

    }
    //弹出创建角色窗
    onOpen = () => {
        this.setState({
            visible: true
        });
    };
    //关闭创建角色弹窗
    onClose = reason => {
        console.log(reason);
        this.setState({
            visible: false
        });
    };
    render() {
        const { init, getError, getState } = this.field;
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
            <a onClick={this.onClose} href="javascript:;">
                取消
            </a>
        );

        //删除操作定义
        const renderdelete = (value, index, record) => {
            return (
                <BalloonConfirm
                    // onConfirm={this.onConfirm.bind(this, record.id)}
                    // onCancel={this.onCancel}
                    // title="您真的要删除吗？"
                >
                    <button >删除</button>
                </BalloonConfirm>

            );
        }
        return(
            <div>
                <Button onClick={this.onOpen} >创建新角色</Button>

                <Dialog
                    title="创建角色"
                    visible={this.state.visible}
                    onClose={this.onClose}
                    style={this.state.Dialogstyle}
                    minMargin={5}
                    footer={footer}>

                    <Form field={this.field}>

                        <FormItem
                            label="角色名称："
                            {...formItemLayout}
                            hasFeedback
                        >
                            <Input
                                maxLength={10}
                                hasLimitHint
                                placeholder="请输入名称"
                                {...init("name", {
                                    rules: [
                                        { required: true, min: 1, message: "名称不能为空！" },
                                    ]
                                })}
                            />
                        </FormItem>

                        <FormItem label="父角色：" {...formItemLayout} required>
                            <Select style={{ width: 200 }} {...init("parentId")}>
                                <Option value="1">1</Option>
                                <Option value="2">2</Option>

                                <Option value="3">3</Option>
                                <Option value="disabled" disabled>
                                    disabled
                                </Option>
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



                {/*<Button onClick={}>查询所有功能点</Button>*/}

                <Table dataSource={this.state.currentData}>
                    <Table.Column title="角色名称" dataIndex="name"/>
                    <Table.Column title="最后修改人"   dataIndex="muser"/>
                    <Table.Column title="最后修改时间" dataIndex="mtime"/>
                    <Table.Column title="删除操作" cell={renderdelete} width="20%" />

                </Table>
                <Pagination total={this.state.totalCount}
                            current={this.state.pageNo}
                            onChange={this.onChange}
                            pageSize={this.state.pageSize
                            }
                            className="page-demo" />
            </div>
        )
    }
}
