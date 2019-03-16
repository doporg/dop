import React from 'react';
import ReactDOM from 'react-dom';
import { Nav, Icon, Menu } from "@icedesign/base";
import {Link,withRouter,BrowserRouter ,Route ,Switch} from 'react-router-dom'

const { Item} = Nav;


// const itemStyle ={
//     width:120,
//     textAlign:'center',
//     fontSize:20,
// };


class NavBar extends React.Component {


    render(){

        const {location}=this.props;
        const {pathname}=location;
        return (
            <div>
                <Nav selectedKeys={[pathname]} direction="hoz" activeDirection="bottom">
                    <Item key="/code/projects/personal">
                        <Link to="/code/projects/personal">
                            你的项目
                        </Link>
                    </Item>
                    <Item key="/code/projects/starred">
                        <Link to="/code/projects/starred">
                            星标项目
                        </Link>
                    </Item>
                    <Item key="/code/projects/all">
                        <Link to="/code/projects/all">
                            浏览项目
                        </Link>
                    </Item>
                </Nav>
            </div>
        );
    }

}




export default withRouter(NavBar);


