import
{
    Table,
    Input,
    Form,
    Field,
    Button,
    Select,
    Feedback,
    NumberPicker, Dialog
} from "@icedesign/base";
import React, {Component} from 'react';
import Axios from "axios";
import API from "../../../../API";

const FormItem = Form.Item;
const Option = Select.Option;
const Toast = Feedback.toast;
const formItemLayout = {
    labelCol: {span: 8},
    wrapperCol: {span: 16}
};

/**
 * 展示应用环境详情的列表（仅编辑界面）
 * @author Bowen
 **/
export default class ApplicationEnvironmentDetail extends Component {

    constructor(props) {
        super(props);
        this.field = new Field(this);
        console.log(props)
        this.state = {
            id: props.id,
            editMode: true,
            envDetailData: []
        }
    }

    getEnvDetailData() {
        let url = API.gateway + "/application-server/application/environment/detail/" + this.state.id;
        Axios.get(url)
            .then((response) => {
                this.setState({
                    envDetailData: response.data
                })
            })
    }

    envDetailConfirm = () => {
        Dialog.confirm({
            content: "你确定要保存修改吗？",
            title: "确认修改",
            onOk: this.testKubernetes.bind(this)
        });
    };

    testKubernetes = () => {
        let url = API.gateway + "/application-server/application/environment/detail/cluster"
        Axios.get(url).then((response) => {
            console.log(response)
        })
            .catch((response) => {
                console.log(response)
            })

    }


    envDetailSubmit() {
        let _this = this;

        //校验输入
        this.field.validate((errors, values) => {

            console.log(errors, values);

            // 没有异常则提交表单
            if (errors == null) {
                console.log("noerros");
                let url = API.gateway + '/application-server/application/environment/detail/' + this.state.id;
                Axios.put(url, {}, {
                        params: {
                            deploymentStrategy: this.field.getValue('deploymentStrategy'),
                            targetCluster: this.field.getValue('targetCluster'),
                            nameSpace: this.field.getValue('nameSpace'),
                            service: this.field.getValue('service'),
                            releaseBatch: this.field.getValue('releaseBatch')
                        }
                    }
                )
                    .then(function (response) {
                        Toast.success("更新成功！")

                        //提交完成后刷新当前页面
                        let url = API.gateway + '/application-server/application/environment/detail/' + this.state.id;
                        Axios.get(url)
                            .then(function (response) {
                                console.log(response)
                                _this.setState({
                                    appBasicData: response.data,
                                    basicEditMode: false
                                })
                            })
                    })
                    .catch(function (error) {
                        console.log(error);
                    });

            }
        });

    }


    componentDidMount() {
        this.getEnvDetailData();
    }

    formRender() {
        if (this.state.editMode) {
            const {init, getValue} = this.field
            return (<Form>

                <FormItem label="部署方式"
                          {...formItemLayout}
                          validateStatus={this.field.getError("deploymentStrategy") ? "error" : ""}
                          help={this.field.getError("deploymentStrategy") ? "请选择部署方式" : ""}>
                    <Select placeholder="部署方式"
                            {...init('deploymentStrategy', {rules: [{required: true, message: "该项不能为空"}]})}>
                        <Option value="KUBERNETES">Kubernetes部署</Option>
                    </Select>
                </FormItem>

                <FormItem label="目标集群"
                          {...formItemLayout}
                          validateStatus={this.field.getError("targetCluster") ? "error" : ""}
                          help={this.field.getError("targetCluster") ? "请输入目标集群" : ""}>
                    <Input placeholder="目标集群"
                           {...init('targetCluster', {rules: [{required: true, message: "该项不能为空"}]})}>
                    </Input>
                </FormItem>

                <FormItem label="命名空间"
                          {...formItemLayout}
                          validateStatus={this.field.getError("nameSpace") ? "error" : ""}
                          help={this.field.getError("nameSpace") ? "请选择命名空间" : ""}>
                    <Input placeholder="命名空间"
                           {...init('nameSpace', {rules: [{required: true, message: "该项不能为空"}]})}>
                    </Input>
                </FormItem>

                <FormItem label="服务"
                          {...formItemLayout}
                          validateStatus={this.field.getError("service") ? "error" : ""}
                          help={this.field.getError("service") ? "服务" : ""}>
                    <Input placeholder="服务"
                           {...init('service', {rules: [{required: true, message: "该项不能为空"}]})}>
                    </Input>
                </FormItem>

                <FormItem label="发布策略"
                          {...formItemLayout}
                          validateStatus={this.field.getError("releaseStrategy") ? "error" : ""}
                          help={this.field.getError("releaseStrategy") ? "发布策略" : ""}>
                    <Select placeholder="发布策略"
                            {...init('releaseStrategy', {rules: [{required: true, message: "该项不能为空"}]})}>
                        <Option value="BATCH">分批发布</Option>
                        <Option value="BLUE_GREEN">蓝绿发布</Option>
                        <Option value="ROLLING_UPDATE">滚动升级</Option>
                    </Select>
                </FormItem>

                <FormItem style={{display: this.field.getValue('releaseStrategy') == 'BATCH' ? "" : "None"}}
                          label="发布批次"
                          {...formItemLayout}
                          validateStatus={this.field.getError("releaseBatch") ? "error" : ""}
                          help={this.field.getError("releaseBatch") ? "发布策略" : ""}>
                    <NumberPicker min={1}
                                  max={99}
                                  placeholder="发布批次"
                                  defaultValue={0}
                                  {...init('releaseBatch')}/>
                </FormItem>

                <Button onClick={this.envDetailConfirm.bind(this)}
                        type="primary"
                        style={{marginRight: "5px"}}>
                    保存
                </Button>

                < Button> 取消 </Button>

            </Form>)

        }
    }

    render() {
        const {init, getValue} = this.field;
        return (
            <div>
                {this.formRender()}
            </div>
        );

    }


}