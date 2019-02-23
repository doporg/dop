import React, {Component} from 'react';


export default class Iframe extends Component{
    constructor(props){
        super(props);
    }
    componentWillReceiveProps(){
        setTimeout(()=>{
            document.getElementById("iframe").src = this.props.src
        },0)
    }
    render(){
        return (
            <div style={style.wrapper}>
                <iframe
                    id = "iframe"
                    frameborder = "0"
                    scrolling = "yes"
                    style={style.iframe}
                />
            </div>
        )
    }
}
const style = {
    wrapper: {
        width: '100%',
        height: '900px',
        overflow: 'hidden'
    },
    iframe : {
        position: "relative",
        top: "-130px",
        width: "100%",
        height: "100%"
    }
};
