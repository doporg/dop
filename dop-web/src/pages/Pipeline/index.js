import Pipeline from './Pipeline';
import PipelineInfo from './PipelineInfo/PipelineInfo'
import PipelineProject from './PipelineProject/PipelineProject'
import PipelineTest from './test/test'
import BasicLayout from "../../layouts/BasicLayout";
import EditPipelineInfo from "./PipelineInfo/EditPipelineInfo";


const pipelineConfig = [
    //pipeline
    {
        path: '/pipeline',
        layout: BasicLayout,
        component: Pipeline,
        isLogin: true
    },
    {
        path: '/pipeline/new',
        layout: BasicLayout,
        component: PipelineInfo,
        isLogin: true
    },
    {
        path: '/pipeline/project/:id',
        layout: BasicLayout,
        component: PipelineProject,
        isLogin: true
    }, {
        path: '/pipeline/edit/:id',
        layout: BasicLayout,
        component: EditPipelineInfo,
        isLogin: true
    },{
        path: '/pipeline/test',
        layout: BasicLayout,
        component: PipelineTest,
        isLogin: true
    },
];

export {Pipeline, PipelineInfo, PipelineProject, PipelineTest, pipelineConfig};
