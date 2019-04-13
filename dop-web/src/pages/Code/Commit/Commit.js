import React from 'react'

import './Commit.css';


import {parseDiff, Diff, Hunk} from 'react-diff-view';

const App = ({diffText}) => {
    const files = parseDiff(diffText);

    const renderFile = ({oldRevision, newRevision, type, hunks}) => (
        <Diff key={oldRevision + '-' + newRevision} viewType="split" diffType={type} hunks={hunks}>
            {hunks => hunks.map(hunk => <Hunk key={hunk.content} hunk={hunk} />)}
        </Diff>
    );

    return (
        <div>
            {files.map(renderFile)}
        </div>
    );
};

const diffText="diff --git a/3.txt b/3.txt\n" +
    "index dc80e08..06775fd 100644\n" +
    "--- a/3.txt\n" +
    "+++ b/3.txt\n" +
    "@@ -6,4 +6,5 @@ fdsf\n" +
    " ds\n" +
    " fdsfdsfdsfdsfsdfdsfdsf\n" +
    " fdsfdsfsdfsdfsdfsdfsdfs\n" +
    "-wwwwwwwwwwwwwww\n" +
    "\\ No newline at end of file\n" +
    "+wwwwwwwwwwwwwww\n" +
    "+fdsfsdfdsfdsfsd\n" +
    "\\ No newline at end of file\n";


export default class Commit extends React.Component{

    constructor(props){
        super(props);
        this.state={
        }
    }

    render(){
        return (
            <App diffText={diffText}/>
            // <div className="commit-container">
            //
            //  </div>
        )
    }
}
