// 以下文件格式为描述路由的协议格式
// 你可以调整 routerConfig 里的内容


import BasicLayout from '../layouts/BasicLayout';
import Projects from '../pages/Projects';
import { pipelineConfig } from '../pages/Pipeline';
import NotFound from '../pages/NotFound';
import {loginConfig} from '../pages/Login'
import Ciadjust from '../pages/Ciadjust';
const baseConfig = [
    {
        path: '/project',
        layout: BasicLayout,
        component: Projects,
    },
    {
        path: '*',
        layout: BasicLayout,
        component: NotFound,
    },
];

const ciadjustConfig = [
    //ci-adjust
    {
        path: '/ciadjust',
        layout: BasicLayout,
        component: Ciadjust,
    }
];

const routerConfig = [...pipelineConfig, ...loginConfig, ...ciadjustConfig,...baseConfig];

export default routerConfig;
