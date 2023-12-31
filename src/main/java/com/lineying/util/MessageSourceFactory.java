package com.lineying.util;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;

/**
 * 处理本地化资源
 */
public class MessageSourceFactory {

    // 默认消息资源
    private static final String DEFAULT_BASENAME = "i18n/messages";

    /**
     * 创建消息源
     * @return
     */
    public static MessageSource buildDefaultMessageSource() {
        return buildMessageSource(DEFAULT_BASENAME);
    }

    /**
     * 创建消息源
     * @param basename
     * @return
     */
    public static MessageSource buildMessageSource(String basename) {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(basename);
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        messageSource.setFallbackToSystemLocale(true);
        messageSource.setCacheSeconds(-1);
        messageSource.setAlwaysUseMessageFormat(false);
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

}
