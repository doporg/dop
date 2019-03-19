import Code from './DataOverview'
import {ProjectList} from './ProjectList'
import ProjectOverview from './ProjectOverview'
import NewProject from './NewProject'
import BasicLayout from "../../layouts/BasicLayout";
import CodeLayout from "../../layouts/CodeLayout";

const codeConfig = [

    {
        path: '/code/projects/new',
        layout: BasicLayout,
        component: NewProject
    },
    {
        path: '/code/projectlist/:sort',
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


];

export default codeConfig;
