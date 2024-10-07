package com.haste.yzx.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.regex.Pattern;

public class CommonUtil {
    public static String getClientIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    /**
     * 验证手机号码是否有效。
     *
     * @param phoneNumber 要验证的手机号码
     * @return 如果手机号码有效返回true，否则返回false
     */
    public static Boolean isValidPhoneNumber(String phoneNumber) {
        // 定义中国大陆手机号码的正则表达式
        String regex = "^\\+86(1[3-9]\\d{9})$";
        Pattern pattern = Pattern.compile(regex);

        // 使用matcher方法来匹配手机号码
        return pattern.matcher(phoneNumber).matches();
    }

}
