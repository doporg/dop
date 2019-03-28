import Projects from './Projects.js';
import BasicLayout from "../../layouts/BasicLayout";
import Application from './Application/Application';
import ApplicationDetail from './ApplicationDetail/ApplicationDetail';
import ApplicationEnvironmentDetail from './components/ApplicationManagement/ApplicationEnvironmentDetail'


const projectConfig = [
    {
        path: '/project',
        layout: BasicLayout,
        component: Projects,
    }, {
        path: '/application',
        layout: BasicLayout,
        component: Application,
    },
    {
        path: '/applicationDetail',
        layout: BasicLayout,
        component: ApplicationDetail,
    },
    {
        path: '/application/environment/detail',
        layout: BasicLayout,
        component: ApplicationEnvironmentDetail
    }
];
export {projectConfig};
