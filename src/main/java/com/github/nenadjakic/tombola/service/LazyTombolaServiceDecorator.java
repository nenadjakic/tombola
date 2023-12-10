package com.github.nenadjakic.tombola.service;

import com.github.nenadjakic.tombola.util.Observer;
import com.github.nenadjakic.tombola.util.ProgressBarUpdateObserver;
import com.github.nenadjakic.tombola.util.SpinnerComponentUpdateObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LazyTombolaServiceDecorator implements TombolaService {
    private final TombolaServiceImpl tombolaServiceImpl;
    private final ProgressBarUpdateObserver progressBarUpdateObserver;
    private final SpinnerComponentUpdateObserver spinnerComponentUpdateObserver;

    @Autowired
    public LazyTombolaServiceDecorator(TombolaServiceImpl tombolaServiceImpl, ProgressBarUpdateObserver progressBarUpdateObserver, SpinnerComponentUpdateObserver spinnerComponentUpdateObserver) {
        this.tombolaServiceImpl = tombolaServiceImpl;
        this.progressBarUpdateObserver = progressBarUpdateObserver;
        this.spinnerComponentUpdateObserver = spinnerComponentUpdateObserver;
    }

    @Override
    public boolean isNumberGenerated() {
        return tombolaServiceImpl.isNumberGenerated();
    }

    @Override
    public void generate(Integer min, Integer max, Integer pick) {

        for (int i = 1; i <= 9; i++) {
            notifyObserver(progressBarUpdateObserver, Map.of("progress", i * 10, "message", "Generating ..."));
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        tombolaServiceImpl.generate(min, max, pick);
        notifyObserver(progressBarUpdateObserver, Map.of("progress", 100, "message", "Tombola numbers are generated.", "message-type", "success"));
    }

    @Override
    public Integer pickNext() {
        notifyObserver(spinnerComponentUpdateObserver, Map.of("timeout-ms", 1000));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return tombolaServiceImpl.pickNext();
    }

    @Override
    public List<Integer> pickAll() {
        notifyObserver(spinnerComponentUpdateObserver, Map.of("timeout-ms", 1000));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return tombolaServiceImpl.pickAll();
    }

    public void notifyObserver(Observer observer, final Map<String, Object> properties) {
        observer.update(properties);
    }
}
