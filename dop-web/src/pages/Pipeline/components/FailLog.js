import React, {Component} from 'react';
import './Styles.scss'
import {Icon} from "@icedesign/base";


export default class FailLog extends Component {
    constructor(props) {
        super(props)
        this.state = {
            content: false
        }
    }

    clickTitle() {
        this.setState({
            content: !this.state.content
        })
    }

    render() {
        return (
            <div className="accordion">
                <div className="title" onClick={this.clickTitle.bind(this)}>
                    <div className="div-icon fail">
                        <Icon type="close" className="icon" size="xxs"/>
                    </div>
                    {
                        this.state.content ?
                            <div className="div-icon2">
                                <Icon type="arrow-down" className="icon" size="xs"/>
                            </div> :
                            <div className="div-icon2">
                                <Icon type="arrow-right" className="icon" size="xs"/>
                            </div>
                    }


                    <div className="process">title</div>
                    <div className="prop">--PrintMessage</div>
                </div>

                {this.state.content ?
                    (<div className="content">
                        <pre className="pre">
                            <ul className="ol">
                                <li className="li">
                                    <span className="idx">1</span>
                                    <span className="shell">
                                        content
                                    </span>
                                </li>
                            </ul>
                        </pre>
                    </div>) :null
                }

            </div>
        )
    }
}
