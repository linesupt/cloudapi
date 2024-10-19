package com.lineying.common;

import org.jetbrains.annotations.Nullable;

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

}
