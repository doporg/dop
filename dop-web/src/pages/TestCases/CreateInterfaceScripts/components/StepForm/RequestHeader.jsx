import React, { Component } from 'react';
import {
    Input,
    Button,
    Grid, Icon,
} from '@icedesign/base';

import {  FormBinder, FormError } from '@icedesign/form-binder';

const { Row, Col } = Grid;

const styles = {
    formItem: {
        marginBottom: '20px',
        display: 'flex',
        alignItems: 'center',
    },
    formItemLabel: {
        width: '70px',
        marginRight: '10px',
        display: 'inline-block',
        textAlign: 'right',
    },
    formItemError: {
        marginLeft: '10px',
    },
    preview: {
        border: '1px solid #eee',
        marginTop: 20,
        padding: 10
    }
};

export default class RequestHeader extends Component{

    render() {
        return (
            <div>
                {this.props.requestHeaders.map((requestHeader, index) => {
                    return (
                        <Row key={index} style={{marginBottom: '10px'}}>
                            <Col>
                                <span style={styles.formItemLabel}>名称：</span>
                                <FormBinder required message="请求头名称必填" name={`requestHeaders[${index}].name`} >
                                    <Input />
                                </FormBinder>
                                <FormError name={`requestHeaders[${index}].name`} />
                            </Col>
                            &nbsp;
                            <Col>
                                <span style={styles.formItemLabel}>值：</span>
                                <FormBinder name={`requestHeaders[${index}].value`} required message="请输入请求头值" >
                                    <Input/>
                                </FormBinder>
                                <FormError name={`requestHeaders[${index}].value`} />
                            </Col>
                            <Col>
                                <Button type="secondary" onClick={this.props.removeItem.bind(this, index)}>
                                    <Icon type="ashbin" size='xxxl'/>
                                </Button>
                            </Col>
                        </Row>
                    );
                })}
                <div style={styles.buttons}>
                    <Button type="secondary" onClick={this.props.addItem}>
                        <Icon type="add" size='xxl'/>
                    </Button>
                </div>
            </div>
        );
    }
}
