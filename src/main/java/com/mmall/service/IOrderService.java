package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.vo.OrderVo;

import java.util.Map;

public interface IOrderService {

    /**
     * 生成预支付二维码
     * @param orderNo
     * @param userId
     * @param path
     * @return
     */
    ServerResponse pay(Long orderNo, Integer userId, String path);

    /**
     * 根据支付宝回调修改支付状态
     * @param params
     * @return
     */
    ServerResponse aliCallback(Map<String, String> params);

    /***
     * 查询订单支付状态
     * @param userId
     * @param orderNo
     * @return
     */
    ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);

    /**
     * 创建订单
     * @param userId
     * @param shippingId
     * @return
     */
    ServerResponse createOrder(Integer userId, Integer shippingId);

    /**
     * 取消订单
     * @param userId
     * @param orderNo
     * @return
     */
    ServerResponse<String> cancel(Integer userId, Long orderNo);

    /**
     * 获取购物车已勾选商品vo
     * @param userId
     * @return
     */
    ServerResponse getOrderCartProduct(Integer userId);

    /**
     * 获取订单详情
     * @param userId
     * @param orderNo
     * @return
     */
    ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo);

    /**
     * 获取某以用户某页订单
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);


    // backend

    /**
     * 管理员获取某页订单
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> manageList(int pageNum, int pageSize);

    /**
     * 管理员获取订单详情
     * @param orderNo
     * @return
     */
    ServerResponse<OrderVo> manageDetail(Long orderNo);

    /**
     * 管理员搜索订单（初期先只根据订单号查询）
     * @param orderNo
     * @param pageSize
     * @param pageNum
     * @return
     */
    ServerResponse<PageInfo> manageSearch(Long orderNo, int pageSize, int pageNum);

    /**
     * 管理员发货
     * @param orderNo
     * @return
     */
    ServerResponse<String> manageSendGoods(Long orderNo);

}
