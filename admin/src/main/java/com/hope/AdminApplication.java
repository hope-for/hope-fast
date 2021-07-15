package com.hope;

import com.hope.config.CustomShutdownConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("com.hope.mapper")
@SpringBootApplication
@EnableScheduling
public class AdminApplication {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(AdminApplication.class, args);
        Object webServerFactory = run.getBean("webServerFactory");
        LOGGER.info(webServerFactory + "");
        LOGGER.info("[项目名称]admin服务启动成功！\n温馨提示：代码千万行，注释第一行，命名不规范，同事泪两行");
    }

    @Bean
    public CustomShutdownConfig customShutdown() {
        return new CustomShutdownConfig();
    }

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory(final CustomShutdownConfig customShutdown) {
        TomcatServletWebServerFactory tomcatServletWebServerFactory = new TomcatServletWebServerFactory();
        tomcatServletWebServerFactory.addConnectorCustomizers(customShutdown);
        return tomcatServletWebServerFactory;
    }
}
