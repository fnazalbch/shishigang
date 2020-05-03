var exec = require('cordova/exec');

var PLUGIN_NAME = 'RootDetectorv2';

var rootdetector = {
    listApps: function (arg0, success, error) {
        exec(success, error, PLUGIN_NAME, 'listApps', [arg0]);
    }
};

module.exports = rootdetector;

