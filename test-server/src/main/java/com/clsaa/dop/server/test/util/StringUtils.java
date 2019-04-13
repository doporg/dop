package com.clsaa.dop.server.test.util;

import com.clsaa.dop.server.test.doExecute.plugin.UrlParamResolvePlugin;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author xihao
 * @version 1.0
 * @since 12/04/2019
 */
@Slf4j
public class StringUtils {

    private static String dollarRegex = "\\S*\\$\\{\\S+\\}\\S*";
    private static Pattern pattern = Pattern.compile(dollarRegex);

    // 带有${}变量的字符串, 进行变量替换
    public static String tryToResolve(String origin, Map<String, String> data) {
        if (pattern.matcher(origin).matches()) {
            try {
                Template template = new Template("tpl", origin, new Configuration(Configuration.VERSION_2_3_28));
                StringWriter resultWriter = new StringWriter();
                template.process(data, resultWriter);
                return resultWriter.toString();
            } catch (IOException | TemplateException e) {
                throw new RuntimeException(
                        String.format("[resolve EL string with param error!]: origin: %s, error message: %s",
                                origin, e.getMessage())
                );
            }
        }
        return origin;
    }

}
