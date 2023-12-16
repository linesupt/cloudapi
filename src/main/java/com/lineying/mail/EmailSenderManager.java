package com.lineying.mail;

import com.lineying.bean.EmailMessage;
import com.lineying.common.CommonConstant;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 邮件发送管理
 */
public class EmailSenderManager {
    private static final String TAG = "EmailSenderManager";
    public static final int CODE_SUCCESS = 0x1;
    public static final int CODE_FAILED = 0x0;
    public static final String SEND_HOST = "smtp.qq.com";
    // ssl连接需要设置端口
    private static final String PORT_SSL = "465";
    // 非ssl可以不设置
    private static final String PORT_NO_SSL = "25";

    public static void main(String[] args){
        System.out.println("");
    }

    /**
     * 邮箱域名
     */
    private static String makeSmtpHost(String domain) {
        return "smtp." + domain;
    }

    // 是否开启ssl连接
    private static boolean sslEnabled() {
        return false;
    }

    /**
     * 发送内容至目标邮箱
     */
    public static int relayEmail(String subject, String content, String targetMail) {

        String senderAccount = CommonConstant.MAIL_SENDER;
        String senderName = CommonConstant.MAIL_SENDER_NAME;
        String senderHost = SEND_HOST;
        List<String> emailToAccountList = Arrays.asList(targetMail);
        int flag = CODE_SUCCESS;
        for (String receiveAccount : emailToAccountList) {
            try {
                Logger.getGlobal().info("mail host::" + senderHost);
                EmailMessage emailMessage = createEmailMessage(content, senderAccount, senderName, receiveAccount, subject);
                int result = doSendEmailMessage(emailMessage, senderHost);
                if (result != CODE_SUCCESS) {
                    flag = CODE_FAILED;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * 获取邮箱地址
     * @param email
     * @return
     */
    public static String getEmailHost(String email) {
        String[] srr = email.split("@");
        return srr[1];
    }

    /**
     * 发送邮件
     */
    private static int doSendEmailMessage(EmailMessage emailMessage, String smtpHost) {
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", smtpHost);

        // 开启SSL
        if (sslEnabled()) {
            setSslMode(props, PORT_SSL);
        } else {
            // 可以不设置
            // setSslMode(props, PORT_NO_SSL);
        }
        setSenderToPro(props, CommonConstant.MAIL_SENDER, CommonConstant.MAIL_PASSWORD);
        props.setProperty("mail.smtp.auth", true + ""); //如果不设置，则报553错误
        props.setProperty("mail.transport.protocol", "smtp");
        //getDefaultInstance得到的始终是该方法初次创建的缺省的对象，getInstance每次获取新对象
        Session session = Session.getInstance(props, new SmtpAuthenticator(CommonConstant.MAIL_SENDER, CommonConstant.MAIL_PASSWORD));
        session.setDebug(true);
        try {
            MimeMessage message = createMimeMessage(session, emailMessage);
            doSendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
            return CODE_FAILED;
        }
        return CODE_SUCCESS;
    }

    /**
     * 执行邮件发送
     * @param message
     */
    private static void doSendMessage(MimeMessage message) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Transport.send(message);
                } catch (MessagingException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 创建消息
     * @param session
     * @param emailMessage
     * @return
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
    private static MimeMessage createMimeMessage(Session session, EmailMessage emailMessage)
        throws UnsupportedEncodingException, MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(emailMessage.getSenderAccount(), emailMessage.getSenderName(), "UTF-8")); //发件人
        message.setRecipients(MimeMessage.RecipientType.TO, emailMessage.getReceiverAccount()); //收件人
        message.setSubject(emailMessage.getSubject()); //主题
        message.setContent(emailMessage.getContent(), "text/html;charset=UTF-8");
        return message;
    }

    /**
     * 封装消息实体
     *
     * @param content
     * @return
     */
    private static EmailMessage createEmailMessage(String content, String senderAccount, String emailSenderName,
        String receiverAccount, String subject) {
        EmailMessage message = new EmailMessage();
        message.setContent(content);
        message.setSenderAccount(senderAccount);
        message.setSenderName(emailSenderName);
        message.setReceiverAccount(receiverAccount);
        message.setSubject(subject);
        return message;
    }

    /**
     * SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果
     * 开启了 SSL 连接,需要改为对应邮箱的 SMTP 服务器的端口, 具体可查看对应
     * 邮箱服务的帮助,QQ邮箱的SMTP(SLL)端口为465或587, 其他邮箱自行去查看)
     *
     * @param props
     * @param smtpPort
     * @return
     */
    private static void setSslMode(Properties props, String smtpPort) {
        props.setProperty("mail.smtp.port", smtpPort);
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", smtpPort);
    }


    /**
     * 将发送发的账号和密码设置给配置文件
     *
     * @param props
     * @param mailSender
     * @param mailPassword
     * @return
     */
    private static void setSenderToPro(Properties props, String mailSender, String mailPassword) {
        props.setProperty("mail.smtp.username", mailSender);
        props.setProperty("mail.smtp.password", mailPassword);
    }

    /**
     * 登录认证
     */
    static class SmtpAuthenticator extends Authenticator {

        String mUsername;
        String mPassword;

        SmtpAuthenticator(String mailSender,String mailPassword) {
            mUsername = mailSender;
            mPassword = mailPassword;
        }

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(mUsername, mPassword);
        }
    }

}