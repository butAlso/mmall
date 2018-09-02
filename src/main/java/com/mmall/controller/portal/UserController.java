package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) session.setAttribute(Const.CURRENT_USER, response.getData());
        return response;
    }

    /**
     * 用户登出
     * @param session
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }

    /**
     * 验证邮箱或用户名是否可用
     * @param str
     * @param type
     * @return
     */
    @RequestMapping(value = "/validation", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }

//    @RequestMapping(value = "get_user_info", method = RequestMethod.GET)
//    @ResponseBody
//    public ServerResponse<User> getUserInfo(HttpSession session) {
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
//        if (user != null) return  ServerResponse.createBySuccess(user);
//        return ServerResponse.createByErrorMessage("用户未登录");
//    }

    /**
     * 获取找回密码的问题
     * @param username
     * @return
     */
    @RequestMapping(value = "/password/question", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.selectQuestion(username);
    }

    /**
     * 找回密码问题的答案验证
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @RequestMapping(value = "/password/answer", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }

    /**
     * 根据旧密码更新密码
     * @param session
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    @RequestMapping(value = "/password", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session, String passwordOld, String passwordNew) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByErrorMessage("用户未登录");
        return iUserService.resetPassword(passwordOld, passwordNew, user);
    }

    /**
     * 修改个人信息
     * @param session
     * @param user
     * @return
     */
    @RequestMapping(value = "/information", method = RequestMethod.PUT)
    @ResponseBody
    public ServerResponse<User> update_information(HttpSession session, User user) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) return ServerResponse.createByErrorMessage("用户未登录");

        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = iUserService.updateInformation(user);

        // 更新成功，重设session
        if (response.isSuccess()) session.setAttribute(Const.CURRENT_USER, response.getData());
        return response;
    }

    /**
     * 获取个人信息
     * @param session
     * @return
     */
    @RequestMapping(value = "/information", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> get_information(HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        return iUserService.getInformation(currentUser.getId());
    }
}
