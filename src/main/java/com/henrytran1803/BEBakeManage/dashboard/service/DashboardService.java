package com.henrytran1803.BEBakeManage.dashboard.service;

import com.henrytran1803.BEBakeManage.quycode.BillStatus;
import com.henrytran1803.BEBakeManage.quycode.entity.Bill;
import com.henrytran1803.BEBakeManage.quycode.entity.BillDetail;
import com.henrytran1803.BEBakeManage.quycode.repository.BillDetailRepository;
import com.henrytran1803.BEBakeManage.quycode.repository.BillRepository;
import com.henrytran1803.BEBakeManage.user.dto.DashBoardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // This will create a constructor with final fields
public class DashboardService {
    @Autowired
    private final BillRepository billRepository;
    @Autowired
    private final BillDetailRepository billDetailRepository;

    public DashBoardDTO getDashboardData() {
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime startOfMonth = today.minusMonths(6); // Get data for last 6 months

        DashBoardDTO dashboard = new DashBoardDTO();

        // Get today's metrics
        List<Bill> todayBills = billRepository.findByBillStatusAndCreatedAtBetween(
                BillStatus.COMPLETED,
                today,
                today.plusDays(1)
        );

        // Set today's revenue and bill count
        dashboard.setTodayRevenue(todayBills.stream()
                .mapToDouble(Bill::getTotalAmount)
                .sum());
        dashboard.setTodayBill(todayBills.size());

        // Get monthly revenue data
        List<Bill> allBills = billRepository.findByBillStatusAndCreatedAtGreaterThanEqual(
                BillStatus.COMPLETED,
                startOfMonth
        );

        // Calculate monthly revenue
        Map<String, List<Bill>> billsByMonth = allBills.stream()
                .collect(Collectors.groupingBy(bill ->
                        bill.getCreatedAt().format(DateTimeFormatter.ofPattern("MM/yyyy"))
                ));

        List<DashBoardDTO.MonthRevenueDTO> monthlyRevenue = billsByMonth.entrySet().stream()
                .map(entry -> new DashBoardDTO.MonthRevenueDTO(
                        entry.getKey(),
                        entry.getValue().stream().mapToDouble(Bill::getTotalAmount).sum(),
                        entry.getValue().size()
                ))
                .sorted((a, b) -> {
                    // Sort by month/year
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
                    LocalDate dateA = LocalDate.parse("01/" + a.getMonth(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    LocalDate dateB = LocalDate.parse("01/" + b.getMonth(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    return dateA.compareTo(dateB);
                })
                .collect(Collectors.toList());

        dashboard.setMonthRevenue(monthlyRevenue);

        // Calculate hourly orders for today
        Map<String, Integer> hourlyOrders = todayBills.stream()
                .collect(Collectors.groupingBy(
                        bill -> bill.getCreatedAt().format(DateTimeFormatter.ofPattern("HH:00")),
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));
        dashboard.setHourlyOrders(hourlyOrders);

        // Calculate category revenue
        Map<String, List<BillDetail>> categoryDetails = allBills.stream()
                .flatMap(bill -> bill.getBillDetails().stream())
                .collect(Collectors.groupingBy(detail ->
                        detail.getProductBatch().getProduct().getName()
                ));

        double totalRevenue = categoryDetails.values().stream()
                .flatMap(List::stream)
                .mapToDouble(detail -> detail.getPrice() * detail.getQuantity())
                .sum();

        List<DashBoardDTO.ProductCategoryRevenueDTO> categoryRevenue = categoryDetails.entrySet().stream()
                .map(entry -> {
                    double categoryTotal = entry.getValue().stream()
                            .mapToDouble(detail -> detail.getPrice() * detail.getQuantity())
                            .sum();
                    return new DashBoardDTO.ProductCategoryRevenueDTO(
                            entry.getKey(),
                            categoryTotal,
                            (categoryTotal / totalRevenue) * 100
                    );
                })
                .collect(Collectors.toList());
        dashboard.setCategoryRevenue(categoryRevenue);

        // Find best selling product
        Map<String, Long> productSales = allBills.stream()
                .flatMap(bill -> bill.getBillDetails().stream())
                .collect(Collectors.groupingBy(
                        detail -> detail.getProductBatch().getProduct().getName(),
                        Collectors.summingLong(BillDetail::getQuantity)
                ));

        String bestSeller = productSales.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No sales yet");
        dashboard.setProductNameBestSeller(bestSeller);

        return dashboard;
    }
}