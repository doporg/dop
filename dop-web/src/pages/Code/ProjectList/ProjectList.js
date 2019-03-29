import React from 'react';
import {Table} from '@icedesign/base';
import {Link, withRouter} from 'react-router-dom';
import FoundationSymbol from 'foundation-symbol';
import API from "../../API";
import Axios from 'axios';

import './ProjectList.css';

import imgPublic from './imgs/public.png'
import imgStar from './imgs/star.png'
import imgPrivate from './imgs/private.png'

export default class ProjectList extends React.Component {


    constructor(props) {
        super(props);
        this.state = {
            sort: "personal",
            data: [],
            showData:[],
            pageNo: 1,
            pageSize: 5,
            pageTotal: 0
        };
    }

    selectClassPersonal() {

        if (this.state.sort === 'personal') {
            return "menu-item-active";
        } else {
            return "menu-item";
        }
    }

    selectClassStarred() {

        if (this.state.sort === 'starred') {
            return "menu-item-active";
        } else {
            return "menu-item";
        }
    }

    selectClassAll() {

        if (this.state.sort === 'all') {
            return "menu-item-active";
        } else {
            return "menu-item";
        }
    }


    componentWillMount() {

        let url = API.code + "/projectlist?sort=" + this.state.sort + "&username=" + sessionStorage.getItem("user-name");
        let self = this;
        Axios.get(url).then((response) => {
            const pageTotal = Math.ceil(response.data.length / this.state.pageSize);
            self.setState({pageTotal: pageTotal, data: response.data,showData:response.data});
        })

    }

    changeSort(sort) {
        let url = API.code + "/projectlist?sort=" + sort + "&username=" + sessionStorage.getItem("user-name");
        let self = this;
        Axios.get(url).then((response) => {
            const pageTotal = Math.ceil(response.data.length / this.state.pageSize);
            self.setState({sort: sort, pageTotal: pageTotal, pageNo: 1, data: response.data,showData:response.data});
        })
    }

    prePage() {
        this.setState({
            pageNo: this.state.pageNo > 1 ? this.state.pageNo - 1 : 1
        })
    }

    nextPage() {
        this.setState({
            pageNo: this.state.pageNo < this.state.pageTotal ? this.state.pageNo + 1 : this.state.pageTotal
        })
    }

    handleInputChange(event) {
        // console.log(event.target.value);
        const val = event.target.value;
        if (val !== "") {
            const showData = this.state.data.filter(function (item) {
                const name = item.path_with_namespace.substring(item.path_with_namespace.indexOf('/')+1);
                return name.indexOf(val) !== -1;
            });
            const pageTotal=Math.ceil(showData.length/this.state.pageSize);
            this.setState({
                pageTotal:pageTotal,
                pageNo:1,
                showData:showData
            })
        }else {
            const pageTotal=Math.ceil(this.state.data.length/this.state.pageSize);
            this.setState({
                pageTotal:pageTotal,
                pageNo:1,
                showData:this.state.data
            })
        }

    }


    render() {


        return (
            <div className="container">
                <div>
                    <ul className="center-top-menu">
                        <li>
                            <a className={this.selectClassPersonal()} onClick={this.changeSort.bind(this, "personal")}>
                                <span>你的项目</span>
                            </a>
                        </li>
                        <li>
                            <a className={this.selectClassStarred()} onClick={this.changeSort.bind(this, "starred")}>
                                <span>星标项目</span>
                            </a>
                        </li>
                        <li>
                            <a className={this.selectClassAll()} onClick={this.changeSort.bind(this, "all")}>
                                <span>全部项目</span>
                            </a>
                        </li>
                    </ul>
                    <div className="search-form">
                        <input className="input_search" placeholder="根据项目名称过滤" onChange={this.handleInputChange.bind(this)}/>
                        <button className="btn_add_project">
                            <Link to="/code/projects/new"><span className="text_new">+新建项目</span></Link>
                        </button>
                    </div>
                    <div>
                        <ul>
                            {(() => {
                                let begin = (this.state.pageNo - 1) * this.state.pageSize;
                                let end = Math.min(this.state.pageNo * this.state.pageSize, this.state.showData.length);
                                return this.state.showData.slice(begin, end).map((item) => {
                                    const path = "/code/" + item.path_with_namespace.substring(0, item.path_with_namespace.indexOf('/')) + "/" + item.id;
                                    // console.log(path);
                                    return (
                                        <li className="list-item">
                                            <div className="list-item-avatar">
                                                {item.name.substring(0, 1).toUpperCase()}
                                            </div>
                                            <div className="list-item-intro">
                                                <div><Link to={path}><span
                                                    className="text_path">{item.path_with_namespace}</span></Link></div>
                                                <div><span className="text_description">{item.description}</span></div>
                                            </div>
                                            <div className="list-item-tag">
                                                <img src={imgStar}/>
                                                <span>{item.star_count}</span>
                                                {
                                                    (() => {
                                                        if (item.visibility === "public") {
                                                            return <img src={imgPublic}/>
                                                        } else {
                                                            return <img src={imgPrivate}/>
                                                        }
                                                    })()
                                                }

                                            </div>
                                        </li>
                                    );
                                })
                            })()
                            }
                        </ul>

                    </div>
                </div>

                <div className="div_page">
                    <button className="btn_page" onClick={this.nextPage.bind(this)}>下一页</button>
                    <button className="btn_page">{this.state.pageNo}页/{this.state.pageTotal}页</button>
                    <button className="btn_page" onClick={this.prePage.bind(this)}>上一页</button>
                </div>

            </div>
        );


    }

}







