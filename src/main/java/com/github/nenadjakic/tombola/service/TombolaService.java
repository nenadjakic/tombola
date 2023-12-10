package com.github.nenadjakic.tombola.service;

import java.util.List;

public interface TombolaService {

    boolean isNumberGenerated();
    void generate(Integer min, Integer max, Integer pick);
    Integer pickNext();
    List<Integer> pickAll();
}
