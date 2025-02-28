package com.advanceit.bankservice.service;

import com.advanceit.bankservice.dto.AccountReportDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface ReportService {
    List<AccountReportDTO> generateAccountReport(LocalDateTime startDate, LocalDateTime endDate);
}
