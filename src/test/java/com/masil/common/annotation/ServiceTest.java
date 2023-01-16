package com.masil.common.annotation;

import com.masil.common.DatabaseCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class ServiceTest {

    @Autowired
    public DatabaseCleaner databaseCleaner;

    @BeforeEach
    void beforeEach() {
        databaseCleaner.execute();
    }
}
