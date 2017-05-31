package xie.com.androidcommon.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * @Desp:Json工具类
 * @author xiechengfa2000@163.com
 * @date 2015-10-12 下午3:52:38
 */
public class JsonUtils {
	/**
	 * 获取Json里的字符串值
	 * 
	 * @param json
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public static String getStr(JSONObject json, String key) {
		try {
			if (!json.isNull(key)) {
				return json.getString(key);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取Json里的整型值
	 * 
	 * @param json
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public static int getInt(JSONObject json, String key) {
		try {
			if (!json.isNull(key)) {
				return json.getInt(key);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 获取Json里的Long值
	 * 
	 * @param json
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public static long getLong(JSONObject json, String key) {
		try {
			if (!json.isNull(key)) {
				return json.getLong(key);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取Json里的boolean
	 * 
	 * @param json
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public static boolean getBoolean(JSONObject json, String key,
			String trueStr) {
		try {
			if (!json.isNull(key)) {
				return trueStr.equals(json.getString(key)) ? true : false;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取Json里的boolean
	 * 
	 * @param json
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public static boolean getBoolean(JSONObject json, String key) {
		try {
			if (!json.isNull(key)) {
				return json.getBoolean(key);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @Desp:获取json数组
	 * @param json
	 * @param key
	 * @return
	 * @Return:JSONArray
	 */
	public static JSONArray getArray(JSONObject json, String key) {
		try {
			if (!json.isNull(key)) {
				return json.getJSONArray(key);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * @Title: getJsonStr
	 * @Description: 生成jason串
	 * @param @param map
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String getJsonObjectStr(HashMap<String, Object> map) {
		String jsonStr = null;

		JSONObject jsonObject = new JSONObject();
		try {
			Iterator<Entry<String, Object>> iter = map.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, Object> entry = iter.next();
				String key = entry.getKey();
				Object value = entry.getValue();
				jsonObject.put(key, value);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		jsonStr = jsonObject.toString();
		return jsonStr;
	}
}
