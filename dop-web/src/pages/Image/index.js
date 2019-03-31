import Project from "./Project/Projects";
import Repos from "./Repo/Repos";
import BasicLayout from "../../layouts/BasicLayout";


const imageConfig = [
    {
        path: "/Projects",
        layout: BasicLayout,
        component: Project,
        isLogin: true
    },
    {
        path: "/Projects/:projectId/Repos",
        layout: BasicLayout,
        component: Repos,
        isLogin: true
    }
];

export {imageConfig};
