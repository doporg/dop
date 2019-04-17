import {ProjectList} from './ProjectList'
import ProjectOverview from './ProjectOverview'
import NewProject from './NewProject'
import Tree from './Tree'
import CodeLayout from "../../layouts/CodeLayout";
import CodeProjectLayout from '../../layouts/CodeProjectLayout';
import CodeSettingLayout from '../../layouts/CodeSettingLayout';
import Blob from "./Blob";
import EditFile from './EditFile'
import CommitList from './CommitList';
import FilePathList from './FilePathList';
import Commit from './Commit';
import EditProject from './EditProject';
import SSH from './SSH';
import NewSSH from './NewSSH'
import SSHREADME from './SSHREADME'
import BranchList from './BranchList'
import NewBranch from './NewBranch'
import TagList from './TagList'

const codeConfig = [

    {
        path: '/code/projects/new',
        layout: CodeLayout,
        component: NewProject
    },
    {
        path: '/code/projects/:sort',
        layout: CodeLayout,
        component: ProjectList
    },
    {
        path: '/code/ssh',
        layout: CodeLayout,
        component: SSH
    },
    {
        path: '/code/ssh/README',
        layout: CodeLayout,
        component: SSHREADME
    },
    {
        path: '/code/ssh/new',
        layout: CodeLayout,
        component: NewSSH
    },
    {
        path: '/code/:username/:projectname',
        layout: CodeProjectLayout,
        component: ProjectOverview

    },
    {
        path: '/code/:username/:projectname/tree/:ref/:path',
        layout: CodeProjectLayout,
        component: Tree
    },
    {
        path: '/code/:username/:projectname/blob/:ref/:path',
        layout: CodeProjectLayout,
        component: Blob
    },
    {
        path: '/code/:username/:projectname/edit/:ref/:path',
        layout: CodeProjectLayout,
        component: EditFile
    },
    {
        path: '/code/:username/:projectname/commitlist/:ref',
        layout: CodeProjectLayout,
        component: CommitList
    },
    {
        path: '/code/:username/:projectname/filepathlist/:ref',
        layout: CodeProjectLayout,
        component: FilePathList
    },
    {
        path: '/code/:username/:projectname/commit/:commitid',
        layout: CodeProjectLayout,
        component: Commit
    },
    {
        path: '/code/:username/:projectname/branches',
        layout: CodeProjectLayout,
        component: BranchList
    },
    {
        path: '/code/:username/:projectname/branches/new',
        layout: CodeProjectLayout,
        component: NewBranch
    },
    {
        path: '/code/:username/:projectname/tags',
        layout: CodeProjectLayout,
        component: TagList
    },
    {
        path: '/code/:username/:projectname/edit',
        layout: CodeSettingLayout,
        component: EditProject
    },


];

export default codeConfig;
