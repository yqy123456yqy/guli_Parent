package com.atguigu.guli.service.trade.controller.api;
import com.atguigu.guli.common.base.result.utils.JwtInfo;
import com.atguigu.guli.common.base.result.utils.JwtUtils;
import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.service.OrderService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/trade/order")
@Slf4j
public class ApiTradeController {

    @Autowired
    OrderService orderService;

    @PostMapping("auth/save/{courseId}")
    public R save(@PathVariable String courseId, HttpServletRequest request){

        System.out.println("courseId = " + courseId);

        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        System.out.println("jwtInfo = " + jwtInfo);

        String orderId = orderService.getOrder(courseId,jwtInfo.getId());

        return R.ok().data("orderId",orderId);
    }

    @GetMapping("/auth/get/{orderId}")
    public R getOrder(@PathVariable String orderId,HttpServletRequest request){

        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);

        Order order = orderService.getByOrderId(orderId,jwtInfo.getId());
        if(order != null){
            return R.ok().data("order",order);
        }else{
            return R.error().message("获取订单失败");
        }
    }

    @GetMapping("/auth/isBuy/{courseId}")
    public R getOrderIsBuy(@PathVariable String courseId,HttpServletRequest request){

        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);

       boolean result =  orderService.selectIsBuy(courseId,jwtInfo.getId());

       return R.ok().data("isBuy",result);
    }
    @ApiOperation(value = "获取当前用户订单列表")
    @GetMapping("auth/list")
    public R list(HttpServletRequest request){

        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        List<Order> orders = orderService.selectOrder(jwtInfo.getId());
      return R.ok().data("items",orders);
    }

    @DeleteMapping("auth/remove/{orderId}")
    public R removeById(@PathVariable String orderId,HttpServletRequest request){
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);

      boolean result = orderService.removeById(orderId,jwtInfo.getId());

      if(result){
          return R.ok().message("删除成功");
      }else{
          return R.error().message("删除失败");
      }

    }

}
