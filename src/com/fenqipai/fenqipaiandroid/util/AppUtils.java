package com.fenqipai.fenqipaiandroid.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.text.TextUtils;

/**
 * App相关的辅助类
 * 
 * @name AppUtils
 * @author zhaoqingyang
 * @Description TODO
 * @date 2015年9月11日
 * @modify
 * @modifyDate 2015年9月11日
 * @modifyContent
 */
public class AppUtils {

	private AppUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("AppUtils cannot be instantiated");
	}

	/**
	 * 获取应用程序名称
	 */
	public static String getAppName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取应用程序版本
	 * 
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static PackageInfo getVersion(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			return packageManager.getPackageInfo(context.getPackageName(), 0);

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 判断当前版本是否兼容目标版本的方法
	 * 
	 * @param versionCode
	 * @return
	 */
	public static boolean isMethodsCompat(int versionCode) {
		int currentVersion = Build.VERSION.SDK_INT;
		return currentVersion >= versionCode;
	}

	/**
	 * 判断某个界面是否在前台
	 * 
	 * @param context
	 * @param className
	 *            某个界面名称
	 */
	public static boolean isForeground(Context context, String className) {
		if (context == null || TextUtils.isEmpty(className)) {
			return false;
		}
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(1);
		if (list != null && list.size() > 0) {
			ComponentName cpn = list.get(0).topActivity;
			if (className.equals(cpn.getClassName())) {
				return true;
			}
		}

		return false;
	}

	public static final String CPU_ARCHITECTURE_TYPE_32 = "32";
	public static final String CPU_ARCHITECTURE_TYPE_64 = "64";

	/** ELF文件头 e_indent[]数组文件类标识索引 */
	private static final int EI_CLASS = 4;
	/** ELF文件头 e_indent[EI_CLASS]的取值：ELFCLASS32表示32位目标 */
	private static final int ELFCLASS32 = 1;
	/** ELF文件头 e_indent[EI_CLASS]的取值：ELFCLASS64表示64位目标 */
	private static final int ELFCLASS64 = 2;

	/** The system property key of CPU arch type */
	private static final String CPU_ARCHITECTURE_KEY_64 = "ro.product.cpu.abilist64";

	/** The system libc.so file path */
	private static final String SYSTEM_LIB_C_PATH = "/system/lib/libc.so";
	private static final String SYSTEM_LIB_C_PATH_64 = "/system/lib64/libc.so";
	private static final String PROC_CPU_INFO_PATH = "/proc/cpuinfo";

	/**
	 * Check if the CPU architecture is x86
	 */
	public static boolean checkIfCPUx86(Context context) {

		/**
		 * Get the CPU arch type: x32 or x64
		 */
		// if (getSystemProperty("ro.product.cpu.abi", "arm").contains("x86")
		// || getSystemProperty("ro.product.cpu.abi",
		// "arm").contains("arm64-v8a")) {
		// return true;
		// } else
		if (getSystemProperty(CPU_ARCHITECTURE_KEY_64, "").length() > 0) {
			return true;
		} else if (isCPUInfo64()) {
			return true;
		} else if (isLibc64()) {
			return true;
		} else {
			return false;
		}
	}

	private static String getSystemProperty(String key, String defaultValue) {
		String value = defaultValue;
		try {
			Class<?> clazz = Class.forName("android.os.SystemProperties");
			Method get = clazz.getMethod("get", String.class, String.class);
			value = (String) (get.invoke(clazz, key, ""));
		} catch (Exception e) {
		}
		return value;
	}

	/**
	 * Read the first line of "/proc/cpuinfo" file, and check if it is 64 bit.
	 */
	private static boolean isCPUInfo64() {
		File cpuInfo = new File(PROC_CPU_INFO_PATH);
		if (cpuInfo != null && cpuInfo.exists()) {
			InputStream inputStream = null;
			BufferedReader bufferedReader = null;
			try {
				inputStream = new FileInputStream(cpuInfo);
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 512);
				String line = bufferedReader.readLine();
				if (line != null && line.length() > 0 && line.toLowerCase(Locale.US).contains("arch64")) {
					return true;
				}
			} catch (Throwable t) {
			} finally {
				try {
					if (bufferedReader != null) {
						bufferedReader.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					if (inputStream != null) {
						inputStream.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	/**
	 * Check if system libc.so is 32 bit or 64 bit
	 */
	private static boolean isLibc64() {
		File libcFile = new File(SYSTEM_LIB_C_PATH);
		if (libcFile != null && libcFile.exists()) {
			byte[] header = readELFHeadrIndentArray(libcFile);
			if (header != null && header[EI_CLASS] == ELFCLASS64) {
				return true;
			}
		}

		File libcFile64 = new File(SYSTEM_LIB_C_PATH_64);
		if (libcFile64 != null && libcFile64.exists()) {
			byte[] header = readELFHeadrIndentArray(libcFile64);
			if (header != null && header[EI_CLASS] == ELFCLASS64) {
				return true;
			}
		}

		return false;
	}

	/**
	 * ELF文件头格式是固定的:文件开始是一个16字节的byte数组e_indent[16] e_indent[4]的值可以判断ELF是32位还是64位
	 */
	private static byte[] readELFHeadrIndentArray(File libFile) {
		if (libFile != null && libFile.exists()) {
			FileInputStream inputStream = null;
			try {
				inputStream = new FileInputStream(libFile);
				if (inputStream != null) {
					byte[] tempBuffer = new byte[16];
					int count = inputStream.read(tempBuffer, 0, 16);
					if (count == 16) {
						return tempBuffer;
					} else {
					}
				}
			} catch (Throwable t) {
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		return null;
	}

}
