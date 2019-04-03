import React, {Component} from 'react';
import API from "../../../API"
import Axios from "axios";
import NamespaceList from "../NamespaceList";


export default class NamespacePagination extends Component {
    constructor(props) {
        super();

        this.state = {
            currentData: [],
            //总页数
            totalPage: 1,
            queryKey: props.searchKey,
            loading: true
        };

        this.handleChange = this.handleChange.bind(this);
    }

    refreshList() {
        let url = API.test_image + '/v1/projects';
        let _this = this;
        Axios.get(url, {
            
        })
            .then(function (response) {
                console.log("镜像信息");
                console.log(response.data);
                _this.setState({
                    currentData: response.data,
                    totalCount: response.data.length
                });

            })

    }

    handleChange(current) {
        this.refreshList(current, this.state.searchKey);
    }


    componentWillReceiveProps(nextProps) {
        let key = nextProps.searchKey;
        this.refreshList(1, key);
    }

    componentDidMount() {
        this.refreshList(1, "");
    }

    render() {
        return (
            <div>
                <NamespaceList currentData={this.state.currentData}/>
            </div>
        )
    }

}






