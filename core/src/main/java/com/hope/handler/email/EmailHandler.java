package com.hope.handler.email;

import com.hope.model.dto.TableRowData;

import java.util.List;

/**
 * @author aodeng
 */
public interface EmailHandler {
    void sendTextEmail(String to, String subject, String content);

    void sendHtmlEmali(String to, String subject, String content);

    void sendAttachmentsEmail(String to, String subject, String content, String filePath);

    void sendStaticResourcesEmail(String to, String subject, String content, String rscPath,
                                  String rscId);

    /**
     * 邮件发送excel附件
     *
     * @param to           发送人(多个逗号隔开,)
     * @param cc           抄送人(多个逗号隔开,)
     * @param subject      邮件标题
     * @param mailContent  邮件正文
     * @param tableHeader  附件表头
     * @param tableRowData 附件内容
     */
    boolean sendAttachmentMail(String to, String cc, String subject, String mailContent,
                               String[] tableHeader, List<TableRowData> tableRowData);
}
