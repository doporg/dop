import Code from './DataOverview'
import {ProjectList} from './ProjectList'
import ProjectOverview from './ProjectOverview'
import NewProject from './NewProject'
import Tree from './Tree'
import BasicLayout from "../../layouts/BasicLayout";
import CodeLayout from "../../layouts/CodeLayout";
import Blob from "./Blob";
import EditFile from './EditFile'
import CommitList from './CommitList';
import FilePathList from './FilePathList';
import Commit from './Commit';

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
        path: '/code/:username/:projectid/tree/:ref/:path',
        layout: CodeLayout,
        component: Tree
    },
    {
        path: '/code/:username/:projectid/blob/:ref/:path',
        layout: CodeLayout,
        component: Blob
    },
    {
        path: '/code/:username/:projectid/edit/:ref/:path',
        layout: CodeLayout,
        component: EditFile
    },
    {
        path: '/code/:username/:projectid/commitlist/:branch',
        layout: CodeLayout,
        component: CommitList
    },
    {
        path: '/code/:username/:projectid/filepathlist/:ref',
        layout: CodeLayout,
        component: FilePathList
    },
    {
        path: '/code/:username/:projectid/commit/:commitid',
        layout: CodeLayout,
        component: Commit
    }

];

export default codeConfig;
