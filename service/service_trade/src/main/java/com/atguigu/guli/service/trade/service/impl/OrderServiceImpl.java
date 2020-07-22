package com.atguigu.guli.service.trade.service.impl;

import com.atguigu.guli.common.base.result.result.ResultCodeEnum;
import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.entity.PayLog;
import com.atguigu.guli.service.trade.feign.CourseDtoService;
import com.atguigu.guli.service.trade.feign.MemberDtoService;
import com.atguigu.guli.service.trade.mapper.OrderMapper;
import com.atguigu.guli.service.trade.mapper.PayLogMapper;
import com.atguigu.guli.service.trade.service.OrderService;
import com.atguigu.guli.service.trade.utils.OrderNoUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2020-05-09
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    CourseDtoService courseDtoService;

    @Autowired
    MemberDtoService memberDtoService;

    @Autowired
    PayLogMapper payLogMapper;




    @Override
    public String getOrder(String courseId, String memberId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        queryWrapper.eq("member_id", memberId);

        Order orderExist = baseMapper.selectOne(queryWrapper);
        if (orderExist != null) {
            return orderExist.getId();
        }
        String orderNo = OrderNoUtils.getOrderNo();

        CourseDto courseDto = courseDtoService.getCourseDtoById(courseId);
        if (courseDto == null) {
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }
        MemberDto memberDto = memberDtoService.getMemberDto(memberId);
        if (memberDto == null) {
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }

        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setCourseCover(courseDto.getCover());
        order.setCourseId(courseDto.getId());
        order.setCourseTitle(courseDto.getTitle());
        order.setMobile(memberDto.getMobile());
        order.setMemberId(memberDto.getId());
        order.setNickname(memberDto.getNickname());
        order.setTeacherName(courseDto.getTeacherName());
        order.setPayType(1);
        order.setStatus(0);
        order.setTotalFee(courseDto.getPrice().multiply(new BigDecimal(100)));
        baseMapper.insert(order);
        return order.getId();
    }

    @Override
    public Order getByOrderId(String orderId, String memberId) {

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", orderId);
        queryWrapper.eq("member_id", memberId);

        Order order = baseMapper.selectOne(queryWrapper);
        return order;
    }

    @Override
    public boolean selectIsBuy(String courseId, String memberId) {

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        queryWrapper.eq("member_id", memberId);
        queryWrapper.eq("status", 1);

        Integer integer = baseMapper.selectCount(queryWrapper);


        return integer.intValue() > 0;
    }

    @Override
    public List<Order> selectOrder(String memberId) {

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");
        queryWrapper.eq("member_id", memberId);
        List<Order> orders = baseMapper.selectList(queryWrapper);
        return orders;
    }

    @Override
    public boolean removeById(String orderId, String memberId) {

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", orderId);
        queryWrapper.eq("member_id", memberId);
        return this.remove(queryWrapper);

    }

    @Override
    public Order getOrderByOrderNo(String orderNo) {

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        return baseMapper.selectOne(queryWrapper);

    }

    @Override
    public void updateOrderStutas(Map<String, String> map) {

        String orderNo = map.get("out_trade_no");
        Order order = this.getOrderByOrderNo(orderNo);
        order.setStatus(1);
        baseMapper.updateById(order);

        //记录支付日志
        PayLog payLog = new PayLog();
        payLog.setOrderNo(order.getOrderNo());
        payLog.setDeleted(order.getDeleted());
        payLog.setPayTime(new Date());
        payLog.setPayType(1);
        payLog.setTotalFee(order.getTotalFee().longValue());
        payLog.setTradeState(map.get("result_code"));
        payLog.setTransactionId(map.get("transaction_id"));
        payLog.setAttr(new Gson().toJson(map));

        payLogMapper.insert(payLog);

        courseDtoService.updateBuy(order.getCourseId());



    }

    @Override
    public boolean queryPayStatus(String orderNo) {

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        Order order = baseMapper.selectOne(queryWrapper);
        return order.getStatus() == 1;
    }


}

