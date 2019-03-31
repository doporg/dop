import Repos from './ReposManagement/Repos';
import BasicLayout from '../../layouts/BasicLayout';
import Image from './Image';

const imageConfig =[
    {
        path: '/image/projects/:projectId/repos',
        layout: BasicLayout,
        component: Repos,
    },
    {
        path: '/image/projects',
        layout: BasicLayout,
        component: Image,
    }
];

export {Repos,imageConfig,Image}