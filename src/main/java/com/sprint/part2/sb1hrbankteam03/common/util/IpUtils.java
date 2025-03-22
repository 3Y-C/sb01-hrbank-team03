package com.sprint.part2.sb1hrbankteam03.common.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class IpUtils {

  public String getClientIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");

    if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    } else {
      // 프록시 거친 경우: 첫 번째 IP만 추출
      ip = ip.split(",")[0].trim();
    }

    // IPv6 루프백 처리
    if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
      ip = "127.0.0.1";
    }

    return ip;
  }
}