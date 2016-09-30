package xie.com.androidcommon.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * 整型与字节数组之间的转换工具
 */
public class ByteConvert {

	/**
	 * 整型转字节数组
	 * 
	 * @param num
	 * @param isHighFirst
	 *            true高位在前 false高位在后
	 * @return
	 */
	public static byte[] int2Bytes(int num, boolean isHighFirst) {
		byte[] buf = new byte[4];
		if (isHighFirst) {
			for (int i = buf.length - 1; i >= 0; i--) {
				buf[i] = (byte) (num & 0x000000ff);
				num >>= 8;
			}
		} else {
			for (int i = 0; i < buf.length; i++) {
				buf[i] = (byte) (num & 0x000000ff);
				num >>= 8;
			}
		}
		return buf;
	}

	/**
	 * 整型转字节数组
	 * 
	 * @param num
	 * @param isHighFirst
	 *            true高位在前 false高位在后
	 * @return
	 */
	public static byte[] int2Bytes2(int num, boolean isHighFirst) {
		byte[] buf = new byte[1];
		if (isHighFirst) {
			for (int i = buf.length - 1; i >= 0; i--) {
				buf[i] = (byte) (num & 0x000000ff);
				num >>= 8;
			}
		} else {
			for (int i = 0; i < buf.length; i++) {
				buf[i] = (byte) (num & 0x000000ff);
				num >>= 8;
			}
		}
		return buf;
	}

	/**
	 * 字节数组转整型
	 * 
	 * @param buf
	 * @param isHighFirst
	 *            true高位在前 false高位在后
	 * @return
	 */
	public static int bytes2Int(byte[] buf, boolean isHighFirst) {
		int result = 0;
		if (isHighFirst) {
			for (int i = 0; i < buf.length; i++) {
				result <<= 8;
				result |= (buf[i] & 0x000000ff);
			}
		} else {
			for (int i = buf.length - 1; i >= 0; i--) {
				result <<= 8;
				result |= (buf[i] & 0x000000ff);
			}
		}
		return result;
	}

	/**
	 * 长整型转字节数组
	 * 
	 * @param num
	 * @param isHighFirst
	 *            true高位在前 false高位在后
	 * @return
	 */
	public static byte[] long2Bytes(long num, boolean isHighFirst) {
		byte[] buf = new byte[8];
		if (isHighFirst) {
			for (int i = buf.length - 1; i >= 0; i--) {
				buf[i] = (byte) (num & 0x00000000000000ff);
				num >>= 8;
			}
		} else {
			for (int i = 0; i < buf.length; i++) {
				buf[i] = (byte) (num & 0x00000000000000ff);
				num >>= 8;
			}
		}
		return buf;
	}

	/**
	 * 字节数组转长整型
	 * 
	 * @param buf
	 * @param isHighFirst
	 *            true高位在前 false高位在后
	 * @return
	 */
	public static long bytes2Long(byte[] buf, boolean isHighFirst) {
		long result = 0;
		if (isHighFirst) {
			for (int i = 0; i < buf.length; i++) {
				result <<= 8;
				result |= (buf[i] & 0x00000000000000ff);
			}
		} else {
			for (int i = buf.length - 1; i >= 0; i--) {
				result <<= 8;
				result |= (buf[i] & 0x00000000000000ff);
			}
		}
		return result;
	}

	/**
	 * 短整型转字节数组
	 * 
	 * @param num
	 * @param isHighFirst
	 *            true高位在前 false高位在后
	 * @return
	 */
	public static byte[] short2Bytes(short num, boolean isHighFirst) {
		byte[] buf = new byte[2];
		if (isHighFirst) {
			for (int i = buf.length - 1; i >= 0; i--) {
				buf[i] = (byte) (num & 0x00ff);
				num >>= 8;
			}
		} else {
			for (int i = 0; i < buf.length; i++) {
				buf[i] = (byte) (num & 0x00ff);
				num >>= 8;
			}
		}
		return buf;
	}

	/**
	 * 字节数组转短整型
	 * 
	 * @param buf
	 * @param isHighFirst
	 *            true高位在前 false高位在后
	 * @return
	 */
	public static short bytes2Short(byte[] buf, boolean isHighFirst) {
		short result = 0;
		if (isHighFirst) {
			for (int i = 0; i < buf.length; i++) {
				result <<= 8;
				result |= (buf[i] & 0x00ff);
			}
		} else {
			for (int i = buf.length - 1; i >= 0; i--) {
				result <<= 8;
				result |= (buf[i] & 0x00ff);
			}
		}
		return result;
	}

	/** **********************xiechengfa************ */
	/**
	 * 高位在前
	 * 
	 * @param n
	 * @return
	 */
	public static byte[] short24byte(short n) {
		byte b[] = new byte[4];
		b[3] = (byte) (n >> 24);
		return b;
	}

	/**
	 * 高位在前
	 */
	public static short byte42short(byte[] b) {
		return (short) (b[3] << 24 & 0xFF000000);
	}

	/**
	 * 高位在后
	 * 
	 * @param n
	 * @return
	 */
	public static byte[] short24byte2(short n) {
		byte b[] = new byte[4];
		b[0] = (byte) n;
		return b;
	}

	/**
	 * 高位在后
	 */
	public static short byte42short2(byte[] b) {
		return b[0];
	}

	public static byte[] getBytes(char[] chars) {
		Charset cs = Charset.forName("UTF-8");
		CharBuffer cb = CharBuffer.allocate(chars.length);
		cb.put(chars);
		cb.flip();
		ByteBuffer bb = cs.encode(cb);

		return bb.array();
	}

	public static char[] getChars(byte[] bytes, String encoding) {
		Charset cs = Charset.forName(encoding);
		ByteBuffer bb = ByteBuffer.allocate(bytes.length);
		bb.put(bytes);
		bb.flip();
		CharBuffer cb = cs.decode(bb);

		return cb.array();
	}

	/******************* BitmapToByte *******************/
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	/**
	 * @Description:以十六进制查看二进制文件
	 * @param @param data
	 * @param @return
	 * @return String
	 */
	public static String byteConvertHex(byte[] dataBytes) {
		if (dataBytes == null || dataBytes.length <= 0) {
			return null;
		}

		StringBuilder result = new StringBuilder();
		for (byte b : dataBytes) {
			result.append(String.format("%02x", b));
		}
		return result.toString();
	}
}
