package com.github.nenadjakic.tombola.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TombolaServiceTests {

    private static TombolaService tombolaService;

    @BeforeAll
    static void initAll() {
        tombolaService = new TombolaService();
    }

    @BeforeEach
    void init() {
    }

    @Test
    void generate() {
        tombolaService.generate(1, 50, 10);

        assertEquals(10, tombolaService.getGeneratedNumbers().size());

        tombolaService.generate(1, 5, 5);
        assertTrue(tombolaService.getGeneratedNumbers().containsAll(Arrays.asList(1, 2, 3, 4, 5)));
    }

    @ParameterizedTest
    @ValueSource(ints = { 5, 10 })
    void pickNext(int pick) {
        tombolaService.generate(1, 50, pick);
        for (int i = 0; i < pick; i++) {
            assertNotNull(tombolaService.pickNext());
        }
        assertNull(tombolaService.pickNext());
    }

    @ParameterizedTest
    @MethodSource("providePickAllParameters")
    void pickAll(int min, int max, int pick) {
        tombolaService.generate(min, max, pick);
        final var generatedNumbers = tombolaService.getGeneratedNumbers();
        final var pickedAllNumbers = tombolaService.pickAll();
        assertTrue(generatedNumbers.containsAll(pickedAllNumbers));
        assertTrue(tombolaService.pickAll().isEmpty());
    }

    private static Stream<Arguments> providePickAllParameters() {
        return Stream.of(
                Arguments.of(1, 5, 5),
                Arguments.of(1, 10, 10)
        );
    }
}