import React, { Component } from 'react';
import IceContainer from '@icedesign/container';
import './NotPermission.scss';

export default class NotPermission extends Component {
    static displayName = 'NotPermission';

    constructor(props) {
        super(props);
        this.state = {};
    }

    render() {
        return (
            <div className="not-permission" style={styles.notPermission}>
                <IceContainer>
                    <div style={styles.content} className="exception-content">
                        <img
                            src={require('./images/notfound.png')}
                            style={styles.imgException}
                            className="imgException"
                            alt="prmission"
                        />
                        <div style={styles.prompt}>
                            <h3 style={styles.title} className="title">
                                抱歉，您尚未运行该流水线～
                            </h3>
                            <p style={styles.description} className="description">
                                抱歉，您尚未运行该流水线, 请点击运行～
                            </p>
                        </div>
                    </div>
                </IceContainer>
            </div>
        );
    }
}

const styles = {
    content: {
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
    },
    title: {
        color: '#333',
    },
    description: {
        color: '#666',
    },
};
