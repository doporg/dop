/* eslint no-undef:0, no-unused-expressions:0, array-callback-return:0 */
import React, { Component } from 'react';
import Menu, { Item as MenuItem } from '@icedesign/menu';
import { withRouter, Link } from 'react-router-dom';
import FoundationSymbol from 'foundation-symbol';
import { Icon } from '@icedesign/base';
import Axios from 'axios';
import API from "../../pages/API";

import './index.scss';

// @withRouter

class CodeProjectAside extends Component {
  static propTypes = {};

  static defaultProps = {};


  constructor(props) {
      super(props);
      const {username,projectname}=this.props.match.params;
      this.state={
          username:username,
          projectname:projectname,
          projectid:username+"/"+projectname,
      };
  }

  componentWillMount(){
      if(this.props.match.params.hasOwnProperty('ref')){
          this.setState({
              ref:this.props.match.params.ref
          });
      }else {
          Axios.get(API.code+"/projects/"+this.state.projectid+"/defaultbranch?userId="+sessionStorage.getItem("user-id")).then(response=>{
              this.setState({
                  ref:response.data
              })
          })
      }
  }


  commitLink(ref){

      let { username,projectname} = this.state;
      let commitLink="/code/"+username+"/"+projectname+"/commitlist/"+ref;
      this.props.history.push(commitLink);

  }

  fileLink(ref){

      let { username,projectname} = this.state;
      let fileLink="/code/"+username+"/"+projectname+"/tree/"+ref+"/"+encodeURIComponent("/");
      this.props.history.push(fileLink);

  }


  getBranchAndJump(Link){

      if(this.props.match.params.hasOwnProperty('ref')){
          Link(this.props.match.params.ref);
      }else {

          Axios.get(API.code + "/projects/" + this.state.projectid + "/defaultbranch?userId=" + sessionStorage.getItem("user-id")).then(response => {
              Link(response.data);
          })

      }

  }



  render() {
    const { location } = this.props;
    const { pathname } = location;
    const { username,projectname} = this.state;


    const backLink="/code/projects/personal";

    const projectLink="/code/"+username+"/"+projectname;



    return (
        <Menu mode="inline" selectedKeys={[pathname]} className="ice-menu-custom">
            <MenuItem key={backLink}>
                <Link to={backLink} className="ice-menu-link">
                    <FoundationSymbol size="small" type="backward" >
                        <Icon size="small" type="backward" />
                    </FoundationSymbol>
                    <span className="ice-menu-item-text">{"返回"}</span>
                </Link>
            </MenuItem>
            <MenuItem key={projectLink}>
                <Link to={projectLink} className="ice-menu-link">
                        <FoundationSymbol size="small" type="directory" >
                            <Icon size="small" type="directory" />
                        </FoundationSymbol>
                    <span className="ice-menu-item-text">{"项目"}</span>
                </Link>
            </MenuItem>
            <MenuItem>
                <a onClick={this.getBranchAndJump.bind(this,this.fileLink.bind(this))} className="ice-menu-link">
                    <FoundationSymbol size="small" type="copy" >
                        <Icon size="small" type="copy" />
                    </FoundationSymbol>
                    <span className="ice-menu-item-text">{"文件"}</span>
                </a>
            </MenuItem>
            <MenuItem>
                <a onClick={this.getBranchAndJump.bind(this,this.commitLink.bind(this))} className="ice-menu-link">
                    <FoundationSymbol size="small" type="edit2" >
                        <Icon size="small" type="edit2" />
                    </FoundationSymbol>
                    <span className="ice-menu-item-text">{"提交"}</span>
                </a>
            </MenuItem>
        </Menu>
    );
  }
}

export default withRouter(CodeProjectAside)
