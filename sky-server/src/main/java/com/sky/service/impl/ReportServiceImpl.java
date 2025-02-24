package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

     /**
     * 统计指定时间区域内的营业额
     * @param beginDate
     * @param endDate
     * @return TurnoverReportVO
     */
    public TurnoverReportVO getTurnoverStatistics(LocalDate beginDate, LocalDate endDate) {
        List<LocalDate> dateList = new ArrayList<>();
        // 使用 for 循环生成日期列表
        for (LocalDate date = beginDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            dateList.add(date);
        }

        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map =new HashMap();
            map.put("beginTime",beginTime);
            map.put("endTime",endTime);
            map.put("status", Orders.COMPLETED);
            Double turnover = orderMapper.sumByMap(map);
            turnover = turnover==null ? 0.0 : turnover;
            turnoverList.add(turnover);
            //select sum(amount) from orders where order_time > ? and order_time< ? and status =5
        }
        //封装结果
        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    /**
     * 统计指定时间区域内的用户数据
     * @param beginDate
     * @param endDate
     * @return
     */
    public UserReportVO getUserStatistics(LocalDate beginDate, LocalDate endDate) {
        List<LocalDate> dateList = new ArrayList<>();
        // 使用 for 循环生成日期列表
        for (LocalDate date = beginDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            dateList.add(date);
        }

        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map =new HashMap();
            map.put("endDate",endTime);
            Integer totalUser = userMapper.countByMap(map);
            map.put("beginDate",beginTime);
            Integer newUser = userMapper.countByMap(map);

            newUserList.add(newUser);
            totalUserList.add(totalUser);
        }

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .totalUserList(StringUtils.join(totalUserList,","))
                .newUserList(StringUtils.join(newUserList,","))
                .build();
    }

     /**
     * 统计指定时间区域内的订单数据
     * @param beginDate
     * @param endDate
     * @return
     */
    public OrderReportVO getOrderStatistics(LocalDate beginDate, LocalDate endDate) {
        List<LocalDate> dateList = new ArrayList<>();
        // 使用 for 循环生成日期列表
        for (LocalDate date = beginDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            dateList.add(date);
        }

        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Integer orderCount = getOrderCount(beginTime, endTime, null);
            Integer validOrderCount = getOrderCount(beginTime, endTime, Orders.COMPLETED);

            orderCountList.add(orderCount);
            validOrderCountList.add(validOrderCount);
        }
        //时间区间内的订单总数和有效订单总数
        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();

        Double orderCompletionRate= totalOrderCount==0? 0.0:
                validOrderCount.doubleValue()/totalOrderCount.doubleValue();

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .orderCountList(StringUtils.join(orderCountList,","))
                .validOrderCountList(StringUtils.join(validOrderCountList,","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    private Integer getOrderCount(LocalDateTime beginTime, LocalDateTime endTime, Integer status){
        Map map=new HashMap();
        map.put("beginTime",beginTime);
        map.put("endTime",endTime);

        map.put("status",status);
        return orderMapper.countByMap(map);
    }

    /**
     * 统计指定时间区域内的销量前十
     * @param beginDate
     * @param endDate
     * @return
     */
    public SalesTop10ReportVO getSalesTop10(LocalDate beginDate, LocalDate endDate) {
        LocalDateTime beginTime = LocalDateTime.of(beginDate, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(endDate, LocalTime.MAX);
        List<GoodsSalesDTO> salesTop10 = orderMapper.getSalesTop10(beginTime, endTime);

        List<String> listName = salesTop10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        String nameList = StringUtils.join(listName, ",");
        List<Integer> listNumber = salesTop10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());
        String numberList = StringUtils.join(listNumber, ",");

        SalesTop10ReportVO salesTop10ReportVO = new SalesTop10ReportVO();
        salesTop10ReportVO.setNameList(nameList);
        salesTop10ReportVO.setNumberList(numberList);
        return salesTop10ReportVO;
    }
}
