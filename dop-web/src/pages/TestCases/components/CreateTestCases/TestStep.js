import React, { Component } from 'react';
import {  FormBinder, FormError } from '@icedesign/form-binder';
import {
    Button, Grid, Input
}
    from "@icedesign/base";
import IceContainer from '@icedesign/container';

const { Row, Col } = Grid;

export class TestSteps extends Component {
    render() {
        return (
            <IceContainer>
                {this.props.testSteps.map((testStep, index) => {
                    return (
                        <Row key={index} >
                            <Col>
                                <FormBinder required message="测试步骤必填" name={`testSteps[${index}].stepDesc`} >
                                    <Input multiple placeholder={'测试步骤'+(index+1)} style={{width: '100%'}}/>
                                    {/*<TextArea />*/}
                                </FormBinder>
                                <FormError name={`testSteps[${index}].stepDesc`} style={styles.formError} />
                            </Col>
                            &nbsp;
                            <Col>
                                <FormBinder name={`testSteps[${index}].expectResult`} required message="请输入预期结果" >
                                    <Input multiple placeholder="预期结果" style={{width: '100%'}}/>
                                    {/*<TextArea/>*/}
                                </FormBinder>
                                <FormError name={`testSteps[${index}].expectResult`} style={styles.formError} />
                            </Col>
                            <Button type="secondary" style={{margin: 'auto', marginLeft: '10px'}} onClick={this.props.removeItem.bind(this, index)}>删除</Button>
                        </Row>
                    );
                })}
                <div style={styles.buttons}>
                    <Button type="secondary" onClick={this.props.addItem}>添加测试步骤</Button>
                </div>
            </IceContainer>
        );
    }
}

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
