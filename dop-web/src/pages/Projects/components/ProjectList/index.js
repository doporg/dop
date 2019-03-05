/**
 * 展示项目列表
 * @author Bowen
 **/

import React, {Component} from 'react';
import {Table} from '@icedesign/base';
import {Grid} from '@icedesign/base';

import API from '../../../API';


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

// async function getProjectData (current){
//
//     let url = API.application + '/projects';
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
//             console.log("getproject");
//
//
//         })
//         .catch(function (error) {
//             console.log(error);
//         });
// }

//项目展示列表
export default class ProjectList extends Component {

    static displayName = 'ProjectList';


    constructor(props) {
        super(props);

        console.log(props.currentData)
        //接受来自分页器的参数即当前页数据
        this.state = {
            currentData: props.currentData
        };
        console.log("constructor");

    }

    // componentDidMount() {
    //
    //     let url = API.application + '/projects';
    //     let _this = this;
    //     Axios.get(url, {
    //         params: {
    //             pageNo: this.state.current,
    //             pageSize: 9,
    //             includeFinished: false
    //         }
    //     })
    //         .then(function (response){
    //             _this.setState({
    //                 currentData:response.data
    //             })
    //         })
    //         .catch(function (error) {
    //             console.log(error);
    //         });
    // }

    componentWillReceiveProps(nextProps, nextContext) {
        let url = API.application + '/projects';
        this.setState({
            currentData: nextProps.currentData
        });
    }


    render() {
        return (
            <Row wrap gutter="20">
                <Table dataSource={this.state.currentData}>
                    <Table.Column title="项目名称" dataIndex="title"/>
                    <Table.Column title="创建人" dataIndex="cuser"/>
                    <Table.Column title="创建时间" dataIndex="ctime"/>
                </Table>
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
