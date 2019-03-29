import Permission from './Permissions/Permission';
import BasicLayout from "../../layouts/BasicLayout";
import DataRules from "./DataRules/DataRules";
import Role from "./Roles/Role";
import UserRoleMapping from "./Roles/UserRoleMapping/UserRoleMapping";

const permissionConfig = [
    //permission
    {
        path: '/permissions',
        layout: BasicLayout,
        component: Permission,
    },

    {
        path: '/dataRules',
        layout: BasicLayout,
        component: DataRules,
    },

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

export {Permission,permissionConfig};