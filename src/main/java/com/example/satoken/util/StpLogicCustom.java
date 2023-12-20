package com.example.satoken.util;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaFoxUtil;

import java.util.Arrays;

/**
 * 自定义Sa-Token权限认证实现类（接口权限默认添加admin角色）
 *
 * @author yangyg
 * @date 2023/12/20 3:12 PM
 */
public class StpLogicCustom extends StpLogic {

    public StpLogicCustom() {
        super(StpUtil.TYPE);
    }

    /**
     * 解决所有角色检查都添加一个admin
     */
    @Override
    public void checkByAnnotation(SaCheckRole at) {
        // 角色默认添加一个admin角色
        String[] customRoleArray = Arrays.copyOf(at.value(), at.value().length + 1);
        customRoleArray[customRoleArray.length - 1] = "admin";
        // 角色校验强制使用or逻辑
        super.checkRoleOr(customRoleArray);
    }

    /**
     * 解决所有权限检查都添加一个admin
     */
    @Override
    public void checkByAnnotation(SaCheckPermission at) {
        String[] permissionArray = at.value();
        try {
            if(at.mode() == SaMode.AND) {
                super.checkPermissionAnd(permissionArray);
            } else {
                super.checkPermissionOr(permissionArray);
            }
        } catch (NotPermissionException e) {
            // 角色默认添加一个admin角色
            String[] customRoleArray = Arrays.copyOf(at.orRole(), at.orRole().length + 1);
            customRoleArray[customRoleArray.length - 1] = "admin";
            // 权限认证校验未通过，再开始角色认证校验
            for (String role : customRoleArray) {
                String[] rArr = SaFoxUtil.convertStringToArray(role);
                // 某一项 role 认证通过，则可以提前退出了，代表通过
                if (hasRoleAnd(rArr)) {
                    return;
                }
            }
            throw e;
        }
    }
}
