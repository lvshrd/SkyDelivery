package com.sky.service;

import com.sky.vo.TurnoverReportVO;

import java.time.LocalDate;

public interface ReportService {

    /**
     * 统计指定时间区域内的营业额
     * @param beginDate
     * @param endDate
     * @return
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate beginDate, LocalDate endDate);
}
