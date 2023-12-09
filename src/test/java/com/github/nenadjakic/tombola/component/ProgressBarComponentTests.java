package com.github.nenadjakic.tombola.component;

import com.github.nenadjakic.tombola.util.PrettyPrinter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ProgressBarComponentTests {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("display with percentage parameter")
    void displayPercentage() throws IOException {
        byte[] buffer = {};
        final ByteArrayInputStream inputStream =  new ByteArrayInputStream(buffer);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Terminal terminal = TerminalBuilder.builder().streams(inputStream, outputStream).build();
        PrettyPrinter prettyPrinter = new PrettyPrinter(terminal);
        ProgressBarComponent progressBarComponent = new ProgressBarComponent(prettyPrinter);

        progressBarComponent.display(50);
        var result = outputStream.toString();
        assertTrue(result.contains("=".repeat(10)));
        assertTrue(result.contains("-".repeat(10)));
        assertTrue(result.contains("50%"));
    }

    @Test
    @DisplayName("display with percentage and message parameters")
    void displayPercentageMessage() throws IOException {
        byte[] buffer = {};
        final ByteArrayInputStream inputStream =  new ByteArrayInputStream(buffer);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Terminal terminal = TerminalBuilder.builder().streams(inputStream, outputStream).build();
        PrettyPrinter prettyPrinter = new PrettyPrinter(terminal);
        ProgressBarComponent progressBarComponent = new ProgressBarComponent(prettyPrinter);

        progressBarComponent.display(100, "DONE");
        var result = outputStream.toString();
        assertTrue(result.contains("=".repeat(20)));
        assertFalse(result.contains("-"));
        assertTrue(result.contains("100%"));
        assertTrue(result.contains("DONE"));
    }

    @Test
    void reset() {
    }
}