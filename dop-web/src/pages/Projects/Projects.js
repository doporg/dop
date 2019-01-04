import React, { Component } from 'react';
import Axios from 'axios';
import API from '../API';
import { Input } from '@icedesign/base';
import TopBar from '../../components/TopBar';
import Card from './components/Card';

export default class Projects extends Component {
  static displayName = 'Projects';

  componentWillMount(){
      this.Get()
  }
  Get(){
      let url = API.axiosGetTest + "/getTest";
      Axios.get(url).then((response)=> {
            // console.log(response)
      }).catch()
  }
  render() {
    return (
      <div>
        <TopBar
          extraBefore={
            <Input
              size="large"
              placeholder="请输入关键字进行搜索"
              style={{ width: '240px' }}
            />
          }
          buttonText="新建项目"
        />
        <Card />
      </div>
    );
  }
}
