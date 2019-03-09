// 菜单配置
// headerMenuConfig：头部导航配置
// asideMenuConfig：侧边导航配置


const headerMenuConfig = [
    {
        name: '反馈',
        // path: 'https://github.com/alibaba/ice',
        external: true,
        newWindow: true,
        icon: 'message',
    },
    {
        name: '帮助',
        // path: 'https://alibaba.github.io/ice',
        external: true,
        newWindow: true,
        icon: 'bangzhu',
    },
];

const asideMenuConfig = [
    {
        name: '全部项目',
        path: '/project',
        icon: 'home2',
    },
    {
        name: '流水线管理',
        path: '/pipeline',
        icon: 'ol-list',
    },
    {
        name: '测试管理',
        path: '/test',
        icon: 'repair',
    },
    {
        name: '应用管理',
        path: '/application',
        icon: 'publish',
    },
    {
        name: '自调节集成',
        path: '/ciadjust',
        icon: 'cascades',
    }
];

export {headerMenuConfig, asideMenuConfig};
