import BasicLayout from "../../layouts/BasicLayout";
import TestCases from "./TestCases";
import CreateManualCase from "../CreateManualCase";

const testConfig = [
  {
    path: "/testCases",
    layout: BasicLayout,
    component: TestCases,
    isLogin: true
  },
  {
    path: "/createManualCase",
    layout: BasicLayout,
    component: CreateManualCase,
    isLogin: true
  },
];


export {testConfig};
