// 以下文件格式为描述路由的协议格式
// 你可以调整 routerConfig 里的内容


import BasicLayout from '../layouts/BasicLayout';
import {projectConfig} from '../pages/Projects'
import {pipelineConfig} from '../pages/Pipeline';
import NotFound from '../pages/NotFound';
import {loginConfig} from '../pages/Login'
import {permissionConfig} from "../pages/Permissions";
import {roleConfig} from "../pages/Roles";
import {dataRulesConfig} from "../pages/DataRules";

const baseConfig = [
    {
        path: '*',
        layout: BasicLayout,
        component: NotFound,
    },
];


const routerConfig = [...projectConfig, ...roleConfig, ...dataRulesConfig, ...permissionConfig, ...pipelineConfig, ...loginConfig, ...baseConfig];

export default routerConfig;
