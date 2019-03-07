import React, {Component} from 'react';
import {Table} from '@icedesign/base';
import {Grid} from '@icedesign/base';
import {Icon} from '@icedesign/base';
import API from "../../../../API.js"
import {Col} from "@alifd/next/lib/grid";


const {Row} = Grid;


// MOCK 数据，实际业务按需进行替换
// function getData() {
//     return Array.from({length: 5}).map((item, index) => {
//         return {
//             appid: `0000${index + 1}`,
//             title: 'NJU',
//             desc: 'Devops program',
//             time: '2018-12-20 14:30:19',
//         };
//     });
// };

// async function getApplicationData (current){
//
//     let url = API.application + '/applications';
//     let data = await Axios.get(url, {
//         params: {
//             pageNo: current,
//             pageSize: 9,
//             includeFinished: false
//         }
//     })
//         .then(function (response){
//            return response.data
//
//             console.log("getApplication");
//
//
//         })
//         .catch(function (error) {
//             console.log(error);
//         });
// }

/**
 * 展示应用的列表
 * @author Bowen
 **/

export default class ApplicationList extends Component {

    static displayName = 'ApplicationList';


    constructor(props) {
        super(props);

        console.log(props.currentData)
        //接受来自分页器的参数即当前页数据
        this.state = {
            currentData: props.currentData
        };
        console.log("constructor");

    }

    componentWillReceiveProps(nextProps, nextContext) {

        for (let i = 0; i < nextProps.currentData.length; i++) {
            let tmpTime = nextProps.currentData[i].ctime;
            if (tmpTime[0].length < 4) break;
            nextProps.currentData[i].ctime = tmpTime[0] + "/" + tmpTime[1] + "/" + tmpTime[2] + " " + tmpTime[3] + ":" + tmpTime[4];
        }
        this.setState({
            currentData: nextProps.currentData
        });
    }

// titleRender= function(title){
//         return <a href = {"http://localhost:3000/ApplicationList?ApplicationId="+String(title)}>{title}</a>
// }
    render() {
        return (
            <Row wrap gutter="20">
                <Col>
                    <Table dataSource={this.state.currentData}>
                        <Table.Column title="ID" dataIndex="id"/>
                        <Table.Column title="应用名称" dataIndex="title"/>
                        <Table.Column title="拥有者" dataIndex="ouser"/>
                        <Table.Column title="创建时间" dataIndex="ctime"/>
                        <Table.Column title="应用描述" dataIndex="description"/>
                    </Table>
                </Col>
            </Row>
        );
    }

}

const styles = {
    pagination: {
        position: 'relative',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
    },
    time: {
        fontSize: '12px',
        color: 'rgba(0, 0, 0, 0.5)',
    },
};
