/* eslint no-undef:0, no-unused-expressions:0, array-callback-return:0 */
import React, { Component } from 'react';
import Menu, { Item as MenuItem } from '@icedesign/menu';
import { withRouter, Link } from 'react-router-dom';
import FoundationSymbol from 'foundation-symbol';
import { codeAsideMenuConfig as asideMenuConfig } from './codeMenuConfig';
import { Icon } from '@icedesign/base';

import './index.scss';

// @withRouter

class CodeAside extends Component {
  static propTypes = {};

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.state={};
  }

  render() {
    const { location } = this.props;
    const { pathname } = location;
    const { username,projectid } = this.props.match.params;
    //需要改成请求获得项目的默认分支名
    let branch = "master";
    // if(this.props.match.params.hasOwnProperty('branch')){
    //   branch=this.props.match.params.branch;
    // }

    const backLink="/code/projectlist";

    const projectLink="/code/"+username+"/"+projectid;
    const commitLink="/code/"+username+"/"+projectid+"/commitlist/"+branch;

    const fileLink="/code/"+username+"/"+projectid+"/tree/"+branch+"/"+encodeURIComponent("/");


    return (
        <Menu mode="inline" selectedKeys={[pathname]} className="ice-menu-custom">
            <MenuItem key={backLink}>
                <Link to={backLink} className="ice-menu-link">
                    <FoundationSymbol size="small" type="home2" >
                        <Icon size="small" type="home2" />
                    </FoundationSymbol>
                    <span className="ice-menu-item-text">{"首页"}</span>
                </Link>
            </MenuItem>
            <MenuItem key={projectLink}>
                <Link to={projectLink} className="ice-menu-link">
                        <FoundationSymbol size="small" type="directory" >
                            <Icon size="small" type="directory" />
                        </FoundationSymbol>
                    {/*<span className="ice-menu-item-text">{"项目"+username+" "+projectid}</span>*/}
                    <span className="ice-menu-item-text">{"项目"}</span>
                </Link>
            </MenuItem>
            <MenuItem key={fileLink}>
                <Link to={fileLink} className="ice-menu-link">
                    <FoundationSymbol size="small" type="copy" >
                        <Icon size="small" type="copy" />
                    </FoundationSymbol>
                    {/*<span className="ice-menu-item-text">{"项目"+username+" "+projectid}</span>*/}
                    <span className="ice-menu-item-text">{"文件"}</span>
                </Link>
            </MenuItem>
            <MenuItem key={commitLink}>
                <Link to={commitLink} className="ice-menu-link">
                    <FoundationSymbol size="small" type="edit2" >
                        <Icon size="small" type="edit2" />
                    </FoundationSymbol>
                    {/*<span className="ice-menu-item-text">{"提交"+branch}</span>*/}
                    <span className="ice-menu-item-text">{"提交"}</span>
                </Link>
            </MenuItem>
        </Menu>
    );
  }
}

export default withRouter(CodeAside)
