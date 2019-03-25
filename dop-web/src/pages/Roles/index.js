import Role from './Role';
import UserRoleMapping from './UserRoleMapping/UserRoleMapping'
import BasicLayout from "../../layouts/BasicLayout";

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

export {Role,UserRoleMapping,roleConfig};