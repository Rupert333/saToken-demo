package com.example.satoken.rest;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/current")
public class CurrentController {

    @RequestMapping("/user")
    public void  user() {
        log.info("当前用户：" + StpUtil.getLoginId());

        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        log.info("当前token：" + JSONUtil.toJsonStr(JSONUtil.parse(tokenInfo)));

        String name = (String) StpUtil.getExtra("name");
        log.info("当前name：" + name);
    }

    @RequestMapping("/token")
    public void  user(String token) {
        log.info("根据输入的token获取信息");
        Object currentLoginId = StpUtil.getLoginId();
        log.info("当前用户：" + currentLoginId);
        String name = (String) StpUtil.getExtra("name");
        log.info("当前name：" + name);

        String loginId = (String) StpUtil.getLoginIdByToken(token);
        log.info("从token获取到的loginId：" + loginId);
        // 临时身份切换
        System.out.println("------- [身份临时切换]调用开始...");
        StpUtil.switchTo(loginId, () -> {
            System.out.println("是否正在身份临时切换中: " + StpUtil.isSwitch());  // 输出 true
            System.out.println("获取当前登录账号id: " + StpUtil.getLoginId());   // 输出 10044
            // 扩展参数必须指定token才可以，否则获取到的仍是当前登陆token的扩展信息
            String anotherName = (String) StpUtil.getExtra(token, "name");
            log.info("anotherName：" + anotherName);
        });
        System.out.println("------- [身份临时切换]调用结束...");
    }
}
