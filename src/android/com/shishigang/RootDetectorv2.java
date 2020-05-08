package com.shishigang;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;

import java.util.*;
import java.io.*;
import android.util.Log;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class RootDetectorv2 extends CordovaPlugin {

    public static final String[] knownRootAppsPackages = {
            "com.noshufou.android.su",
            "com.noshufou.android.su.elite",
            "eu.chainfire.supersu",
            "com.koushikdutta.superuser",
            "com.thirdparty.superuser",
            "com.yellowes.su",
            "com.zachspong.temprootremovejb",
            "com.ramdroid.appquarantine",
            "eu.chainfire.supersu",
            "com.topjohnwu.magisk"
    };

    public static final String[] knownDangerousAppsPackages = {
            "com.koushikdutta.rommanager",
            "com.koushikdutta.rommanager.license",
            "com.dimonvideo.luckypatcher",
            "com.chelpus.lackypatch",
            "com.ramdroid.appquarantine",
            "com.ramdroid.appquarantinepro"
    };

    /** EdXposed */
    public static final String[] knownRootCloakingPackages = {
            "com.devadvance.rootcloak",
            "com.devadvance.rootcloakplus",
            "de.robv.android.xposed.installer",
            "com.saurik.substrate",
            "com.zachspong.temprootremovejb",
            "com.amphoras.hidemyroot",
            "com.amphoras.hidemyrootadfree",
            "com.formyhm.hiderootPremium",
            "com.formyhm.hideroot"
    };

    public static final String[] suPaths ={
            "/data/local/",
            "/data/local/bin/",
            "/data/local/xbin/",
            "/sbin/",
            "/su/bin/",
            "/system/bin/",
            "/system/bin/.ext/",
            "/system/bin/failsafe/",
            "/system/sd/xbin/",
            "/system/usr/we-need-root/",
            "/system/xbin/"
    };

    public static final String[] pathsThatShouldNotBeWritable = {
            "/system",
            "/system/bin",
            "/system/sbin",
            "/system/xbin",
            "/vendor/bin",
            "/sys",
            "/sbin",
            "/etc",
            "/proc",
            "/dev"
    };

    public static JSONObject result = new JSONObject();

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
                /* if (packageInfo.packageName.equals("com.topjohnwu.magisk")) {
                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, "Se ha encontrado app root"));
                    return true;
                } */
                if (checkForRootPackages(packageInfo.packageName)) {
                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, "Se ha encontrado app root"));
                    return true;
                }
            }
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, "No se ha detectado app root"));
            return false;
        }
        else if (action.equals("isSELinux")) {
            isSELinuxEnforced(result);
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, result));
            return true;
        }
        else {
            callbackContext.error("Ha ocurrido un error");
            return false;
        }
    }

    private static boolean isSELinuxEnforced(JSONObject result) throws JSONException {
        StringBuffer output = new StringBuffer();
        Process p;

        try {
            p = Runtime.getRuntime().exec("getenforce");
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
        }
        catch (Exception e) {
            result.put("Error", e.getMessage());
            result.put("Catch", false);
            Log.e("Ionic Android Plugin", "No existe soporte getenforce");
            e.printStackTrace();
            return false;
        }
        String response = output.toString();
        if ("Enforcing".equals(response)) {
            result.put("Response", response);
            result.put("Result", true);
            return true;
        }
        else if ("Permissive".equals(response)) {
            result.put("Response", response);
            result.put("Permissive", false);
            return false;
        }
        else {
            Log.e("Ionic Android Plugin", "Valor inesperado");
            result.put("Response", response);
            result.put("Unexpected", false);
            return false;
        }
    }

    public static boolean checkForRootPackages(String userPackage) throws InterruptedException {
        boolean res = false;
        /* Concurrent */
        final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (String pkg : knownRootAppsPackages) {
            executorService.submit(new java.lang.Runnable() {
                @Override
                public void run() {
                    try {
                        if (executeEqualityForRootPackages(pkg, userPackage)) {
                            res = true;
                            executorService.shutdownNow();
                        }
                    }
                    catch (CloneNotSupportedException e) {
                        result.put("Error", e.getMessage());
                    }
                }
            });
            /**if (userPackage.equals(pkg)) {
                return true;
            }*/
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        }
        catch (InterruptedException e) {
            executorService.shutdownNow();
            throw e;
        }
        return res;
    }

    private static boolean executeEqualityForRootPackages(String pkg, String userPackage) {
        return userPackage.equals(pkg);
    }

    public static boolean checkForDangerousApps(String userPackage) {
        for (String pkg : knownDangerousAppsPackages) {
            if (userPackage.equals(pkg)) {
                return true;
            }
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
