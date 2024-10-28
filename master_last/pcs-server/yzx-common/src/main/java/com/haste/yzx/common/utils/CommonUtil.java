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
     * Verify phone number
     *
     * @param phoneNumber 
     * @return if valid, return true，else, return false
     */
    public static Boolean isValidPhoneNumber(String phoneNumber) {
        // Expression for Australian phone number
        String regex = "^\\+61\\d{9}$";
        Pattern pattern = Pattern.compile(regex);

        // Using matcher to match phone number
        return pattern.matcher(phoneNumber).matches();
    }

}
