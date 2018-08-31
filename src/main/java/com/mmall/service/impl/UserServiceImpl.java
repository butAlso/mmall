package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    public ServerResponse<User> login(String username, String password) {
        // 用户名验证
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) return ServerResponse.createByErrorMessage("用户名不存在");

        // 密码验证
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) return ServerResponse.createByErrorMessage("密码错误");
        // 密码字段设为空，返回登录成功和用户信息
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    public ServerResponse<String> register(User user) {
        // 校验用户名
        ServerResponse validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) return validResponse;
        // 校验邮箱
        validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) return validResponse;

        user.setRole(Const.Role.ROLE_CUSTOMER);
        // md5加密密码
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        // 插入用户信息并判断是否成功，返回结果
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) return ServerResponse.createByErrorMessage("注册失败");
        return ServerResponse.createBySuccessMessage("注册成功");

    }

    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            // 校验用户名是否已存在
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) return ServerResponse.createByErrorMessage("用户名已存在");
            }
            //校验邮箱是否已被使用
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) return ServerResponse.createByErrorMessage("email 已被使用");
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage(type + " " + str + "校验成功，可以使用");
    }

    public ServerResponse<String> selectQuestion(String username) {
        // 校验用户是否存在
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) return ServerResponse.createByErrorMessage("用户不存在");

        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) return ServerResponse.createBySuccess(question);
        // 找回密码问题为空
        return ServerResponse.createByErrorMessage("找回密码的问题是空的");
    }

    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);

        if (resultCount > 0) {
            // 问题，答案，用户匹配
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        // 问题，答案，用户不匹配
        return ServerResponse.createByErrorMessage("问题答案错误");
    }

    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) return ServerResponse.createByErrorMessage("参数错误，token不存在");
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        // 用户校验
        if (validResponse.isSuccess()) return ServerResponse.createByErrorMessage("用户不存在");
        // token校验
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(token)) return ServerResponse.createByErrorMessage("token无效或过期");
        if (!StringUtils.equals(forgetToken, token)) return ServerResponse.createByErrorMessage("token错误，请重试");

        // token校验成功，更新密码
        String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
        int rowCount = userMapper.updatePasswordByUsername(username, passwordNew);
        if (rowCount > 0) return ServerResponse.createBySuccessMessage("修改密码成功");
        else return ServerResponse.createByErrorMessage("修改密码失败");
    }

    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
        //  防止横向越权，验证用户旧密码正确
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (resultCount == 0) return ServerResponse.createByErrorMessage("旧密码错误");
        // 重设密码
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (resultCount > 0) return ServerResponse.createBySuccessMessage("密码更新成功");
        else return ServerResponse.createByErrorMessage("密码更新失败");
    }

    public ServerResponse<User> updateInformation(User user) {
        // username不能被更新，email不能更新为其他用户正在使用的email
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (resultCount > 0) return ServerResponse.createByErrorMessage("email已存在");

        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0) return ServerResponse.createBySuccess("更新个人信息成功", updateUser);
        else  return ServerResponse.createByErrorMessage("更新个人信息失败");
    }

    public ServerResponse<User> getInformation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) return ServerResponse.createByErrorMessage("用户不存在");
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    public ServerResponse checkAdminRole(User user) {
        if (user != null && user.getRole() == Const.Role.ROLE_ADMIN) return ServerResponse.createBySuccess();
        else return ServerResponse.createByError();
    }
}
