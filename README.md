包含功能、版本信息：

- springboot框架版本：2.1.8.RELEASE
- 没有权限依赖，没有过度封装，内置hplus前端ui框架，却丝毫不影响前后端分离
- 主流rest/restful风格接口，全局异常，验证码，excel导出，FTP文件上传,文件上传本地存储等基础功能
- 数据库变更版本控制工具flyway
- 工具类库hutool 版本：5.6.5
- mybatis-plus 版本：3.4.3
- 第三方授权登录的工具类库JustAuth
- 阿里云短信接口
- 微信相关：根据code获取微信小程序用户openid，session_key 解密小程序用户手机号
- 优雅关闭 Spring Boot 应用
- 又拍云附件上传，附件删除 SDK版本：4.2.3
- SpringBoot默认的连接池 HikariCP（史上最快？）
- Token校验拦截器
- 切面自定义注解 @Log 操作日志收集记录，日常开发非常方便  AOP版本：2.5.3
- 数据库主键设置GUID Mybatis调用存储过程 
    
    <details>   
        <summary>
            <b>存储过程 数据库配置：</b>
        </summary>

        1、mysql数据库创建表（该表为配置id生成规则）：

            CREATE TABLE `pb_code_ident` (
              `PCI_Table` varchar(64) NOT NULL,
              `PCI_Type` varchar(64) DEFAULT NULL,
              `PCI_Length` int DEFAULT NULL,
              `PCI_Head` varchar(8) DEFAULT NULL,
              `PCI_Fill` varchar(64) DEFAULT NULL,
              `PCI_Date` datetime DEFAULT NULL,
              `PCI_Default` decimal(18,0) DEFAULT NULL,
              `PCI_Identity` decimal(16,0) DEFAULT NULL,
              PRIMARY KEY (`PCI_Table`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

        2、创建存储过程

                DROP PROCEDURE IF EXISTS `GetID2`;
                DELIMITER ;;
                CREATE PROCEDURE `GetID2`(IN TableName VARCHAR(100),OUT TableID VARCHAR(36))
                BEGIN
                DECLARE s_Ident VARCHAR(20);
                DECLARE s_Fill VARCHAR(1);
                DECLARE s_Type VARCHAR(3);
                DECLARE s_Date VARCHAR(16);
                DECLARE s_Head VARCHAR(10);
                DECLARE s_ID VARCHAR(20);
                DECLARE d_Date datetime;

                select PCI_Date into d_Date from PB_Code_Ident Where PCI_Table = TableName;
                if(REPLACE(DATE_FORMAT(d_Date,'%Y/%m/%d'),'-','/')=REPLACE(curdate(),'-','/')) THEN
                SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
                update PB_Code_Ident set PCI_Identity = PCI_Identity + 1 Where PCI_Table = TableName;
                else
                SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
                update PB_Code_Ident set PCI_Identity = PCI_Default,PCI_Date=REPLACE(curdate(),'-','/') Where PCI_Table = TableName;
                end if;
                select PCI_Identity,PCI_Head into s_ID,s_Head from PB_Code_Ident Where PCI_Table = TableName;
                set @TableID = concat(s_Head,REPLACE(curdate(),'-',''),s_ID);
                select @TableID INTO TableID;
                END
                ;;
                DELIMITER ;

        3、MyBatis调用

            <!-- 此处的大括号与call之间不能换行（但是可以有空格），后面的大括号可以换行，否则会抛异常 -->
            <select id="getID" statementType="CALLABLE" parameterType="com.hope.model.bean.GetID" useCache="false">
                <![CDATA[
                call GetID2(#{name,mode=IN},#{id,jdbcType=VARCHAR,mode=OUT});
                ]]>
            </select>

    </details>
    
<h1 align="center"><a href="https://github.com/java-aodeng" target="_blank">hope-fast</a></h1>

> 简介：快速开发

<p align="center">
<a href="https://github.com/java-aodeng"><img alt="Author" src="https://img.shields.io/badge/author-%E4%BD%8E%E8%B0%83%E5%B0%8F%E7%86%8A%E7%8C%AB-blue.svg"/></a>
<a href="https://jq.qq.com/?_wv=1027&k=574chhz"><img alt="QQ群" src="https://img.shields.io/badge/chat-%E4%BD%8E%E8%B0%83%E5%B0%8F%E7%86%8A%E7%8C%ABQQ%E7%BE%A4-yellow.svg"/></a>
<a href="https://t.me/joinchat/LSsyBxVKLGEkF5MtIhg6TQ"><img alt="Telegram" src="https://img.shields.io/badge/telegram-%E4%BD%8E%E8%B0%83%E5%B0%8F%E7%86%8A%E7%8C%AB--%E5%AE%98%E6%96%B9%E9%83%A8%E8%90%BD-orange.svg"/></a>
</p>

------------------------------

### 开源协议

hope-fast 使用  MIT 协议开源
