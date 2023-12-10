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

class TombolaServiceImplTests {

    private static TombolaServiceImpl tombolaServiceImpl;

    @BeforeAll
    static void initAll() {
        tombolaServiceImpl = new TombolaServiceImpl();
    }

    @BeforeEach
    void init() {
    }

    @Test
    void generate() {
        tombolaServiceImpl.generate(1, 50, 10);

        assertEquals(10, tombolaServiceImpl.getGeneratedNumbers().size());

        tombolaServiceImpl.generate(1, 5, 5);
        assertTrue(tombolaServiceImpl.getGeneratedNumbers().containsAll(Arrays.asList(1, 2, 3, 4, 5)));
    }

    @ParameterizedTest
    @ValueSource(ints = { 5, 10 })
    void pickNext(int pick) {
        tombolaServiceImpl.generate(1, 50, pick);
        for (int i = 0; i < pick; i++) {
            assertNotNull(tombolaServiceImpl.pickNext());
        }
        assertNull(tombolaServiceImpl.pickNext());
    }

    @ParameterizedTest
    @MethodSource("providePickAllParameters")
    void pickAll(int min, int max, int pick) {
        tombolaServiceImpl.generate(min, max, pick);
        final var generatedNumbers = tombolaServiceImpl.getGeneratedNumbers();
        final var pickedAllNumbers = tombolaServiceImpl.pickAll();
        assertTrue(generatedNumbers.containsAll(pickedAllNumbers));
        assertTrue(tombolaServiceImpl.pickAll().isEmpty());
    }

    private static Stream<Arguments> providePickAllParameters() {
        return Stream.of(
                Arguments.of(1, 5, 5),
                Arguments.of(1, 10, 10)
        );
    }
}