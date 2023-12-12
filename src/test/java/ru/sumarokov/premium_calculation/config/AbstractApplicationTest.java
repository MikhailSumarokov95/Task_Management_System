package ru.sumarokov.premium_calculation.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testng.annotations.*;

public abstract class AbstractApplicationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @SpringBootApplication(scanBasePackages = "ru.sumarokov.task_management_system")
    static class TestApp {
    }

    private static volatile ConfigurableApplicationContext CONTEXT;

    @AfterMethod
    protected void afterMethod() {
    }

    @BeforeMethod
    protected void setUp() {
        jdbcTemplate.update("truncate task cascade;");
        jdbcTemplate.update("truncate users cascade;");
        jdbcTemplate.update("truncate comment cascade;");
    }

    @AfterSuite
    final protected void afterSuite() {
        CONTEXT.close();
        CONTEXT = null;
    }

    @BeforeClass
    protected void beforeClass() {
        CONTEXT.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @BeforeSuite
    final protected void beforeSuite() {
        CONTEXT = new SpringApplicationBuilder(TestApp.class)
                .profiles("test", "test-local")
                .web(WebApplicationType.SERVLET)
                .run();
    }
}
