package com.github.nenadjakic.tombola.component;

import com.github.nenadjakic.tombola.util.PrettyPrinter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SpinnerComponentTests {

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5 })
    @DisplayName("display without parameters")
    void display(int count) throws IOException {
        byte[] buffer = {};
        final ByteArrayInputStream inputStream =  new ByteArrayInputStream(buffer);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Terminal terminal = TerminalBuilder.builder().streams(inputStream, outputStream).build();
        PrettyPrinter prettyPrinter = new PrettyPrinter(terminal);

        SpinnerComponent spinnerComponent = new SpinnerComponent(prettyPrinter);

        char[] spinnerChars = (char[]) ReflectionTestUtils.getField(spinnerComponent, "spinner");

        for (int i = 0; i < count; i++) {
            spinnerComponent.display();
        }
        var charPosition = count != spinnerChars.length ? (count % spinnerChars.length) - 1 : 0;
        var result = outputStream.toString();
        assertTrue(result.indexOf(spinnerChars[charPosition]) != -1);
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5 })
    @DisplayName("display with message parameter")
    void displayWithMessage(int count) throws IOException {
        byte[] buffer = {};
        final ByteArrayInputStream inputStream =  new ByteArrayInputStream(buffer);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Terminal terminal = TerminalBuilder.builder().streams(inputStream, outputStream).build();
        PrettyPrinter prettyPrinter = new PrettyPrinter(terminal);

        SpinnerComponent spinnerComponent = new SpinnerComponent(prettyPrinter);

        char[] spinnerChars = (char[]) ReflectionTestUtils.getField(spinnerComponent, "spinner");

        for (int i = 0; i < count; i++) {
            spinnerComponent.display("MSG");
        }
        var charPosition = count != spinnerChars.length ? (count % spinnerChars.length) - 1 : 0;
        var result = outputStream.toString();
        assertTrue(result.indexOf(spinnerChars[charPosition]) != -1);
        assertTrue(result.contains("MSG"));
    }

    @Test
    void reset() {
    }
}