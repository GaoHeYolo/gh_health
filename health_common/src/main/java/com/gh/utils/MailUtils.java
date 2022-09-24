package com.gh.utils;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * 邮箱发送工具类
 */
public class MailUtils {
    public static final String VALIDATE_CODE = "SMS_159620392";//发送短信验证码
    public static final String ORDER_NOTICE = "SMS_159771588";//体检预约成功通知

    /**
     * 发送验证码
     *
     * @param receiveMail
     * @throws Exception
     */
    public static void sendMail(String templateCode,String receiveMail, String param) throws Exception {
        Properties prop = new Properties();
        // 开启debug调试，以便在控制台查看
        prop.setProperty("mail.debug", "true");
        // 设置邮件服务器主机名
        prop.setProperty("mail.host", "smtp.163.com");
        // 发送服务器需要身份验证
        prop.setProperty("mail.smtp.auth", "true");
        // 发送邮件协议名称
        prop.setProperty("mail.transport.protocol", "smtp");
        // 开启SSL加密，否则会失败

        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.socketFactory", sf);
        // 创建session
        Session session = Session.getInstance(prop);
        // 通过session得到transport对象
        Transport ts = session.getTransport();
        // 连接邮件服务器：邮箱类型，帐号，POP3/SMTP协议授权码 163使用：smtp.163.com，qq使用：smtp.qq.com
        ts.connect("smtp.163.com", "ghmail4444@163.com", "ZYZPZJRPUTLNTTKU");
        // 创建邮件
        Message message = createSimpleMail(session,templateCode,receiveMail,param);
        // 发送邮件
        ts.sendMessage(message, message.getAllRecipients());
        ts.close();
    }

    /**
     * @Method: createSimpleMail
     * @Description: 创建一封只包含文本的邮件
     */
    public static MimeMessage createSimpleMail(Session session,String templateCode, String receiveMail, String param) throws Exception {
        // 创建邮件对象
        MimeMessage message = new MimeMessage(session);
        // 指明邮件的发件人
        message.setFrom(new InternetAddress("ghmail4444@163.com"));
        // 指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiveMail));
        if (templateCode.equals(VALIDATE_CODE)){
            // 邮件的标题
            message.setSubject("验证码");
            // 邮件的文本内容
            message.setContent("您的验证码：" + param + "，如非本人操作，请忽略！请勿回复此邮箱", "text/html;charset=UTF-8");
        }else if (templateCode.equals(ORDER_NOTICE)){
            // 邮件的标题
            message.setSubject("体检预约");
            // 邮件的文本内容
            message.setContent("恭喜您成功预约" + param + "日的体检，如非本人操作，请忽略！请勿回复此邮箱", "text/html;charset=UTF-8");
        }
        // 返回创建好的邮件对象
        return message;
    }
}
