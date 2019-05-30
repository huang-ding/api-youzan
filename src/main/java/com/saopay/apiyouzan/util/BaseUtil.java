package com.saopay.apiyouzan.util;

import java.io.File;
import java.io.FileNotFoundException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;

/**
 * @author huangding
 * @description
 * @date 2018/11/26 14:58
 */
public class BaseUtil {


    /**
     * 获取当前访问路径
     */
    public static String getNotifyUrl(HttpServletRequest request, String urlPath) {
        StringBuffer url = request.getRequestURL();
        String tempContextUrl = url
            .delete(url.length() - request.getRequestURI().length(), url.length()).append(urlPath)
            .toString();
        return tempContextUrl;
    }


    /**
     * 获取excel模板
     */
    public static File getTemplateExcelFile(String templateFileName) {
        return getTemplateFile("excel/" + templateFileName);
    }

    /**
     * 获取图片模板
     */
    public static File getTemplateImageFile(String templateFileName) {
        return getTemplateFile("images/" + templateFileName);
    }

    private static File getTemplateFile(String templateFileName) {
        try {
            File file = ResourceUtils.getFile("classpath:" + templateFileName);
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 检查是否存在空字符串
     */
    public static boolean hasBlank(String... strs) {
        if (strs == null || strs.length == 0) {
            return true;
        } else {
            for (String str : strs) {
                if (StringUtils.isBlank(str)) {
                    return true;
                }
            }
            return false;
        }
    }
}
