// 以下文件格式为描述路由的协议格式
// 你可以调整 routerConfig 里的内容


import BasicLayout from '../layouts/BasicLayout';
import Projects from '../pages/Projects';
import {Pipeline, PipelineInfo, PipelineProject, PipelineTest} from '../pages/Pipeline';
import NotFound from '../pages/NotFound';
import {Login, Register, RegisterTransfer, ModifyPwd} from '../pages/Login'
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

const pipelineConfig = [
    //pipeline
    {
        path: '/pipeline',
        layout: BasicLayout,
        component: Pipeline,
    },
    // {
    //     path: '/pipeline/new',
    //     layout: BasicLayout,
    //     component: PipelineInfo,
    // },
    {
        path: '/pipeline/project/:id',
        layout: BasicLayout,
        component: PipelineProject,
    }, {
        path: '/pipeline/edit/:id',
        layout: BasicLayout,
        component: PipelineInfo,
    },{
        path: '/pipeline/test',
        layout: BasicLayout,
        component: PipelineTest,
    },
];

const loginConfig = [
    {
        path: '/login',
        component: Login,
        isLogin: true
    },
    {
        path: '/register',
        component: Register,
        isLogin: true
    },
    {
        path: '/register/transfer',
        component: RegisterTransfer,
        isLogin: true
    },
    {
        path: '/modifyPwd',
        component: ModifyPwd,
        isLogin: true
    }
];

const ciadjustConfig = [
    //ci-adjust
    {
        path: '/ciadjust',
        layout: BasicLayout,
        component: Ciadjust,
    }
];

const permissionConfig = [
    //permission
    {
        path: '/permissions',
        layout: BasicLayout,
        component: Permission,
    },
];
const roleConfig = [
    //role
    {
        path: '/roles',
        layout: BasicLayout,
        component: Role,
    },
    {
        path: '/roles/userwithrole',
        layout: BasicLayout,
        component: UserRoleMapping,
    },
];


const codeConfig = [
    {
        path: '/code/projects/personal',
        layout: CodeLayout,
        component:PersonalProjects

    },
    {
        path: '/code/projects/starred',
        layout: CodeLayout,
        component:StarredProjects

    },
    {
        path: '/code/projects/all',
        layout: CodeLayout,
        component:AllProjects

    },
    {
        path: '/code/summary',
        layout: CodeLayout,
        component:Code

    },
    {
        path: '/code/files',
        layout: CodeLayout,
        component:Code

    },
    {
        path: '/code/commits',
        layout: CodeLayout,
        component:Code

    },
    {
        path: '/code/groups',
        layout: CodeLayout,
        component:Code

    },
];

const routerConfig = [...projectConfig, ...pipelineConfig, ...loginConfig, ...ciadjustConfig,...codeConfig, ...permissionConfig , ...roleConfig, ...baseConfig];

export default routerConfig;
