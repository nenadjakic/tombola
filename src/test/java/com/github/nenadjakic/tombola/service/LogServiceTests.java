package com.github.nenadjakic.tombola.service;

import com.github.nenadjakic.tombola.model.entity.Log;
import com.github.nenadjakic.tombola.repository.LogRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class LogServiceTests {
    @Autowired
    private LogRepository logRepository;

    private LogService logService;

    private static Log log;

    @BeforeAll
    static void initAll() {
        log = new Log();
        log.setPickedNumbers(Arrays.asList(0, 1, 2, 3));
        log.setDateTime(LocalDateTime.MIN);
        log.setCombination("COMBINATION");

    }

    @BeforeEach
    void setUp() {
        logService = new LogService(logRepository);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void saveAndFlush() {
        var savedLog = logService.saveAndFlush(log);
        assertEquals("COMBINATION", savedLog.getCombination());
        assertEquals(LocalDateTime.MIN, savedLog.getDateTime());
        assertEquals(Arrays.asList(0, 1, 2, 3), savedLog.getPickedNumbers());
    }

    @Test
    void getAll() {
        assertEquals(0, logService.getAll().size());

        logService.saveAndFlush(log);

        var result = logService.getAll();

        assertEquals(1, result.size());
    }
}
