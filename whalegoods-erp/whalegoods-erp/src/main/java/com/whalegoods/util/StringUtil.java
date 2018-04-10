package com.whalegoods.util;

/**
 * 字符串工具类
 * @author chencong
 *
 */
public class StringUtil {

	/**
	 * 生成固定长度的随机字符串（字母数字）
	 * @param length 传入的字符串的长度
	 * @return
	 */
	public static String randomString(int length) {
		StringBuilder builder = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int r = (int) (Math.random() * 3);
			int rn1 = (int) (48 + Math.random() * 10);
			int rn2 = (int) (65 + Math.random() * 26);
			int rn3 = (int) (97 + Math.random() * 26);
			switch (r) {
			case 0:
				builder.append((char) rn1);
				break;
			case 1:
				builder.append((char) rn2);
				break;
			case 2:
				builder.append((char) rn3);
				break;
			}
		}
		return builder.toString();
	}
}
