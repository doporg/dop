package com.clsaa.dop.server.code.util;


import com.clsaa.dop.server.code.model.dto.ProjectDto;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.clsaa.dop.server.code.util.HttpRequestUtil.httpGet;


/**
 * json字符串与java对象转换工具类
 * @author wsy
 */
public class JsonUtil {

    private static final String api = "http://gitlab.dop.clsaa.com/api/v4";
    private static final String privateToken = "y5MJTK9yisBKfNF1t-gd";


    /**
     * 将json字符串转换为java对象，java对象的字段可以是json对象的字段的子集
     * @param jsonStr
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonStrToList(String jsonStr, Class<T> clazz) {

        //获得类的所有字段
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
        }

        JSONArray jsonArray = JSONArray.fromObject(jsonStr);
        List<T> result = new ArrayList<>();

        for (int i = 0; i < jsonArray.size(); i++) {

            JSONObject jsonObject = (JSONObject) jsonArray.get(i);

            T o;
            try {
                o = clazz.newInstance();
                //将需要字段的值注入对象
                for (Field f : fields) {
                    f.set(o, jsonObject.get(f.getName()));
                }
                result.add(o);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }


        }

        return result;
    }


    public static void main(String[] args) throws IllegalAccessException, InstantiationException {

        String jsonStr = httpGet("/users/root/projects");
//        JSONObject jsonObject = (JSONObject) JSONArray.fromObject(jsonStr).get(0);
        List<ProjectDto> list=jsonStrToList(jsonStr,ProjectDto.class);
        for(ProjectDto temp:list){
            System.out.println(temp);

        }

//        Class clazz = ProjectDto.class;
//
//        Field[] fields = clazz.getDeclaredFields();
//
//        for (Field f : fields) {
//            System.out.println(f.getName());
//            System.out.println(jsonObject.get(f.getName()));
//        }
//
//        Object o = clazz.newInstance();


    }

//    public static <T> List<T> jsonStrToList(String jsonStr, Class<T> clazz){
//
//
//
//
//
//
//    }

}
