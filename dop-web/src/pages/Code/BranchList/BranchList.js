import React from 'react';

import './BranchList.css'

class BranchList extends React.Component{

    render(){
        return (
            <div>
                BranchList !!!!
            </div>
        )
    }
}

export default (props)=><BranchList {...props} key={props.location.pathname} />
