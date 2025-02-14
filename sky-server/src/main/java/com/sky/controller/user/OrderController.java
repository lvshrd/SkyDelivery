package com.sky.controller.user;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userOrderController")//admin也有此controller，为了在spring容器bean时进行区分
@Slf4j
@Api(tags = "用户订单相关接口")
@RequestMapping("/user/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户提交订单
     * @param ordersSubmitDTO
     * @return
     */
    @ApiOperation("用户提交订单")
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submitOrder(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("提交订单:{}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO=orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }
}
