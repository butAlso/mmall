package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ICartService iCartService;

    /**
     * 获取用户购物车商品列表
     * @param session
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<CartVo> list(HttpSession session) {
        // 登录状态验证
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());

        return iCartService.list(user.getId());
    }

    /**
     * 添加商品到购物车
     * @param session
     * @param count
     * @param productId
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> add(HttpSession session, Integer count, Integer productId) {
        // 登录状态验证
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());

        return iCartService.add(user.getId(), productId, count);
    }

    /**
     * 修改购物车某商品数量
     * @param session
     * @param count
     * @param productId
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    public ServerResponse<CartVo> update(HttpSession session, Integer count, Integer productId) {
        // 登录状态验证
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());

        return iCartService.update(user.getId(), productId, count);
    }

    /**
     * 删除购物车某些商品
     * @param session
     * @param productIds
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ResponseBody
    public ServerResponse<CartVo> deleteProduct(HttpSession session, String productIds) {
        // 登录状态验证
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());

        return iCartService.deleteProduct(user.getId(), productIds);
    }

    /**
     * 选中购物车中所有商品
     * @param session
     * @return
     */
    @RequestMapping(value = "/check_all", method = RequestMethod.PUT)
    @ResponseBody
    public ServerResponse<CartVo> selectAll(HttpSession session) {
        // 登录状态验证
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());

        return iCartService.selectOrUnSelect(user.getId(), null, Const.Cart.CHECKED);
    }

    /**
     * 取消选中购物车中所有商品
     * @param session
     * @return
     */
    @RequestMapping(value = "/uncheck_all", method = RequestMethod.PUT)
    @ResponseBody
    public ServerResponse<CartVo> unSelectAll(HttpSession session) {
        // 登录状态验证
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());

        return iCartService.selectOrUnSelect(user.getId(), null, Const.Cart.UN_CHECKED);
    }

    /**
     * 选择购物车中某商品
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "/check", method = RequestMethod.PUT)
    @ResponseBody
    public ServerResponse<CartVo> select(HttpSession session, Integer productId) {
        // 登录状态验证
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());

        return iCartService.selectOrUnSelect(user.getId(), productId, Const.Cart.CHECKED);
    }

    /**
     * 取消选择购物车中某商品
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "/uncheck", method = RequestMethod.PUT)
    @ResponseBody
    public ServerResponse<CartVo> unSelect(HttpSession session, Integer productId) {
        // 登录状态验证
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());

        return iCartService.selectOrUnSelect(user.getId(), productId, Const.Cart.UN_CHECKED);
    }

    /**
     * 获取购物车中商品种类数量
     * @param session
     * @return
     */
    @RequestMapping(value = "/count", method = RequestMethod.PUT)
    @ResponseBody
    public ServerResponse<Integer> getCartProductCount(HttpSession session) {
        // 登录状态验证
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());

        return iCartService.getCartProductCount(user.getId());
    }
}
