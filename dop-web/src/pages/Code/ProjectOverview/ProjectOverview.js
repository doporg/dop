import React from 'react';
import IceLabel from '@icedesign/label';
import {Button} from '@icedesign/base';

import './ProjectOverview.css'

import imgPublic from './imgs/public.png'


export default class ProjectOverview extends React.Component{

    render(){

        return (

            <div className="container">
                <Button type="normal" className="btn btn_public">
                    <img src={imgPublic}/>PUBLIC
                </Button>
            </div>
        );
    }
}

