package com.henrytran1803.BEBakeManage.dashboard.controller;
import com.henrytran1803.BEBakeManage.dashboard.service.DashboardService;
import com.henrytran1803.BEBakeManage.user.dto.DashBoardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashBoardDTO> getDashboard() {
        return ResponseEntity.ok(dashboardService.getDashboardData());
    }
}