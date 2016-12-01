package com.fenqipai.fenqipaiandroid.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;



/**
 * @ClassName:MoneyUtils
 * @Description:价钱转换
 * @author qianyuhang
 * @date 2016-7-15 上午9:16:33
 */

public class MoneyUtils {

	/**
	 * @Title:toWan
	 * @Description: 元转换为万元
	 * @param
	 * @return
	 * @throws
	 */
	public static String toWan(String str){
		String money="";
		
		if (isNumeric(str)) {
			NumberFormat nf = new DecimalFormat("##.##");
			
			Double d = Double.parseDouble(str);
			money = nf.format(d/10000);
		}
		return money;
	}
	
	
	public static boolean isNumeric(String str){ 
		try { 
		Double.parseDouble(str); 
		return true; 
		} catch (NumberFormatException e) { 
		return false; 
		} 
		} 
	
	
	
	/**
	 * @Title:toDisplacement
	 * @Description: 毫升转换为升
	 * @param
	 * @return
	 * @throws
	 */
	
	public static String toDisplacement(String str){
		String money="";
		
		if (isNumeric(str)) {
			NumberFormat nf = new DecimalFormat("##.#");
			
			Double d = Double.parseDouble(str);
			money = nf.format(d/1000);
		}
		return money;
	}
	
	

}
