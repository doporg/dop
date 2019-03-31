import BasicLayout from "../../layouts/BasicLayout";
import TestCases from "./TestCases";
import CreateInterfaceCase from "./CreateInterfaceCase";
import CreateInterfaceScripts from "./CreateInterfaceScripts";
import {Feedback} from "@icedesign/base";
import CreateManualCaseFrom from "./components/CreateTestCases/CreateManualCaseForm";
import AuthorityTable from "./ExecuteLogs/components/AuthorityTable";

const Toast = Feedback.toast;

const testConfig = [
  {
    path: "/test/showExecuteLogs/:caseId",
    layout: BasicLayout,
    component: AuthorityTable,
    isLogin: true
  },
  {
    path: "/test/createInterfaceScripts/:caseId",
    layout: BasicLayout,
    component: CreateInterfaceScripts,
    isLogin: true
  },
  {
    path: "/test/createInterfaceCase",
    layout: BasicLayout,
    component: CreateInterfaceCase,
    isLogin: true
  },
  {
    path: "/test/createManualCase",
    layout: BasicLayout,
    component: CreateManualCaseFrom,
    isLogin: true
  },
  {
    path: "/testCases",
    layout: BasicLayout,
    component: TestCases,
    isLogin: true
  },
];


export {testConfig};
