package com.printway;

import com.printway.config.DBConnection;
import com.printway.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
@AutoConfigurationPackage
@PropertySources({@PropertySource("classpath:application.properties")})
public class Application {
    @Autowired
    private Environment env;

    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        return dataSource;
    }

    public static void main(String[] args) {
        Date dateCurrent = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateCurrentStr = formatter.format(dateCurrent);
        try {
            for (String parameter : args) {
                if (parameter.equals("all")) {
                    System.out.println("Start Getting Labels...  [" + dateCurrentStr + "]");
                    ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
                    LabelService labelService = new LabelService();
                    labelService.getLabel();
                    System.out.println("** DONE DHL LABEL! **");
                    DBConnection.getInstance().closeConnection();
                    int exitCode = SpringApplication.exit(ctx, new ExitCodeGenerator() {
                        @Override
                        public int getExitCode() {
                            return 0;
                        }
                    });
                    System.exit(exitCode);
                } else {
                    System.out.println("Start Getting Label For ... [" + parameter + "] - [DHL LABEL]");
                    ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
                    LabelService labelService = new LabelService();
                    labelService.getLabelByOrderId(parameter);
                    System.out.println("** DONE DHL LABEL! **");
                    DBConnection.getInstance().closeConnection();
                    int exitCode = SpringApplication.exit(ctx, new ExitCodeGenerator() {
                        @Override
                        public int getExitCode() {
                            return 0;
                        }
                    });
                    System.exit(exitCode);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
            DBConnection.getInstance().closeConnection();
            int exitCode = SpringApplication.exit(ctx, new ExitCodeGenerator() {
                @Override
                public int getExitCode() {
                    return 0;
                }
            });
            System.exit(exitCode);
        }
    }
}
