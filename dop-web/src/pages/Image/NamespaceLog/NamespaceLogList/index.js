import React,{Component} from 'react';
import {Input, Loading, Pagination, Table} from '@icedesign/base';
import IceContainer from '@icedesign/container';
import {Grid} from "@icedesign/base/index";
import API from "../../../API";
import Axios from "axios/index";
import {injectIntl} from "react-intl";

const {Row} = Grid;
class NamespaceLogList extends Component{
    static displayName = "NamespaceLogList";

    constructor(props) {
        super(props);
        this.state = {
            currentData: [],
            current:1,
            totalCount:0,
            pageSize:10,
            loading: true,
            id: this.props.projectId,
            queryKey:""
        };

    }

    refreshList(current,queryKey){
        let url = API.image+"/v1/projects/"+this.state.id+"/logs"
        let _this = this;
        if(queryKey!==""){
            Axios.get(url, {
                params:{
                    page:current,
                    pageSize: this.state.pageSize,
                    username:queryKey
                }
            })
                .then(function (response) {
                    console.log("日志信息");
                    console.log(response.data);
                    if (response.data.totalCount!==0){
                        _this.setState({
                            currentData: response.data.pageList,
                            totalCount:response.data.totalCount,
                            loading:false
                        });
                    } else {
                        _this.setState({
                            currentData:[],
                            totalCount:0,
                            loading:false,
                        })
                    }
                })
        }else {
            Axios.get(url, {
                params:{
                    page:current,
                    pageSize: this.state.pageSize
                }
            })
                .then(function (response) {
                    console.log("日志信息");
                    console.log(response.data);
                    if (response.data.totalCount!==0){
                        _this.setState({
                            currentData: response.data.pageList,
                            totalCount:response.data.totalCount,
                            loading:false
                        });
                    } else {
                        _this.setState({
                            currentData:[],
                            totalCount:0,
                            loading:false,
                        })
                    }
                })
        }
    }

    onSearch(value){
        this.setState({
            queryKey:value
        });
        this.refreshList(this.state.current,value)
    }


    handleChange(current,e){
        this.setState({
            current:current
        });
        this.refreshList(current,this.state.queryKey)
    }


    componentWillMount() {
        this.refreshList(this.state.current,this.state.queryKey)
    }

    render() {
        return (
            <div>
                <IceContainer title={this.props.intl.messages["image.search"]}>
                    <Row wrap>
                        <Input placeholder={this.props.intl.messages["image.searchPlaceholder"]} onChange={this.onSearch.bind(this)}/>
                    </Row>
                </IceContainer>

                <IceContainer title={this.props.intl.messages["image.accessLog"]}>
                    <Loading visible={this.state.loading} shape="dot-circle" color="#2077FF">
                        <Table dataSource={this.state.currentData}
                               isLoading={this.state.isLoading}
                               primaryKey="logId"
                        >

                            <Table.Column title={this.props.intl.messages["image.logTable.user"]}
                                          dataIndex="username"/>

                            <Table.Column title={this.props.intl.messages["image.logTable.repoName"]}
                                          dataIndex="repoName"/>

                            <Table.Column title={this.props.intl.messages["image.logTable.tag"]}
                                          dataIndex="repoTag"/>
                            <Table.Column title={this.props.intl.messages["image.logTable.operation"]}
                                          dataIndex="operation"/>
                            <Table.Column title={this.props.intl.messages["image.logTable.time"]}
                                          dataIndex="opTime"/>
                        </Table>
                    </Loading>

                    <Pagination className={"body"}
                                current={this.state.current}
                                onChange={this.handleChange.bind(this)}
                                pageSize={this.state.pageSize}
                                total={this.state.totalCount}
                                hideOnlyOnePage={true}/>
                </IceContainer>
            </div>
        )
    };
}
export default injectIntl(NamespaceLogList)