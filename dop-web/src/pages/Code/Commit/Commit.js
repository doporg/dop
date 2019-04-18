import React from 'react'
import Axios from 'axios';
import API from "../../API";
import {parseDiff, Diff, Hunk,Decoration} from 'react-diff-view';

import './Commit.css';
import './ReactDiffView.css'

import imgFile from './imgs/file.png';


//diff,new_path,old_path,a_mode,b_mode,new_file,renamed_file,deleted_file
const DiffFile = ({diffItem}) => {
    const diffText=diffItem.diff;
    const files = parseDiff(diffText);

    const renderFile = ({oldRevision, newRevision, type, hunks}) => {

        let res=[];
        if(diffItem.new_file){
            res.push(
                <div className="div-diff-file-title">
                    <img className="img-diff-file-title" src={imgFile}/>
                    <span className="text-diff-file-name">{diffItem.new_path}</span>
                    <span className="text-diff-file-change">&nbsp;&nbsp;new&nbsp;{diffItem.a_mode}&nbsp;->&nbsp;{diffItem.b_mode}</span>
                </div>
            )
        }else if(diffItem.deleted_file){
            res.push(
                <div className="div-diff-file-title">
                    <img className="img-diff-file-title" src={imgFile}/>
                    <span className="text-diff-file-name">{diffItem.new_path}</span>
                    <span className="text-diff-file-change">&nbsp;&nbsp;deleted&nbsp;{diffItem.a_mode}&nbsp;->&nbsp;{diffItem.b_mode}</span>
                </div>
            )
        }else if(diffItem.renamed_file){
            res.push(
                <div className="div-diff-file-title">
                    <img className="img-diff-file-title" src={imgFile}/>
                    <span className="text-diff-file-name">{diffItem.old_path}&nbsp;->&nbsp;{diffItem.new_path}</span>
                    <span className="text-diff-file-change">&nbsp;&nbsp;renamed</span>
                </div>
            )
        }else{
            res.push(
                <div className="div-diff-file-title">
                    <img className="img-diff-file-title" src={imgFile}/>
                    <span className="text-diff-file-name">{diffItem.new_path}</span>
                    <span className="text-diff-file-change">&nbsp;&nbsp;modified</span>
                </div>
            )
        }

        res.push(
            <Diff key={oldRevision + '-' + newRevision} viewType="unified" diffType={type} hunks={hunks}>
                {
                    hunks => hunks.map(hunk => [
                        <Decoration key={'decoration-' + hunk.content}>
                            {hunk.content}
                        </Decoration>,
                        <Hunk key={hunk.content} hunk={hunk}/>
                    ])
                }
            </Diff>
        );

        return res;

    };

    return (
        <div className="div-diff-file">
            {files.map(renderFile)}
        </div>
    );
};


class Commit extends React.Component{

    constructor(props){
        super(props);
        let {projectname,username,sha}=this.props.match.params;
        sha=decodeURIComponent(sha);
        this.state={
            username:username,
            projectname:projectname,
            projectid:username+"/"+projectname,
            sha:sha,
            diffInfo:[],//diff,new_path,old_path,a_mode,b_mode,new_file,renamed_file,deleted_file
        }
    }

    componentWillMount(){
        let url=API.code+"/projects/"+this.state.projectid+"/repository/commit?sha="+this.state.sha;
        Axios.get(url).then(response=>{
            this.setState({
                diffInfo:response.data
            })
        })
    }

    render(){
        return (
            <div className="commit-container">
                {
                    this.state.diffInfo.map(item=>{
                        return <DiffFile diffItem={item}/>;
                    })
                }
             </div>
        )
    }
}

export default (props)=><Commit {...props} key={props.location.pathname} />

