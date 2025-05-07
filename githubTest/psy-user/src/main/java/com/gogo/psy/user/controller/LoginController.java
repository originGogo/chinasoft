package com.gogo.psy.user.controller;


import com.gogo.psy.common.Result.Result;
import com.gogo.psy.common.login.GogoLogin;
import com.gogo.psy.common.util.BaseController;
import com.gogo.psy.user.pojo.dto.*;
import com.gogo.psy.user.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@Slf4j
public class LoginController extends BaseController {

    @Resource
    private UserInfoService userInfoService;

    @Operation(summary = "输入账号密码登录 只有在该微信账户未绑定用户的时候才需要输入账号密码")
    @PostMapping("/secondLogin")
    public Result<String> secondLogin(@RequestBody LoginInfoDTO loginInfoDTO){
        return Result.ok(userInfoService.secondLogin(loginInfoDTO));
    }

    @Operation(summary = "获取用户登录信息")
    @GetMapping("/getUserLoginInfo")
    public Result<UserInfoDTO> getUserLoginInfo(HttpServletRequest request){
        UserInfoDTO res = userInfoService.getUserInfo(request);
        return Result.ok(res);
    }

    @Operation(summary = "微信授权登录")
    @PostMapping("/firstLogin")
    public Result<String> firstLogin(@RequestBody FirstLoginDTO firstLoginDTO){
        String token = userInfoService.firstLogin(firstLoginDTO.getCode(), firstLoginDTO.getAvatarUrl());
        return Result.ok(token);
    }

    @Operation(summary = "修改密码")
    @GogoLogin
    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO){
        userInfoService.updatePassword(updatePasswordDTO);
        return Result.ok();
    }

    @Operation(summary = "退出登录")
    @GogoLogin
    @PostMapping("logout")
    public Result logout(@RequestBody LogOutDTO logOutDTO){
        userInfoService.logout(logOutDTO);
        return Result.ok();
    }

    @PostMapping("/addImg")
    @Operation(summary = "添加图片")
    public Result<String> addImg(@RequestParam("file") MultipartFile file, @RequestParam("path") String path) {
        String s = userInfoService.uploadImg(file, path);
        return Result.ok(s);
    }

    @GetMapping("/balances")
    @GogoLogin
    @Operation(summary = "获取余额")
    public Result<Integer> getBalance(@RequestParam("userId") Long userId){
        Integer balance = userInfoService.getBalance(userId);
        return Result.ok(balance);
    }

    @PostMapping("/transfer")
    @Operation(summary = "转账")
    public Result transfer(@RequestBody TransferDTO transferDTO){
        userInfoService.transfer(transferDTO);
        return Result.ok("转账成功");
    }
}
