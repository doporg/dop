import Code from './DataOverview'
import {ProjectList} from './ProjectList'
import ProjectOverview from './ProjectOverview'
import NewProject from './NewProject'
import Tree from './Tree'
import BasicLayout from "../../layouts/BasicLayout";
import CodeLayout from "../../layouts/CodeLayout";
import Blob from "./Blob/Blob";
import CommitList from './CommitList';

const codeConfig = [

    {
        path: '/code/projects/new',
        layout: BasicLayout,
        component: NewProject
    },
    {
        path: '/code/projectlist',
        layout: BasicLayout,
        component: ProjectList
    },
    {
        path: '/code/:username/:projectid',
        layout: CodeLayout,
        component: ProjectOverview

    },
    {
        path: '/code/:username/:projectid/commits/:branch',
        layout: CodeLayout,
        component: Code
    },
    {
        path: '/code/:username/:projectid/tree/:branch',
        layout: CodeLayout,
        component: Tree
    },
    {
        path: '/code/:username/:projectid/blob/:branch',
        layout: CodeLayout,
        component: Blob
    },
    {
        path: '/code/:username/:projectid/commitlist/:branch',
        layout: CodeLayout,
        component: CommitList
    }

];

export default codeConfig;
