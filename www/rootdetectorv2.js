var exec = require('cordova/exec');

var PLUGIN_NAME = 'rootdetectorv2';

var rootdetectorv2 = {
    listApps: function (success, error) {
        exec(success, error, PLUGIN_NAME, 'listApps', []);
    }
};

module.exports = rootdetectorv2;

