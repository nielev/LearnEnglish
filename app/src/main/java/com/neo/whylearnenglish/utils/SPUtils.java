package com.neo.whylearnenglish.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import org.json.JSONArray;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

/**
 * SharePreference工具类
 * 
 * @author Nielev
 * @company Oray
 * @version 2016年1月28日 下午4:43:42
 */

public class SPUtils {
	/**
	 * 存入List集合
	 * 
	 * @param key
	 * @param datas
	 * @param ctx
	 */
	public static void putStringList(String key, List<String> datas, Context ctx) {
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < datas.size(); i++) {
			String item = datas.get(i);
			jsonArray.put(item);
		}
		SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		sp.edit().putString(key, jsonArray.toString()).commit();
	}
	/**
	 * 获得List集合
	 * @param key
	 * @param ctx
	 * @return
	 */
	public static List<String> getStringList(String key, Context ctx) {
		List<String> datas = new ArrayList<String>();
		SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		try {
			String result = sp.getString(key, "");
			if(!TextUtils.isEmpty(result)){
				JSONArray array = new JSONArray(result);
				for (int i = 0; i < array.length(); i++) {
					datas.add(array.getString(i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datas;
	}

	public static void putBoolean(String key, boolean value, Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}

	public static boolean getBoolean(String key, boolean defValue, Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getBoolean(key, defValue);
	}

	public static void putString(String key, String value, Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}

	public static String getString(String key, String defValue, Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getString(key, defValue);
	}

	public static void putInt(String key, int value, Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		sp.edit().putInt(key, value).commit();
	}

	public static int getInt(String key, int defValue, Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getInt(key, defValue);
	}

	public static void putLong(String key, long value, Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		sp.edit().putLong(key, value).commit();
	}

	public static long getLong(String key, long defValue, Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getLong(key, defValue);
	}

	public static void remove(String key, Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		sp.edit().remove(key).commit();
	}

	/**
	 * <ul>
	 * <li>将list集合转换成字符串</li>
	 * 
	 * @param SceneList
	 * @return
	 * @throws IOException
	 *             </ul>
	 */
	public static String SceneList2String(List SceneList) throws IOException {
		// 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		// 然后将得到的字符数据装载到ObjectOutputStream
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		// writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
		objectOutputStream.writeObject(SceneList);
		// 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
		String SceneListString = new String(Base64.encode(byteArrayOutputStream.toByteArray(),
				Base64.DEFAULT));
		// 关闭objectOutputStream
		objectOutputStream.close();
		return SceneListString;
	}

	// 将字符串转换成list集合

	/**
	 * <ul>
	 * <li>将字符串转成list集合</li>
	 * 
	 * @param SceneListString
	 * @return
	 * @throws StreamCorruptedException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 *             </ul>
	 */
	@SuppressWarnings("unchecked")
	public static List String2SceneList(String SceneListString) throws StreamCorruptedException,
			IOException, ClassNotFoundException {
		byte[] datas = Base64.decode(SceneListString.getBytes(), Base64.DEFAULT);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(datas);
		ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
		List SceneList = (List) objectInputStream.readObject();
		objectInputStream.close();
		return SceneList;
	}
	
	/** 
	 * 通过这个方法可以存储任意类型数据
	 * 让SharedPreference 不仅限于存储基本数据类型 
	 * 使用这个方法必须有个前提就是 你的javabean对象必须实现序列化接口
	 *  
	 * @param key
	 * @param obj
	 * @param ctx
	 */

	public static void putObject(String key, Object obj, Context ctx){
		SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try{
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(bos);
			// writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
			objectOutputStream.writeObject(obj);
			// 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
			String SceneListString = new String(Base64.encode(bos.toByteArray(),
					Base64.DEFAULT));
			sp.edit().putString(key, SceneListString).commit();
			objectOutputStream.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取Object中数据
	 * @param key
	 * @param context
	 * @return
	 */
	public static Object getObject(String key, Context context){
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		String SceneListString = sp.getString(key,"");
		byte[] datas = Base64.decode(SceneListString.getBytes(), Base64.DEFAULT);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(datas);
		try{
			 ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
			 Object obj =  objectInputStream.readObject();
			 objectInputStream.close();
			 return obj;
		}catch(Exception e){
			//e.printStackTrace();
			return null;
		}
	
	}
	
}
