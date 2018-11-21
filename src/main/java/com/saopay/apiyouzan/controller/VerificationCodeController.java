package com.saopay.apiyouzan.controller;

import com.saopay.apiyouzan.service.AdminService;
import com.saopay.apiyouzan.service.OrderService;
import com.saopay.apiyouzan.util.http.JsonResult;
import com.saopay.apiyouzan.util.http.JsonResultErrorEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author huangding
 * @description
 * @date 2018/11/16 20:40
 */
@Api("有赞核销")
@Controller
@Slf4j
@RequestMapping("verification")
public class VerificationCodeController extends YouZanBaseController {


    @Autowired
    private AdminService adminService;

    @Autowired
    private OrderService orderService;


    @RequestMapping("code")
    public String getCode(String code, ModelMap modelMap) {
        if (StringUtils.isBlank(code)) {
            modelMap.addAttribute("errorMsg", "请从微信公众号进入");
            return "error";
        }
        JsonResult jsonResult = adminService.addYouZanVerificationAdmin(code);
        switch (JsonResultErrorEnum.N(jsonResult.getCode())) {
            case SUCCESS:
                StringBuilder url = new StringBuilder();
                url.append(request.getRequestURL()).append("?").append(request.getQueryString());
                modelMap.addAttribute("url", url.toString());
                modelMap.addAttribute("openId", jsonResult.getData());
                return "verification";
            case YZ_TAG_NULL:
            case YZ_USER_NULL:
                modelMap.addAttribute("errorMsg", "非核销员不可操作");
                return "error";
            default:
                modelMap.addAttribute("errorMsg", "异常请联系管理员");
                return "error";
        }
    }

    @PostMapping("virtual-code")
    @ApiOperation("核销")
    @ResponseBody
    public JsonResult virtualCode(
        @ApiParam(value = "核销码", required = true) @RequestParam(name = "code") String code,
        @ApiParam(value = "微信openId", required = true) @RequestParam(name = "openId") String openId) {
        JsonResult jsonResult = orderService.virtualCode(code, openId);
        return jsonResult;
    }

    @RequestMapping("success")
    public String success() {
        return "success";
    }

    @RequestMapping("error")
    public String error(String msg, ModelMap modelMap) {
        modelMap.addAttribute("errorMsg", msg);
        return "error";
    }
}
