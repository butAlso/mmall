package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.service.IUserService;
import com.mmall.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/order")
public class OrderManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IOrderService iOrderService;

    /**
     * 获取订单列表
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> orderList(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        if (!iUserService.checkAdminRole(user).isSuccess()) return ServerResponse.createByErrorMessage("无权限操作");

        return iOrderService.manageList(pageNum, pageSize);
    }

    /**
     * 获取订单详情
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "/{orderNo}", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<OrderVo> orderDetail(HttpSession session, Long orderNo) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        if (!iUserService.checkAdminRole(user).isSuccess()) return ServerResponse.createByErrorMessage("无权限操作");

        return iOrderService.manageDetail(orderNo);
    }

    /**
     * 搜索订单
     * @param session
     * @param orderNo
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> orderSearch(HttpSession session, Long orderNo, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        if (!iUserService.checkAdminRole(user).isSuccess()) return ServerResponse.createByErrorMessage("无权限操作");

        return iOrderService.manageSearch(orderNo, pageNum, pageSize);
    }

    /**
     * 对某订单发货
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "/{orderNo}", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> orderSendGoods(HttpSession session, Long orderNo) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        if (!iUserService.checkAdminRole(user).isSuccess()) return ServerResponse.createByErrorMessage("无权限操作");

        return iOrderService.manageSendGoods(orderNo);
    }


}
