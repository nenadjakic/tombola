package com.github.nenadjakic.tombola.service;

import com.github.nenadjakic.tombola.model.entity.Log;
import com.github.nenadjakic.tombola.repository.LogRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {
    private final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public List<Log> getAll() {
        return logRepository.findAll(Sort.by(Sort.Direction.DESC, "dateTime"));
    }

    public Log saveAndFlush(final Log log) {
        return logRepository.saveAndFlush(log);
    }
}
