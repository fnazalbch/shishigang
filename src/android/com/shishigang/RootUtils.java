package com.shishigang;

public class RootUtils {
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

	public static boolean checkForRootPackages(String userPackage) {
		for (String pkg : knownRootAppsPackages) {
			if (userPackage.equals(pkg)) {
				return true;
			}
		}
		return false;
	}

	public static boolean checkForDangerousApps(String userPackage) {
		for (String pkg : knownDangerousAppsPackages) {
			if (userPackage.equals(pkg)) {
				return true;
			}
		}
		return false;
	}
}