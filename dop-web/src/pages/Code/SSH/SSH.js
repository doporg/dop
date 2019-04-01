import React from 'react';
import ReactMarkdown from 'react-markdown';


const input = '# a-hh-project\n' +
    '\n' +
    'xc\n' +
    'this is a hhhhh project\n' +
    'wwwwwwwwwwwwwww\n' +
    'fffffffffff';

export default class SSH extends React.Component{

    render(){
        return (
            <div className="ssh-container">
                <ReactMarkdown source={input} />
            </div>
        )
    }
}
