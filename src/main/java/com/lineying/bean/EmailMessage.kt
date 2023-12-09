package com.lineying.bean

/**
 * 邮件消息
 */
class EmailMessage {
    var senderAccount: String? = null
    var receiverAccount: String? = null
    var senderName: String? = null
    var subject: String? = null
    var content: String? = null

    constructor()
    constructor(senderAccount: String?, receiverAccount: String?, senderName: String?, subject: String?, content: String?) {
        this.senderAccount = senderAccount
        this.receiverAccount = receiverAccount
        this.senderName = senderName
        this.subject = subject
        this.content = content
    }

}