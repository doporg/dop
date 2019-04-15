const { injectBabelPlugin } = require('react-app-rewired');
const WebpackPluginImport = require('webpack-plugin-import');
const rewireSass = require('./rewire-scss');

const {
    addLessLoader,
    fixBabelImports,
    override
} = require("customize-cra");

module.exports = {
    webpack: override(
        addLessLoader({
            javascriptEnabled: true
        }),
        fixBabelImports("babel-plugin-import", {
            libraryName: "antd-mobile",
            style: true
        })
    )
};

module.exports = function override(config) {
  config = injectBabelPlugin(
    ['import', { libraryName: '@icedesign/base' }],
    config
  );

  config.plugins.push(
    new WebpackPluginImport([
      {
        libraryName: /^@icedesign\/base\/lib\/([^/]+)/,
        stylePath: 'style.js',
      },
      {
        libraryName: /@icedesign\/.*/,
        stylePath: 'style.js',
      },
    ])
  );

  config = rewireSass(config);

  return config;
};
