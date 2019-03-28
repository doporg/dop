// 以下文件格式为描述路由的协议格式
// 你可以调整 routerConfig 里的内容


import BasicLayout from '../layouts/BasicLayout';
import {Projects, Application, ApplicationDetail} from '../pages/Projects';
import {ApplicationEnvironmentDetail} from '../pages/Projects';
import {pipelineConfig} from '../pages/Pipeline';
import NotFound from '../pages/NotFound';
import {loginConfig} from '../pages/Login'
import Ciadjust from '../pages/Ciadjust';
import {permissionConfig} from "../pages/Permissions";
import {roleConfig} from "../pages/Roles";
import {dataRulesConfig} from "../pages/DataRules";
import codeConfig from '../pages/Code'

const baseConfig = [
    {
        path: '*',
        layout: BasicLayout,
        component: NotFound,
    },
];



const routerConfig = [...roleConfig, ...dataRulesConfig, ...permissionConfig,  ...pipelineConfig, ...loginConfig, ...baseConfig];

export default routerConfig;
