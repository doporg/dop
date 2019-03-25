import Permission from './Permission';
import BasicLayout from "../../layouts/BasicLayout";

const permissionConfig = [
    //permission
    {
        path: '/permissions',
        layout: BasicLayout,
        component: Permission,
    },
];

export {Permission,permissionConfig};