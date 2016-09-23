package xie.com.androidcommon.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import org.w3c.dom.Text;

import xie.com.androidcommon.MyApplication;

/**
 * 文件操作工具类
 * 
 * @author xiechengfa
 * 
 */
public class FileUtils {
	private final static int SDCARD_STATE_OK = 0;// 正常
	private final static int SDCARD_STATE_NONE = 1;// 没有SD卡
	private final static int SDCARD_STATE_LACK = 2;// 空间不足
	// 一小时的MS
	private final static long HOUR_TIME = 60 * 60 * 1000;

	/**
	 * 在SD卡上创建文件
	 * @return
	 * @throws IOException
	 */
	public static File createSDFile(String path) throws IOException {
		File file = new File(path);
		if (!file.exists()) {
			file.createNewFile();
		}

		return file;
	}

	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirName
	 * @return
	 */
	public static File createSDDir(String dirName) {
		File file = new File(dirName);
		if (!file.exists())
			file.mkdir();
		return file;
	}

	/**
	 * 判断SD卡上的文件是否存在
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean isFileExist(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return false;
		}

		File file = new File(filePath);
		return file.exists();
	}

	/**
	 * 创建文件目录(包含文件的父文件夹)
	 * 
	 * @param filePath
	 */
	public static void createFileDir(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			File parentFile = file.getParentFile();
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			parentFile = null;
		}
		file = null;
	}


	/**
	 * 删除文件（不是目录）
	 * @param fileName
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static boolean deleteFile(String fileName) {
		try {
			if (fileName == null) {
				return false;
			}

			File f = new File(fileName);
			if (f == null || !f.exists()) {
				return false;
			}

			if (f.isDirectory()) {
				return false;
			}

			return f.delete();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 删除文件夹下的文件(不含当前文件夹)
	 * 
	 * @param dirName
	 *            源文件或目录
	 * @param isRecurse
	 *            如果是目录,是否删除其下的子目录
	 * @return true-成功,false-失败
	 */
	public static boolean deleteFileOfDir(String dirName, boolean isRecurse) {
		boolean blret = false;
		try {
			File f = new File(dirName);
			if (f == null || !f.exists()) {
				return false;
			}

			if (f.isFile()) {
				blret = f.delete();
				return blret;
			} else {
				File[] flst = f.listFiles();
				if (flst == null || flst.length <= 0) {
					return true;
				}

				int filenumber = flst.length;
				File[] fchilda = f.listFiles();
				for (int i = 0; i < filenumber; i++) {
					File fchild = fchilda[i];
					if (fchild.isFile()) {
						blret = fchild.delete();
						if (!blret) {
							break;
						}
					} else if (isRecurse) {
						blret = deleteFileDir(fchild.getAbsolutePath(), true);
					}
				}
			}
		} catch (Exception e) {
			// Log.d(FILE_TAG, e.getMessage());
			blret = false;
		}

		return blret;
	}

	/**
	 * 删除文件夹(含当前文件夹)
	 * 
	 * @param dirName
	 *            源文件或目录
	 * @param isRecurse
	 *            如果是目录,是否删除其下的子目录
	 * @return true-成功,false-失败
	 */
	public static boolean deleteFileDir(String dirName, boolean isRecurse) {
		boolean blret = false;
		try {
			File f = new File(dirName);
			if (f == null || !f.exists()) {
				return false;
			}
			if (f.isFile()) {
				blret = f.delete();
				return blret;
			} else {
				File[] flst = f.listFiles();
				if (flst == null || flst.length <= 0) {
					f.delete();
					return true;
				}
				int filenumber = flst.length;
				File[] fchilda = f.listFiles();
				for (int i = 0; i < filenumber; i++) {
					File fchild = fchilda[i];
					if (fchild.isFile()) {
						blret = fchild.delete();
						if (!blret) {
							break;
						}
					} else if (isRecurse) {
						blret = deleteFileDir(fchild.getAbsolutePath(), true);
					}
				}

				// 删除当前文件夹
				blret = new File(dirName).delete();
			}
		} catch (Exception e) {
			// Log.d(FILE_TAG, e.getMessage());
			blret = false;
		}
		return blret;
	}

	/**
	 * 移动文件
	 * 
	 * @param filePath
	 */
	public static void removeToDir(String filePath, String toFilePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			return;
		}
		file.renameTo(new File(toFilePath));
	}

	/**
	 * 复制文件
	 * 
	 * @param from
	 * @param to
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void copyFile(String from, String to) {
		FileInputStream is = null;
		FileOutputStream os = null;
		try {
			is = new FileInputStream(from);
			byte[] buffer = new byte[1024];
			// 删除原文件
			deleteFile(to);
			os = new FileOutputStream(to);
			int i = 0;
			while ((i = is.read(buffer)) != -1) {
				os.write(buffer, 0, i);
			}
			os.flush();
			buffer = null;
		} catch (Exception ie) {
			ie.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (Exception ex) {
			}
			try {
				if (os != null) {
					os.close();
				}
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * 复制文件夹
	 * 
	 * @param srcFileDir
	 * @param destFileDir
	 */
	public static void copyDirToDir(String srcFileDir, String destFileDir) {
		String[] srcFiles = getFileNames(srcFileDir);
		try {
			if (srcFiles == null || srcFiles.length == 0) {
				return;
			} else {
				for (int i = 0; i < srcFiles.length; i++) {
					if (!srcFiles[i].endsWith(".zip")) {
						copyFile(srcFileDir + "\\" + srcFiles[i], destFileDir
								+ "\\" + srcFiles[i]);
					}
				}
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 根据文件路径取得该文件路径下的所有文件名称：
	 * 
	 * @param filePath
	 * @return 存放文件名称的数组
	 */
	public static String[] getFileNames(String filePath) {
		if (filePath == null || filePath.length() <= 0) {
			return null;
		}

		File f = new File(filePath);
		if (f.isFile()) {
			return null;
		}

		String[] flName = f.list();
		f = null;

		return flName;
	}

	/**
	 * 将一个InputStream里面的数据写入文件
	 * 
	 * @param inputStream
	 * @return
	 */
	public static File writeFile(File file, InputStream inputStream) {
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = inputStream.read(temp)) > 0) {
				outputStream.write(temp, 0, i);
			}
			// byte buffer[] = new byte[1024];
			// while ((inputStream.read(buffer) != -1)) {
			// outputStream.write(buffer);
			// }
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return file;
	}


	/**
	 * 检查SDCard是否存在
	 * @return
	 */
	public static boolean isExistsStorage() {
		// 是否存在外存储器(优先判断)
		// Environment.MEDIA_MOUNTED // sd卡在手机上正常使用状态
		// Environment.MEDIA_UNMOUNTED // 用户手工到手机设置中卸载sd卡之后的状态
		// Environment.MEDIA_REMOVED // 用户手动卸载，然后将sd卡从手机取出之后的状态
		// Environment.MEDIA_BAD_REMOVAL // 用户未到手机设置中手动卸载sd卡，直接拨出之后的状态
		// Environment.MEDIA_SHARED // 手机直接连接到电脑作为u盘使用之后的状态
		// Environment.MEDIA_CHECKINGS // 手机正在扫描sd卡过程中的状态

		boolean res = false;
		String exStorageState = Environment.getExternalStorageState();
		if (exStorageState != null
				&& Environment.MEDIA_SHARED.equals(exStorageState)) {
			// 有SD卡，手机直接连接到电脑作为u盘使用之后的状态
			return false;
		}

		if (exStorageState != null
				&& Environment.MEDIA_MOUNTED.equals(exStorageState)) {
			// sd卡在手机上正常使用状态
			res = true;
		} else {
			// 如果外存储器不存在，则判断内存储器
			res = isExitInternalStorage();
		}

		return res;
	}

	// 是否存在内存储器
	private static boolean isExitInternalStorage() {
		File file = MyApplication.getInstance().getFilesDir();
		if (file != null) {
			return true;
		}
		return false;
	}

	// // 是否存在外存在器
	// private static boolean externalStorageState() {
	// if (Environment.getExternalStorageState().equals(
	// android.os.Environment.MEDIA_MOUNTED)) {
	// return true;
	// }
	//
	// return false;
	// }

	/**
	 * 获取SDCard的路径
	 * 
	 * @return
	 */
	public static String getStoragePath() {
		/* 检测是否存在SD卡 */
		File filePath = null;
		String exStorageState = Environment.getExternalStorageState();
		if (exStorageState != null
				&& Environment.MEDIA_SHARED.equals(exStorageState)) {
			// 有SD卡，手机直接连接到电脑作为u盘使用之后的状态
			return null;
		}

		if (exStorageState != null
				&& Environment.MEDIA_MOUNTED.equals(exStorageState)) {
			// sd卡在手机上正常使用状态
			// 是否存在外存储器(优先判断)
			filePath = Environment.getExternalStorageDirectory();
		} else if (isExitInternalStorage()) {
			// 如果外存储器不存在，则判断内存储器
			filePath = MyApplication.getInstance().getFilesDir();
		}

		if (filePath != null) {
			return filePath.getPath() + "/";
		} else {
			return null;
		}
	}

	// 是否还有可使用的储蓄空间，低于10M则认为没有满足程序使用的储蓄空间
	public static int isAvailableMermory() {
		int state = SDCARD_STATE_OK;
		String exStorageState = Environment.getExternalStorageState();
		if (exStorageState != null
				&& Environment.MEDIA_SHARED.equals(exStorageState)) {
			// 有SD卡，手机直接连接到电脑作为u盘使用之后的状态
			return SDCARD_STATE_NONE;
		}

		if (exStorageState != null
				&& Environment.MEDIA_MOUNTED.equals(exStorageState)) {
			// sd卡在手机上正常使用状态
			// 是否存在外存储器(优先判断)
			File file = Environment.getExternalStorageDirectory();
			if (isAvailableMermoryOfFile(file)) {
				state = SDCARD_STATE_OK;
			} else {
				state = SDCARD_STATE_LACK;
			}
		} else if (isExitInternalStorage()) {
			// 如果外存储器不存在，则判断内存储器
			File file = MyApplication.getInstance().getFilesDir();
			if (isAvailableMermoryOfFile(file)) {
				state = SDCARD_STATE_OK;
			} else {
				state = SDCARD_STATE_LACK;
			}
		}

		return state;
	}

	// 指定的文件是否有可用的空间
	private static boolean isAvailableMermoryOfFile(File file) {
		if (file == null || file.getPath() == null) {
			return false;
		}

		StatFs statfs = new StatFs(file.getPath());
		long blockSize = statfs.getBlockSize();
		long availableBlocks = statfs.getAvailableBlocks();
		int availableMermory = (int) ((availableBlocks * blockSize) / 1024 / 1024);
		file = null;

		return availableMermory >= 10;
	}


	/**
	 * 读取文本文件内容
	 * 
	 * @param path
	 *            文件
	 * @return 返回文本文件的内容
	 */
	public static byte[] readFileOfByte(String path) {
		if (path == null || path.trim().length() <= 0) {
			return null;
		}

		byte[] res = null;
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(path);
			res = new byte[fs.available()];
			fs.read(res, 0, res.length);
		} catch (IOException es) {
			res = null;
		} finally {
			if (fs != null) {
				try {
					fs.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				fs = null;
			}
		}
		return res;
	}

	/**
	 * 读取assets的文件
	 * @return
	 */
	public static String readAssetsFile(String fileName) {
		String res = null;

		InputStream inputStream = null;
		try {
			inputStream = MyApplication.getInstance().getAssets()
					.open(fileName);
			int len;
			len = inputStream.available();
			byte[] arrayOfByte = new byte[len];
			inputStream.read(arrayOfByte);
			res = new String(arrayOfByte, "UTF-8");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				inputStream = null;
			}
		}

		return res;
	}

	/**
	 * 读取文本文件内容
	 *
	 * @param path
	 *            文件
	 * @param encoding
	 *            文本文件打开的编码方式
	 * @return 返回文本文件的内容
	 */
	public static String readFile(String path, String encoding) {
		encoding = encoding.trim();
		StringBuffer str = new StringBuffer("");
		String st = "";
		try {
			FileInputStream fs = new FileInputStream(path);
			InputStreamReader isr = null;
			if (encoding.equals("") || encoding == null) {
				isr = new InputStreamReader(fs);
			} else {
				isr = new InputStreamReader(fs, encoding);
			}
			BufferedReader br = new BufferedReader(isr);
			try {
				String data = "";
				while ((data = br.readLine()) != null) {
					str.append(data + "\r\n");
				}
			} catch (Exception e) {
				str.append(e.toString());
			}
			st = str.toString();
			str = null;
		} catch (IOException es) {
			st = "";
		}
		return st;
	}

	/**
	 * 读取文本文件内容
	 * 
	 * @param fs
	 *            文件输入流
	 * @param encoding
	 *            文本文件打开的编码方式
	 * @return 返回文本文件的内容
	 */
	public static String readFile(InputStream fs, String encoding)
			throws IOException {
		encoding = encoding.trim();
		StringBuffer str = new StringBuffer("");
		String st = "";
		try {
			// FileInputStream fs = new FileInputStream(filePathAndName);
			InputStreamReader isr = null;
			if (encoding.equals("") || encoding == null) {
				isr = new InputStreamReader(fs);
			} else {
				isr = new InputStreamReader(fs, encoding);
			}
			BufferedReader br = new BufferedReader(isr);
			try {
				String data = "";
				while ((data = br.readLine()) != null) {
					str.append(data + "\r\n");
				}
			} catch (Exception e) {
				str.append(e.toString());
			}
			st = str.toString();
			str = null;
		} catch (IOException es) {
			st = "";
		}
		return st;
	}

	/**
	 * 写入文件
	 * 
	 * @param path
	 *            文件名
	 */
	public static void writeFileOfDelExists(String path, byte[] in) {
		if (in == null || in.length <= 0) {
			return;
		}

		FileOutputStream ios = null;
		File f = null;
		try {
			f = new File(path);
			if (f.exists()) {
				f.delete();
			}
			f.createNewFile();

			ios = new FileOutputStream(f);
			ios.write(in, 0, in.length);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ios != null) {
				try {
					ios.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ios = null;
			}
			if (f != null) {
				f = null;
			}
		}
	}

	/**
	 * 写入文件
	 * 
	 * @param path
	 *            文件名
	 * @throws Exception
	 */
	public static void writeFileOfNODelExists(String path, byte[] in) {
		if (in == null || in.length <= 0) {
			return;
		}

		// 创建文件目录
		FileUtils.createFileDir(path);

		FileOutputStream ios = null;
		File f = null;
		try {
			f = new File(path);
			if (f.exists()) {
				return;
			}
			f.createNewFile();

			ios = new FileOutputStream(f);
			ios.write(in, 0, in.length);
			ios.flush();
		} catch (Exception e) {
			e.printStackTrace();
			// 若写文件失败，则删除脏文件
			deleteFile(path);
		} finally {
			if (ios != null) {
				try {
					ios.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ios = null;
			}
			if (f != null) {
				f = null;
			}
		}
	}

	/**
	 * 写入文件(如有原文件,不删除原文件,新内容追加到原文件)
	 * 
	 * @param path
	 *            文件名
	 * @param content
	 *            内容
	 */
	public static void writeFileOfNoDelExist(String path, String content) {
		FileUtils.createFileDir(path);

		String s = new String();
		String s1 = new String();
		// 构建文件路径
		// String filePath = getSDCardPath() + path;
		try {
			File f = new File(path);
			if (f.exists()) {
			} else {
				f.createNewFile();
			}

			FileInputStream ios = new FileInputStream(f);
			InputStreamReader isr = new InputStreamReader(ios, "UTF-8");
			BufferedReader input = new BufferedReader(isr);
			while ((s = input.readLine()) != null) {
				s1 += s + "\r\n";
			}
			input.close();
			s1 += content;

			FileOutputStream fos = new FileOutputStream(f);
			Writer out = new OutputStreamWriter(fos, "UTF-8");
			out.write(s1);
			out.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 写入文件(删除原文件)
	 * 
	 * @param path
	 *            文件名
	 * @param content
	 *            内容
	 */
	public static void writeFileOfDelExist(String path, String content) {
		if (path == null || path.trim().length() <= 0) {
			return;
		}

		if (content == null) {
			return;
		}

		FileUtils.createFileDir(path);

		String s = new String();
		String s1 = new String();
		try {
			File f = new File(path);
			if (f.exists()) {
				f.delete();
			}
			f.createNewFile();

			FileInputStream ios = new FileInputStream(f);
			InputStreamReader isr = new InputStreamReader(ios, "UTF-8");
			BufferedReader input = new BufferedReader(isr);
			while ((s = input.readLine()) != null) {
				s1 += s + "\r\n";
			}
			input.close();
			s1 += content;

			FileOutputStream fos = new FileOutputStream(f);
			Writer out = new OutputStreamWriter(fos, "UTF-8");
			out.write(s1);
			out.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * Writes content to internal storage making the content private to the
	 * application. The method can be easily changed to take the MODE as
	 * argument and let the caller dictate the visibility: MODE_PRIVATE,
	 * MODE_WORLD_WRITEABLE, MODE_WORLD_READABLE, etc.
	 * 
	 * @param filename
	 *            - the name of the file to create
	 * @param content
	 *            - the content to write
	 */
	public static void writeInternalStoragePrivate(String filename,
			byte[] content) {
		FileOutputStream fos = null;
		try {
			// MODE_PRIVATE creates/replaces a file and makes
			// it private to your application. Other modes:
			// MODE_WORLD_WRITEABLE
			// MODE_WORLD_READABLE
			// MODE_APPEND
			fos = MyApplication.getInstance().openFileOutput(filename,
					Context.MODE_PRIVATE);
			fos.write(content);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				fos = null;
			}
		}
	}

	/**
	 * Reads a file from internal storage
	 * 
	 * @param filename
	 *            the file to read from
	 * @return the file content
	 */
	public static byte[] readInternalStoragePrivate(String filename) {
		int len = 1024;
		FileInputStream fis = null;
		byte[] buffer = new byte[len];
		try {
			fis = MyApplication.getInstance().openFileInput(filename);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int nrb = fis.read(buffer, 0, len); // read up to len bytes
			while (nrb != -1) {
				baos.write(buffer, 0, nrb);
				nrb = fis.read(buffer, 0, len);
			}
			buffer = baos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				fis = null;
			}
		}
		return buffer;
	}

	// 删除应用程序目录下的文件
	public static void delInternalStoragePrivate(String fileName) {
		MyApplication.getInstance().deleteFile(fileName);
	}

	/**
	 * 重命名文件
	 * 
	 * @param sourcePath
	 * @param objPath
	 */
	public static void renameFile(String sourcePath, String objPath) {
		File sourceFile = new File(sourcePath);
		File objFile = new File(objPath);

		if (sourceFile != null && !sourceFile.exists()) {// 源文件不存在，则返回
			return;
		}

		if (objFile != null && objFile.exists()) {// 目标文件存在，则删除
			objFile.delete();
		}

		sourceFile.renameTo(objFile);

		sourceFile = null;
		objFile = null;
	}

	/*
	 * 从Assets中读取图片
	 */
	public static Drawable getDrawableFromAssetsFile(String fileName) {
		return new BitmapDrawable(getBitmapFromAssetsFile(fileName));

	}

	/*
	 * 从Assets中读取图片
	 */
	public static Bitmap getBitmapFromAssetsFile(String fileName) {
		Bitmap image = null;
		AssetManager am = MyApplication.getInstance().getResources()
				.getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	/**
	 * 清除缓存
	 * 
	 * @param filePath
	 *            相对路径
	 * @param hours
	 *            （单位：小时）
	 */
	public static void delCache(String filePath, float hours) {
		try {
			File f = new File(FileUtils.getStoragePath() + filePath);
			if (f == null || !f.exists()) {
				return;
			}

			if (f.isFile()) {
				delCacheFileOfTime(f, hours);
			} else {
				File[] flst = f.listFiles();
				if (flst == null || flst.length <= 0) {
					return;
				}

				int filenumber = flst.length;
				for (int i = 0; i < filenumber; i++) {
					File fchild = flst[i];
					if (fchild.isFile()) {
						delCacheFileOfTime(fchild, hours);
					} else {
						deleteFileDirOfCache(fchild.getAbsolutePath(), hours);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 删除文件文件夹(含当前文件夹)
	private static void deleteFileDirOfCache(String dirName, float hours) {
		try {
			File f = new File(dirName);
			if (f == null || !f.exists()) {
				return;
			}

			if (f.isFile()) {
				delCacheFileOfTime(f, hours);
			} else {
				File[] flst = f.listFiles();
				if (flst == null || flst.length <= 0) {
					f.delete();
				} else {
					int filenumber = flst.length;
					for (int i = 0; i < filenumber; i++) {
						File fchild = flst[i];
						if (fchild.isFile()) {
							delCacheFileOfTime(fchild, hours);
						} else {
							deleteFileDirOfCache(fchild.getAbsolutePath(),
									hours);
						}
					}
					// 删除当前文件夹
					f.delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 删除指定时间间隔的文件
	private static void delCacheFileOfTime(File file, float hours) {
		if (file == null) {
			return;
		}

		if (Math.abs(System.currentTimeMillis() - file.lastModified()) >= hours
				* HOUR_TIME) {
			file.delete();
		}
	}

	/*** 获取文件夹大小 ***/
	public static long getFileDirectorySize(String path) {
		long size = 0;
		File f = new File(path);
		if (f == null || !f.exists()) {
			return size;
		}

		try {
			File flist[] = f.listFiles();
			for (int i = 0; i < flist.length; i++) {
				if (flist[i].isDirectory()) {
					size = size + getFileDirectorySize(flist[i].getPath());
				} else {
					size = size + flist[i].length();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	/**
	 * 文件最后修改的时间
	 * 
	 * @param path
	 * @return
	 */
	public static long getFileModifyLastTime(String path) {
		File f = new File(path);
		if (f == null || !f.exists()) {
			return System.currentTimeMillis();
		}

		return f.lastModified();
	}

	/**
	 * 通过文件路径获得文件名
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileNameFromPath(String path) {
		File file = new File(path);
		if (file.exists()) {
			String name = file.getName();
			if (name.lastIndexOf(".") != -1) {
				// 带后缀名
				return name.substring(0, name.lastIndexOf("."));
			} else {
				return name;
			}
		} else {
			return null;
		}
	}

	/**
	 * 通过Url获得文件名
	 */
	public static String getFileNameFromUrl(String url) {
		String fileName = "";

		if (url == null || url.trim().length() <= 0) {
			return fileName;
		}

		int start = url.lastIndexOf("/") + 1;
		int end = url.lastIndexOf(".");
		if (end <= 0) {
			end = url.length();
		}

		if (start > 0 && end > 0 && end > start) {
			fileName = url.substring(start, end);
		}

		return fileName;
	}

	/**
	 * 通过Url获得文件名(带后缀名)
	 */
	public static String getFileNameOfTailFromUrl(String url) {
		String fileName = "";

		if (url == null || url.trim().length() <= 0) {
			return fileName;
		}

		int start = url.lastIndexOf("/") + 1;
		int end = url.length();

		if (start > 0 && end > 0 && end > start) {
			fileName = url.substring(start, end);
		}

		return fileName;
	}

	/**
	 * 获取命令URL的命令名
	 * 
	 * @param url
	 * @return
	 */
	public static String getOrderNameFromUrl(String url) {
		String orderName = "";
		if (url == null || url.trim().length() <= 0) {
			return orderName;
		}

		int start = "http://".length();
		int end = url.length();

		if (start > 0 && end > 0 && end > start) {
			orderName = url.substring(start, end);
		}
		return orderName;
	}

	/**
	 * 读取raw里文件，返回字节数组
	 * 
	 * @param resId
	 * @return
	 */
	public static byte[] readRawFileOfByte(int resId) {
		InputStream fis = null;
		byte[] res = null;
		try {
			fis = MyApplication.getInstance().getResources()
					.openRawResource(resId);
			res = new byte[fis.available()];
			fis.read(res, 0, res.length);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
					fis = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return res;
	}

	/**
	 * 读取raw里文件，返回字符串
	 * 
	 * @param resId
	 * @return
	 */
	public static String readRawFileOfStr(int resId) {
		InputStream in = null;
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer();
		sb.append("");
		try {
			in = MyApplication.getInstance().getResources()
					.openRawResource(resId);
			String str = null;
			br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			while ((str = br.readLine()) != null) {
				sb.append(str);
				sb.append("|");
			}
		} catch (NotFoundException e) {
			// Toast.makeText(this, "文本文件不存在", 100).show();
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
