package com.clsaa.dop.server.code.util;
import com.clsaa.dop.server.code.model.bo.ProjectBo;



/**
 * json字符串与java对象转换工具类
 * @author wsy
 */
public class JsonUtil {


//    public static <T> List<T> jsonStrToList(String jsonStr, Class<T> clazz) {
//
//        //获得类的所有字段
//        Field[] fields = clazz.getDeclaredFields();
//        for (Field f : fields) {
//            f.setAccessible(true);
//        }
//
//        JSONArray jsonArray = JSONArray.fromObject(jsonStr);
//        List<T> result = new ArrayList<>();
//
//        for (int i = 0; i < jsonArray.size(); i++) {
//
//            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//
//            T o;
//            try {
//                o = clazz.newInstance();
//                //将需要字段的值注入对象
//                for (Field f : fields) {
//                    f.set(o, jsonObject.get(f.getName()));
//                }
//                result.add(o);
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//
//
//        }
//
//        return result;
//    }





}
