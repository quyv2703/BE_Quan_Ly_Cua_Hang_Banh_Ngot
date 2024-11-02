package com.henrytran1803.BEBakeManage.common.util;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class RoleBasedRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            System.out.println("Current role: " + role); // Log vai trò hiện tại
            return role;
        }
        System.out.println("No authentication available. Defaulting to ROLE_USER.");
        return "ROLE_USER"; // Chọn mặc định nếu không có thông tin xác thực
    }

}
