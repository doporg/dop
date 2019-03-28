import BasicLayout from "../../layouts/BasicLayout";
import TestCases from "./TestCases";
import CreateManualCase from "../CreateManualCase";
import CreateInterfaceScripts from "../CreateInterfaceScripts";

const testConfig = [
  {
    path: "/test/createInterfaceScripts",
    layout: BasicLayout,
    component: CreateInterfaceScripts,
    isLogin: true
  },
  {
    path: "/test/createManualCase",
    layout: BasicLayout,
    component: CreateManualCase,
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
