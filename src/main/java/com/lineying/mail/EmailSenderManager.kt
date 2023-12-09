package com.lineying.mail

import com.lineying.bean.EmailMessage
import com.lineying.common.CommonConstant
import java.io.UnsupportedEncodingException
import java.util.*
import java.util.logging.Logger
import javax.mail.Authenticator
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

/**
 * 邮件发送管理
 */
object EmailSenderManager {
    const val TAG = "EmailSenderManager"
    const val CODE_SUCCESS = 0x1
    const val CODE_FAILED = 0x0
    // ssl连接需要设置端口
    private const val PORT_SSL = "465"
    // 非ssl可以不设置
    private const val PORT_NO_SSL = "25"

    /**
     * 邮箱域名
     */
    private fun makeSmtpHost(domain: String) : String {
        return "smtp." + domain
    }

    // 是否开启ssl连接
    private fun sslEnabled() : Boolean {
        return false;
    }

    /**
     * 发送内容至目标邮箱
     */
    fun relayEmail(subject: String, content: String, targetMail: String) : Int {

        val senderAccount = CommonConstant.MAIL_SENDER
        val senderName = CommonConstant.MAIL_SENDER_NAME
        val emailToAccountList = arrayListOf(targetMail)
        val subject = subject
        var flag = CODE_SUCCESS
        for (receiveAccount in emailToAccountList) {
            try {
                val srr = receiveAccount.split("@")
                val host = makeSmtpHost(srr[1])
                Logger.getGlobal().info("mail host::" + host)
                val emailMessage = createEmailMessage(content, senderAccount, senderName, receiveAccount, subject)
                val result = doSendEmailMessage(emailMessage, host)
                if (result != CODE_SUCCESS) {
                    flag = CODE_FAILED
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        return flag
    }

    /**
     * 发送邮件
     */
    private fun doSendEmailMessage(emailMessage: EmailMessage, smtpHost: String) : Int {
        val props = Properties()
        props["mail.smtp.host"] = smtpHost

        // 开启SSL
        if (sslEnabled()) {
            setSslMode(props, PORT_SSL)
        } else {
            // 可以不设置
            // setSslMode(props, PORT_NO_SSL)
        }
        setSenderToPro(props, CommonConstant.MAIL_SENDER, CommonConstant.MAIL_PASSWORD)
        props["mail.smtp.auth"] = true //如果不设置，则报553错误
        props["mail.transport.protocol"] = "smtp"
        //getDefaultInstance得到的始终是该方法初次创建的缺省的对象，getInstance每次获取新对象
        val session = Session.getInstance(props, SmtpAuthenticator(CommonConstant.MAIL_SENDER, CommonConstant.MAIL_PASSWORD))
        session.debug = true
        try {
            val message = createMimeMessage(session, emailMessage)
            doSendMessage(message)
        } catch (e: Exception) {
            e.printStackTrace()
            return CODE_FAILED
        }
        return CODE_SUCCESS
    }

    /**
     * 执行邮件发送
     * @param message
     */
    private fun doSendMessage(message: MimeMessage) {
        object : Thread() {
            override fun run() {
                try {
                    Transport.send(message)
                } catch (e: MessagingException) {
                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

    /**
     * 创建消息
     * @param session
     * @param emailMessage
     * @return
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
    @Throws(UnsupportedEncodingException::class, MessagingException::class)
    private fun createMimeMessage(session: Session, emailMessage: EmailMessage): MimeMessage {
        val message = MimeMessage(session)
        message.setFrom(InternetAddress(emailMessage.senderAccount, emailMessage.senderName, "UTF-8")) //发件人
        message.setRecipients(MimeMessage.RecipientType.TO, emailMessage.receiverAccount) //收件人
        message.subject = emailMessage.subject //主题
        message.setContent(emailMessage.content, "text/html;charset=UTF-8")
        return message
    }

    /**
     * 封装消息实体
     *
     * @param content
     * @return
     */
    private fun createEmailMessage(content: String, senderAccount: String, emailSenderName: String,
                                   receiverAccount: String, subject: String): EmailMessage {
        val message = EmailMessage()
        message.content = content
        message.senderAccount = senderAccount
        message.senderName = emailSenderName
        message.receiverAccount = receiverAccount
        message.subject = subject
        return message
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
    private fun setSslMode(props: Properties, smtpPort: String) {
        props.setProperty("mail.smtp.port", smtpPort)
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
        props.setProperty("mail.smtp.socketFactory.fallback", "false")
        props.setProperty("mail.smtp.socketFactory.port", smtpPort)
    }


    /**
     * 将发送发的账号和密码设置给配置文件
     *
     * @param properties
     * @param user
     * @return
     */
    private fun setSenderToPro(properties: Properties, mailSender: String, mailPassword: String) {
        properties["mail.smtp.username"] = mailSender
        properties["mail.smtp.password"] = mailPassword
    }

    /**
     * 登录认证
     */
    private class SmtpAuthenticator(mailSender: String, mailPassword: String) : Authenticator() {
        var mUsername: String
        var mPassword: String

        init {
            mUsername = mailSender
            mPassword = mailPassword
        }

        public override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(mUsername, mPassword)
        }
    }

}