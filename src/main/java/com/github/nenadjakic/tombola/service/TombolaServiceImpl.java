package com.github.nenadjakic.tombola.service;

import com.github.nenadjakic.tombola.model.entity.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TombolaServiceImpl implements TombolaService {
    private static Queue<Integer> generatedNumbers = new LinkedList<>();
    private static String combination;
    private static final List<Integer> pickedNumbers = new LinkedList<>();
    private final LogService logService;

    @Autowired
    public TombolaServiceImpl(final LogService logService) {
        this.logService = logService;
    }

    public Queue<Integer> getGeneratedNumbers() {
        return new LinkedList<>(generatedNumbers);
    }

    @Override
    public boolean isNumberGenerated() {
        return generatedNumbers != null && !generatedNumbers.isEmpty();
    }

    @Override
    public void generate(Integer min, Integer max, Integer pick) {
        pickedNumbers.clear();
        generatedNumbers = IntStream
                .rangeClosed(min, max)
                .boxed()
                .collect(Collectors.collectingAndThen(Collectors.toCollection(LinkedList::new),
                        x -> {
                            Collections.shuffle(x);
                            return x.stream().limit(pick).collect(Collectors.toCollection(LinkedList::new));
                        }));
        combination = MessageFormat.format("{0}/({1}-{2})", pick, min, max);
    }

    @Override
    public Integer pickNext() {
        var pickedNumber = generatedNumbers.poll();
        if (pickedNumber != null) {
            pickedNumbers.add(pickedNumber);
        }
        if (generatedNumbers.isEmpty()) {
            saveLog();
        }
        return pickedNumber;
    }

    @Override
    public List<Integer> pickAll() {
        final List<Integer> result = generatedNumbers.stream().toList();
        generatedNumbers.clear();
        if (!result.isEmpty()) {
            pickedNumbers.addAll(result);
        }

        saveLog();

        return result;
    }

    private void saveLog() {
        Log log = new Log();
        log.setDateTime(LocalDateTime.now());
        log.setPickedNumbers(pickedNumbers);
        log.setCombination(combination);
        logService.saveAndFlush(log);
    }
}
