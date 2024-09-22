package com.lineying.common;

/**
 * 密钥管理
 */
public class SecretManager {

    /**
     * 密钥
     * @param appcode
     * @return
     */
    public static String getSecret(String appcode) {
        switch (appcode) {
            case "mathcalc":
                return "2280d5323503451791a7faabebfffab6";
            case "scancode":
                return "2b7a18ace6cd4042b6aad66c9f8f7050";
            case "linevideo":
                return "cb403f049c0f49d1941ad42a589cbc63";
            case "smsmessenger":
                return "";
        }
        return "";
    }

}
