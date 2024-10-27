package com.lineying.manager;

import com.lineying.entity.AppEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * appcode应用管理
 */
public class AppcodeManager {

    /**
     * 获取指定APP信息
     * @param appcode
     * @return
     */
    public static @Nullable AppEntity getEntity(String appcode) {
        switch (appcode) {
            case "mathcalc":
                return AppEntity.MATHCALC;
            case "scancode":
                return AppEntity.SCANCODE;
            case "linevideo":
                return AppEntity.LINEVIDEO;
            case "scanmessenger":
                return AppEntity.SCANMESSENGER;
        }
        return null;
    }

    /**
     * 是否包含应用
     * @param appcode
     * @return
     */
    public static boolean contains(String appcode) {
        return getEntity(appcode) != null;
    }

    /**
     * 通过产品ID获取应用码
     * @param productId
     * @return
     */
    public static String getAppcode(long productId) {
        AppEntity[] entities = AppEntity
                .values();
        for (AppEntity entity : entities) {
            if (entity.getAppleId() == productId) {
                return entity.getAppcode();
            }
        }
        return "";
    }

    /**
     * 获取数据表后缀
     * @param appcode
     * @return
     */
    public static String getTableSuffix(String appcode) {

        AppEntity entity = getEntity(appcode);
        if (entity == null) {
            return "";
        }
        return entity.getTableSuffix();
    }

    /**
     * 获取应用共享密钥
     * @param appcode
     * @return
     */
    public static String getSecret(String appcode) {
        AppEntity entity = getEntity(appcode);
        if (entity == null) {
            return "";
        }
        return entity.getSecret();
    }

}
