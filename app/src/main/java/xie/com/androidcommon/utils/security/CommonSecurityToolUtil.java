package xie.com.androidcommon.utils.security;

import java.nio.charset.Charset;

public class CommonSecurityToolUtil {
	public static int G_SEED_NUM_1 = 26895494;
	public static int G_SEED_NUM_2 = 35063394;
	private static char[] HEXCHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 特殊混淆
	 * @param byteIn
     */
	public static void mixByte(byte[] byteIn) {
		int n_cerLen = byteIn.length;
		Integer n_key = getLocNum1() + getLocNum2();
		String s_key = n_key.toString();
		byte[] bs_key = s_key.getBytes(Charset.forName("UTF-8"));
		int n_keyLen = bs_key.length;

		int i = 0;
		do {
			for (int j = 0; j < n_keyLen; j++) {
				int index = i + j;
				if (index < n_cerLen) {
					byteIn[index] = (byte) (byteIn[index] ^ bs_key[j]);
				} else {
					break;
				}

			}
			i = i + n_keyLen;
		} while (i < n_cerLen);
	}



	/**
	 * byte[] to hexString
	 * @param b
	 * @return
     */
	public static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEXCHAR[(b[i] & 0xf0) >>> 4]);
			sb.append(HEXCHAR[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	/**
	 * hexstring to byte[]
	 * @param hexString
	 * @return
     */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}


	/**
	 * 拆分字符串
	 */
	public static String[] splitString(String string, int len) {
		int x = string.length() / len;
		int y = string.length() % len;
		int z = 0;
		if (y != 0) {
			z = 1;
		}
		String[] strings = new String[x + z];
		String str = "";
		for (int i=0; i<x+z; i++) {
			if (i==x+z-1 && y!=0) {
				str = string.substring(i*len, i*len+y);
			}else{
				str = string.substring(i*len, i*len+len);
			}
			strings[i] = str;
		}
		return strings;
	}
	/**
	 *拆分数组
	 */
	public static byte[][] splitArray(byte[] data,int len){
		int x = data.length / len;
		int y = data.length % len;
		int z = 0;
		if(y!=0){
			z = 1;
		}
		byte[][] arrays = new byte[x+z][];
		byte[] arr;
		for(int i=0; i<x+z; i++){
			arr = new byte[len];
			if(i==x+z-1 && y!=0){
				System.arraycopy(data, i*len, arr, 0, y);
			}else{
				System.arraycopy(data, i*len, arr, 0, len);
			}
			arrays[i] = arr;
		}
		return arrays;
	}



	// 特殊混淆代码种子 start
	private static int getLocNum1() {
		return G_SEED_NUM_1;
	}

	private static int getLocNum2() {
		return G_SEED_NUM_2;
	}
	// 特殊混淆代码种子 end
}
