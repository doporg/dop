package cn.com.devopsplus.dop.server.pipeline.util;

import org.apache.commons.text.StringSubstitutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StepUtil {
     static public List<String> wrapperWithDirectory(String directory, List<String> steps) {
         List<String> ret = new ArrayList<>();
         ret.add(String.format("dir(\"%s\") {", directory));
         ret.addAll(steps);
         ret.add("}");

         return ret;
    }

    static public String generate(Map<String, String> valuesMap, List<String> steps) {
        StringSubstitutor sub = new StringSubstitutor(valuesMap);
        String joined = String.join(System.lineSeparator(), steps);
        return sub.replace(joined);
    }
}
