import React from 'react';

import './TagList.css'

class TagList extends React.Component{

    render(){
        return (
            <div>
                TagList!!!!!
            </div>
        )
    }
}

export default (props)=><TagList {...props} key={props.location.pathname} />
