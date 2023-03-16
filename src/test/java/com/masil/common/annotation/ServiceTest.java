package com.masil.common.annotation;

import com.masil.common.DatabaseCleaner;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.CompositeDatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.SQLException;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServiceTest {

    @Autowired
    protected DatabaseCleaner databaseCleaner;
    @Autowired
    private DataSource dataSource;

    @BeforeAll
    public void initData() throws SQLException {
        // 초기 sql 스크립트 실행
        CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("setup.sql")));
        populator.populate(dataSource.getConnection());
    }

    @BeforeEach
    void beforeEach() {
        databaseCleaner.execute();
    }
}