package com.github.nenadjakic.tombola.component;

import com.github.nenadjakic.tombola.util.PrettyPrinter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    private ByteArrayOutputStream outputStream;
    SpinnerComponent spinnerComponent;
    char[] spinnerChars;

    @BeforeEach
    void setUp() throws IOException {
        final ByteArrayInputStream inputStream =  new ByteArrayInputStream(new byte[]{});
        outputStream = new ByteArrayOutputStream();
        Terminal terminal = TerminalBuilder.builder().streams(inputStream, outputStream).build();
        PrettyPrinter prettyPrinter = new PrettyPrinter(terminal);
        spinnerComponent = new SpinnerComponent(prettyPrinter);
        spinnerChars = (char[]) ReflectionTestUtils.getField(spinnerComponent, "spinner");
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5 })
    @DisplayName("display without parameters")
    void display(int count) {
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
    void displayWithMessage(int count) {
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
        assertSpinnerStoped();
        spinnerComponent.display();
        assertEquals(1, (int) ReflectionTestUtils.getField(spinnerComponent, "spinCounter"));
        assertTrue((boolean) ReflectionTestUtils.getField(spinnerComponent, "started"));
        spinnerComponent.reset();
        assertSpinnerStoped();
    }

    private void assertSpinnerStoped() {
        assertEquals(0, (int) ReflectionTestUtils.getField(spinnerComponent, "spinCounter"));
        assertFalse((boolean) ReflectionTestUtils.getField(spinnerComponent, "started"));
    }
}