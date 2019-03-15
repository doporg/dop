import React from 'react';
import IceLabel from '@icedesign/label';
import FoundationSymbol from 'foundation-symbol';
import './ProjectOverview.css'

import imgPublic from './imgs/public.png'
import imgStar from './imgs/star.png'
import imgBranch from './imgs/branch.png'
import imgDownload from './imgs/download.png'

export default class ProjectOverview extends React.Component{

    componentWillMount(){

    }

    render(){

        return (

            <div className="container">
                <button className="btn btn_public">
                    <img src={imgPublic}/>PUBLIC
                </button>
                <button className="btn btn_edit">
                    <FoundationSymbol size="small" type="edit2" >
                    </FoundationSymbol>
                </button>
                <div className="project_avatar">
                    H
                </div>

                <div>
                    <p className="text_title">hhh</p>
                    <p className="text_description">This is a description!</p>
                </div>

                <div className="div1">
                    <button className="btn btn_star">
                        <img src={imgStar}/>0
                    </button>
                    <button className="btn btn_branch">
                        <img src={imgBranch}/>0
                    </button>

                    <button className="btn btn_ssh">
                        SSH
                    </button>
                    <button className="btn btn_http">
                        HTTP
                    </button>
                    <input className="input_url" type="text"/>
                    <button className="btn btn_copy">
                        <FoundationSymbol size="small" type="copy" >
                        </FoundationSymbol>
                    </button>
                    <button className="btn btn_download">
                        <img src={imgDownload}/>
                    </button>
                </div>

                <div className="div2">
                    <span>1次提交</span>
                    <span>1个分支</span>
                    <span>0个标签</span>
                    <span>0.14MB</span>
                </div>

            </div>
        );
    }
}

