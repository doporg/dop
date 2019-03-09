// 以下文件格式为描述路由的协议格式
// 你可以调整 routerConfig 里的内容
// 变量名 routerConfig 为 iceworks 检测关键字，请不要修改名称

import BasicLayout from '../layouts/BasicLayout';
import Projects from '../pages/Projects';
import {Pipeline, PipelineInfo, PipelineProject, PipelineTest} from '../pages/Pipeline';
import NotFound from '../pages/NotFound';
import {Login, Register, RegisterTransfer, ModifyPwd} from '../pages/Login'
import Ciadjust from '../pages/Ciadjust';
import {Permission} from "../pages/Permissions";
import {Role,RolePermissionMapping} from "../pages/Roles";

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
    },{
        path: '/pipeline/new',
        layout: BasicLayout,
        component: PipelineInfo,
    }, {
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
    // {
    //     path: '/permissions/newPermission',
    //     layout: BasicLayout,
    //     component: PipelineInfo,
    // },
];
const roleConfig = [
    //role
    {
        path: '/roles',
        layout: BasicLayout,
        component: Role,
    },
    {
        path: '/roles/rolewithpermission',
        layout: BasicLayout,
        component: RolePermissionMapping,
    },
];

const routerConfig = [...pipelineConfig, ...loginConfig, ...ciadjustConfig, ...permissionConfig , ...roleConfig, ...baseConfig];

export default routerConfig;
