/**
 * JSON转换成List/Map对象
 */
package com.prism.action.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtil {
	public static void main(String[] arg) {
		JsonUtil ju = new JsonUtil();
		String json = "[{\"acc_no\":[{\"bank_code\":12,\"no\":\"1\\\"\\\"2345\"}],\"name\":\"X\",\"bank_code\":\"308\"}]";
		 @SuppressWarnings("unchecked")
		List<Object> list = (List<Object>)ju.toObject(json);
		System.out.println(list);
		Map<String,String> map = new HashMap<String,String>();
		map.put("a11", "A22");
		map.put("b11", "B22");
		map.put("c11", "C22");
		
		System.out.println(ju.toJson(map));
		System.out.println(ju.Json2List("[1,2,3,4,5]"));
	}

	public Object toObject(String json) {
		if (json.startsWith("[")) {
			return Json2List(json);
		} else {
			return Json2Map(json);
		}
	}
	public String toJson(Object obj){
		if(obj instanceof List){
			JSONArray jsonArray = JSONArray.fromObject(obj);
			return jsonArray.toString();			
		}else{
			JSONObject jsonObect = JSONObject.fromObject(obj);
			return jsonObect.toString();
		}
	}
	private List<Object> Json2List(String json) {
		List<Object> list = new ArrayList<Object>();
		JSONArray jsonArray = JSONArray.fromObject(json);
		for (int i = 0; i < jsonArray.size(); i++) {
			Object o = jsonArray.get(i);
			
			if (o instanceof JSONObject) {
				list.add(json2Map((JSONObject) o));
			} else if (o instanceof JSONArray) {
				list.add(json2List((JSONArray) o));
			} else {
				list.add(o);
			}
		}
		return list;
	}

	private Map<String, Object> Json2Map(String json) {
		Map<String, Object> result = new HashMap<String, Object>();
		JSONObject jsonObject = JSONObject.fromObject(json);
		@SuppressWarnings("unchecked")
		Iterator<Map.Entry<String, Object>> it = jsonObject.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> o = (Map.Entry<String, Object>) it.next();
			if (o.getValue() instanceof JSONObject) {
				result.put(o.getKey(), json2Map((JSONObject) o.getValue()));
			} else if (o.getValue() instanceof JSONArray) {
				result.put(o.getKey(), json2List((JSONArray) o.getValue()));
			} else {
				result.put(o.getKey(), o.getValue());
			}
		}
		return result;
	}

	private Map<String, Object> json2Map(JSONObject jsonObject) {
		Map<String, Object> result = new HashMap<String, Object>();
		@SuppressWarnings("unchecked")
		Iterator<Map.Entry<String, Object>> it = jsonObject.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> o = (Map.Entry<String, Object>) it.next();
			if (o.getValue() instanceof JSONObject) {
				result.put(o.getKey(), json2Map((JSONObject) o.getValue()));
			} else if (o.getValue() instanceof JSONArray) {
				result.put(o.getKey(), json2List((JSONArray) o.getValue()));
			} else {
				result.put(o.getKey(), o.getValue());
			}
		}
		return result;
	}

	private List<Object> json2List(JSONArray jsonArray) {
		List<Object> result = new ArrayList<Object>();
		for (int i = 0; i < jsonArray.size(); i++) {
			Object o = jsonArray.get(i);
			if (o instanceof JSONObject) {
				result.add(json2Map((JSONObject) o));
			} else if (o instanceof JSONArray) {
				result.add(json2List((JSONArray) o));
			} else {
				result.add(0);
			}
		}
		return result;
	}
}
