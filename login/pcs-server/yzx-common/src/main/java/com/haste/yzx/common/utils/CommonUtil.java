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
     * Verify that the mobile number is valid.
     *
     * @param phoneNumber Mobile phone number to verify
     * @return Returns true if the phone number is valid, otherwise returns false
     */
    public static Boolean isValidPhoneNumber(String phoneNumber) {
        // Define the regular expression for mobile phone numbers in mainland China
        String regex = "^\\+86(1[3-9]\\d{9})$";
        Pattern pattern = Pattern.compile(regex);

        // Use the matcher method to match mobile phone numbers
        return pattern.matcher(phoneNumber).matches();
    }

}
