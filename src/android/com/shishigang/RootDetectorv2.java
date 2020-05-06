package com.shishigang;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;

import java.util.*;

import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import android.os.SELinux;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class RootDetectorv2 extends CordovaPlugin {

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("listApps")) {
            PackageManager packageManager = this.cordova.getActivity().getPackageManager();
            List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
            ArrayList<JSONObject> res = new ArrayList<JSONObject>();

            for (ApplicationInfo packageInfo : packages) {
                JSONObject json = new JSONObject();
                json.put("package", packageInfo.packageName);
                res.add(json);
            }
            JSONArray array = new JSONArray(res);
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, array.toString()));
            return true;
        }
        else if (action.equals("lookForRootApp")) {
            PackageManager packageManager = this.cordova.getActivity().getPackageManager();
            List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

            for (ApplicationInfo packageInfo : packages) {
                if (packageInfo.packageName.equals("com.topjohnwu.magisk")) {
                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, "Se ha encontrado app root"));
                    return true;
                }
            }
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, "No se ha detectado app root"));
            return false;
        }
        else if (action.equals("isSELinux")) {
            boolean result = SELinux.isSELinuxEnforced();
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, result));
            return true;
        }
        else {
            callbackContext.error("Ha ocurrido un error");
            return false;
        }
    }

    /**
     * final PackageManager pm = getPackageManager();
     * //get a list of installed apps.
     * List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
     *
     * for (ApplicationInfo packageInfo : packages) {
     *     Log.d(TAG, "Installed package :" + packageInfo.packageName);
     *     Log.d(TAG, "Source dir : " + packageInfo.sourceDir);
     *     Log.d(TAG, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
     * }
     * @param callbackContext
     */
}
