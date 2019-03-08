import React, {Component} from 'react';


export default class Iframe extends Component{
    componentDidMount(){
        setTimeout(()=>{
            document.getElementById("iframe").src = this.props.src
        },1000)
    }

    onLoad(){
        this.props.onLoad(false)
    }
    render(){
        return (
            <div style={style.wrapper}>
                <iframe
                    id = "iframe"
                    title= "iframe"
                    frameBorder= "0"
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
        width: "102%",
        height: "100%"
    }
};
