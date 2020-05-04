package android;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;

import java.util.*;

import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class RootDetectorv2 extends CordovaPlugin {

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
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, array));
            return true;
        }
        return false;
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
