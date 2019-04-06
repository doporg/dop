import Repos from './ReposManagement/Repos';
import BasicLayout from '../../layouts/BasicLayout';
import Image from './Image';
import ImageList from "./ImageList/ImageList";

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
    },
    {
        path: '/image/projects/:projectId/repos/:projectName/:repoName/images',
        layout: BasicLayout,
        component: ImageList
    }
];

export {Repos,imageConfig,Image,ImageList}