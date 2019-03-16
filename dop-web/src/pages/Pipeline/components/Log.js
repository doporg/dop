import React, {Component} from 'react';
import './Styles.scss'
import {Icon} from "@icedesign/base";
import Axios from 'axios';
import API from '../../API'


export default class Log extends Component {
    constructor(props) {
        super(props)
        this.state = {
            content: false,
            count: 1,
            logs: []
        }
    }

    clickTitle(href) {
        this.setState({
            content: !this.state.content,
            count: this.state.count + 1
        });
        if (href && this.state.count % 2) {
            let url = API.jenkins + href;
            let self = this;
            Axios({
                method: 'get',
                url: url,
                headers: {
                    "Authorization": self.props.authorization
                }
            }).then((response) => {
                if (response.status === 200) {
                    console.log(response.data)
                    let logs =  response.data.split('\n');
                    logs.pop();
                    self.setState({
                        logs
                    })
                }
            })
        }

    }

    render() {
        return (
            <div className="accordion">
                <div className="title" onClick={this.clickTitle.bind(this, this.props.href)}>
                    {(() => {
                        if (this.props.result === 'SUCCESS') {
                            return (
                                <div className="div-icon success">
                                    <Icon type="select" className="icon" size="xxs"/>
                                </div>
                            )
                        } else if (this.props.result === 'FAILURE') {
                            return (
                                <div className="div-icon fail">
                                    <Icon type="close" className="icon" size="xxs"/>
                                </div>
                            )
                        } else {
                            return (
                                <div className="div-icon loading">
                                    <Icon type="loading" className="icon" size="xxs"/>
                                </div>
                            )
                        }
                    })()}

                    {
                        this.state.content ?
                            <div className="div-icon2">
                                <Icon type="arrow-down" className="icon" size="xs"/>
                            </div> :
                            <div className="div-icon2">
                                <Icon type="arrow-right" className="icon" size="xs"/>
                            </div>
                    }


                    <div className="process">{this.props.title}</div>
                    <div className="prop">{this.props.prop ? "--" + this.props.prop : null}</div>
                </div>

                {this.state.content ?
                    (<div className="content">
                        <pre className="pre">
                            <ul className="ol">
                                <li className="li">
                                    {this.state.logs.map((log, index) => {
                                        return (
                                            <div key={index} className="li-wrap">
                                                <span className="idx" >{index}</span>
                                                <span className="shell" >
                                                    {log}
                                                </span>
                                            </div>
                                        )
                                    })}

                                </li>
                            </ul>
                        </pre>
                    </div>) : null
                }

            </div>
        )
    }
}
