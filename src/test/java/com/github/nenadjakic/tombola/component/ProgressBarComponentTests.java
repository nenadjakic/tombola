package com.github.nenadjakic.tombola.component;

import com.github.nenadjakic.tombola.util.PrettyPrinter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ProgressBarComponentTests {

    private ByteArrayOutputStream outputStream;
    private ProgressBarComponent progressBarComponent;

    @BeforeEach
    void setUp() throws IOException {
        final ByteArrayInputStream inputStream =  new ByteArrayInputStream(new byte[] {});
        outputStream = new ByteArrayOutputStream();
        Terminal terminal = TerminalBuilder.builder().streams(inputStream, outputStream).build();
        PrettyPrinter prettyPrinter = new PrettyPrinter(terminal);
        progressBarComponent = new ProgressBarComponent(prettyPrinter);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("display with percentage parameter")
    void displayPercentage() {
        progressBarComponent.display(50);
        var result = outputStream.toString();
        assertTrue(result.contains("=".repeat(10)));
        assertTrue(result.contains("-".repeat(10)));
        assertTrue(result.contains("50%"));
    }

    @Test
    @DisplayName("display with percentage and message parameters")
    void displayPercentageMessage() {
        progressBarComponent.display(100, "DONE");
        var result = outputStream.toString();
        assertTrue(result.contains("=".repeat(20)));
        assertFalse(result.contains("-"));
        assertTrue(result.contains("100%"));
        assertTrue(result.contains("DONE"));
    }

    @Test
    void reset() {
        assertFalse((boolean) ReflectionTestUtils.getField(progressBarComponent, "started"));
        progressBarComponent.display(100, "DONE");
        assertTrue((boolean) ReflectionTestUtils.getField(progressBarComponent, "started"));
        progressBarComponent.reset();
        assertFalse((boolean) ReflectionTestUtils.getField(progressBarComponent, "started"));
    }
}