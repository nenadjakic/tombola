package com.github.nenadjakic.tombola.service;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TombolaService {
    private static Queue<Integer> generatedNumbers = new LinkedList<>();

    public Queue<Integer> getGeneratedNumbers() {
        return new LinkedList<>(generatedNumbers);
    }

    public void generate(Integer min, Integer max, Integer pick) {
        generatedNumbers = IntStream
                .rangeClosed(min, max)
                .boxed()
                .collect(Collectors.collectingAndThen(Collectors.toCollection(LinkedList::new),
                        x -> {
                            Collections.shuffle(x);
                            return x.stream().limit(pick).collect(Collectors.toCollection(LinkedList::new));
                        }));
    }

    public Integer pickNext() {
        return generatedNumbers.poll();
    }

    public List<Integer> pickAll() {
        final List<Integer> result = generatedNumbers.stream().toList();
        generatedNumbers.clear();
        return result;
    }
}
