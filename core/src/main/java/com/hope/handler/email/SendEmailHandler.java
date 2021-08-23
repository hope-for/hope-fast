package com.hope.handler.email;

import cn.hutool.core.date.DateUtil;
import com.hope.model.dto.TableRowData;
import com.hope.utils.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * 邮件发送实现类
 *
 * @author aodeng
 */
@Service
public class SendEmailHandler implements EmailHandler {

    private static final Logger log = LoggerFactory.getLogger(SendEmailHandler.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.fromMail.addr}")
    private String from;

    private String COMMA = ",";

    /***
     * 发送文本邮件
     * @param to
     * @param subject
     * @param content
     */
    @Override
    public void sendTextEmail(String to, String subject, String content) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(content);
        try {
            mailSender.send(mailMessage);
            log.info("[文本邮件发送成功，当前时间]-[{}]", new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * html邮件
     * @param to
     * @param subject
     * @param content
     */
    @Override
    public void sendHtmlEmali(String to, String subject, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            //true表示创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content);
            //发送邮件
            mailSender.send(message);
            log.info("[html邮件发送成功，当前时间]-[{}]", new Date());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /***
     * 附件邮件
     * @param to
     * @param subject
     * @param content
     * @param filePath
     */
    @Override
    public void sendAttachmentsEmail(String to, String subject, String content, String filePath) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            //true表示创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            //上传附件
            FileSystemResource resource = new FileSystemResource(new File(filePath));
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            helper.addAttachment(fileName, resource);
            //发送邮件
            mailSender.send(message);
            log.info("[附件邮件发送成功，当前时间]-[{}]", new Date());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /***
     * 嵌入静态资源邮件
     * @param to
     * @param subject
     * @param content
     * @param rscPath
     * @param rscId
     */
    @Override
    public void sendStaticResourcesEmail(String to, String subject, String content, String rscPath,
                                         String rscId) {
        MimeMessage message = mailSender.createMimeMessage();
        try {

            //true表示创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            //嵌入静态资源
            FileSystemResource resource = new FileSystemResource(new File(rscPath));
            helper.addInline(rscId, resource);

            //发送邮件
            mailSender.send(message);
            log.info("[嵌入静态资源邮件发送成功，当前时间]-[{}]", new Date());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
/*    @Transactional(rollbackFor = Exception.class)
    @Async*/
    public boolean sendAttachmentMail(String to, String cc, String subject, String content,
                                      String[] tableHeader, List<TableRowData> tableRowData) {
        MimeMessage message = mailSender.createMimeMessage();

        //暂时一个
        //String[] tos = to.split(COMMA);
        //String[] ccs = cc.split(COMMA);

        try {
            //true表示创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            //抄送
            //helper.setCc(ccs);
            helper.setSubject(subject);

            if (StringUtils.isEmpty(content)) {
                StringBuilder htmlBuilder = new StringBuilder();
                htmlBuilder.append("<html><head></head>");
                htmlBuilder.append("<body><p>各位好：</p></body>");
                htmlBuilder.append("<body><p>本邮件为系统自动发送。</p></body>");
                htmlBuilder.append("<body><p>祝好！</p></body>");
                htmlBuilder.append("</html>");
                content = htmlBuilder.toString();
            }
            helper.setText(content, true);

            //嵌入静态资源
            InputStream in = getWriteExcel("Sheet1", tableHeader, tableRowData);
            String fileName = DateUtil.today() + "工单统计表-" + System.currentTimeMillis() + ".xls";
            helper.addAttachment(fileName, new ByteArrayResource(IOUtils.toByteArray(in)),
                "application/vnd.ms-excel;charset=UTF-8");
            in.close();
            //发送邮件
            mailSender.send(message);
            log.info("[嵌入静态资源邮件发送成功，当前时间]-[{}]", new Date());
            return true;
        } catch (MessagingException e) {
            log.warn("发送附件到邮件失败io：{}", e);
        } catch (IOException e) {
            log.warn("发送附件到邮件失败io：{}", e);
        }

        return false;
    }

    /**
     * 导出excel为文件流，空了优化下，先这样
     *
     * @author aodeng
     */
    private InputStream getWriteExcel(String tableTitle, String[] tableHeader,
                                      List<TableRowData> rowData) {
        // 创建工作簿
        HSSFWorkbook wb;
        // 创建表对象
        HSSFSheet sheet;
        // 创建行
        HSSFRow row;
        // 输入输出流
        ByteArrayOutputStream out;
        InputStream in;

        try {
            // 不存在，则新建excel
            // 1.获取excel的book对象
            wb = new HSSFWorkbook();
            // 2.创建sheet
            sheet = wb.createSheet(tableTitle);
            // 居中样式
            HSSFCellStyle style = wb.createCellStyle();
            // 字体样式
            HSSFFont font = wb.createFont();
            font.setBold(true);
            font.setItalic(true);
            font.setFontHeight((short) 200);
            font.setColor(HSSFFont.COLOR_NORMAL);
            style.setFont(font);
            // 4.创建第一行，作为表头
            int rowIndex = 0;
            if (null != tableHeader) {
                HSSFRow row1 = sheet.createRow(0);
                for (int i = 0; i < tableHeader.length; ++i) {
                    HSSFCell row1cell = row1.createCell(i);
                    row1cell.setCellValue(tableHeader[i]);
                    row1cell.setCellStyle(style);
                }
                rowIndex++;
            }
            // 5创建数据行，插入数据
            if (!CollectionUtils.isEmpty(rowData)) {
                for (int i = 0; i < rowData.size(); i++) {

                    row = sheet.createRow(rowIndex++);

                    HSSFCell row1cell = row.createCell(0);
                    row1cell.setCellValue(rowData.get(i).getColumn1());
                    row1cell.setCellStyle(style);

                    HSSFCell row1cel2 = row.createCell(1);
                    row1cel2.setCellValue(rowData.get(i).getColumn2());
                    row1cel2.setCellStyle(style);

                    HSSFCell row1cel3 = row.createCell(2);
                    row1cel3.setCellValue(rowData.get(i).getColumn3());
                    row1cel3.setCellStyle(style);
                }
            }

            // 写文件
            out = new ByteArrayOutputStream();
            wb.write(out);
            byte[] bookByteAry = out.toByteArray();
            in = new ByteArrayInputStream(bookByteAry);
            return in;
        } catch (IOException e) {
            //out不关闭，这里不会影响内存
            //e.printStackTrace();
            return null;
        }
    }
}
