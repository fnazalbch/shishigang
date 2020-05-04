var exec = require('cordova/exec');

var PLUGIN_NAME = 'RootDetectorv2';

var rootdetector = {
    listApps: function (success, error) {
        exec(success, error, PLUGIN_NAME, 'listApps', []);
    }
};

module.exports = rootdetector;

