import React from 'react';
import NavBar from './NavBar'
import { Table } from '@icedesign/base';


export default class AllProjects extends React.Component {

    onRowClick = function(record, index, e) {
        console.log(record, index, e);
    };
    getData = () => {
        let result = [];
        for (let i = 0; i < 5; i++) {
            result.push({
                title: {
                    name: `Quotation for 1PCS Nano ${3 + i}.0 controller compatible`
                },
                id: 100306660940 + i,
                time: 2000 + i
            });
        }
        return result;
    };
    cellRender = (value, index, record) => {
        return <a>Remove({record.id})</a>;
    };


    render(){

        return (
            <div>
                <NavBar/>
                <div>
                    <Table dataSource={this.getData()} onRowClick={this.onRowClick} sortable={true}>
                        <Table.Column title="Id" dataIndex="id" />
                        <Table.Column title="Title" dataIndex="title.name" />
                        <Table.Column title="Time" dataIndex="time" />
                        <Table.Column cell={this.cellRender} width="40%" />
                    </Table>
                </div>
            </div>
        );


    }

}







