package com.github.nenadjakic.tombola.service;

import com.github.nenadjakic.tombola.util.Mode;
import com.github.nenadjakic.tombola.util.Observer;
import com.github.nenadjakic.tombola.util.ProgressBarUpdateObserver;
import com.github.nenadjakic.tombola.util.SpinnerComponentUpdateObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LazyTombolaServiceDecorator implements TombolaService {
    private final TombolaServiceImpl tombolaServiceImpl;
    private final ProgressBarUpdateObserver progressBarUpdateObserver;
    private final SpinnerComponentUpdateObserver spinnerComponentUpdateObserver;
    private final int timeoutDivider;
    private final Mode mode;

    @Autowired
    public LazyTombolaServiceDecorator(TombolaServiceImpl tombolaServiceImpl, ProgressBarUpdateObserver progressBarUpdateObserver,
                                       SpinnerComponentUpdateObserver spinnerComponentUpdateObserver,
                                       @Value("${tombola.mode}") final Mode mode) {
        this.tombolaServiceImpl = tombolaServiceImpl;
        this.progressBarUpdateObserver = progressBarUpdateObserver;
        this.spinnerComponentUpdateObserver = spinnerComponentUpdateObserver;
        this.mode = mode;
        this.timeoutDivider = Mode.FAST.equals(mode) ? 2 : 1;
    }

    @Override
    public boolean isNumberGenerated() {
        return tombolaServiceImpl.isNumberGenerated();
    }

    @Override
    public void generate(Integer min, Integer max, Integer pick) {
        if (!Mode.SIMPLE.equals(mode)){
            for (int i = 1; i <= 9; i++) {
                notifyObserver(progressBarUpdateObserver, Map.of("progress", i * 10, "message", "Generating ..."));
                try {
                    Thread.sleep(250 / timeoutDivider);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        tombolaServiceImpl.generate(min, max, pick);
        notifyObserver(progressBarUpdateObserver, Map.of("progress", 100, "message", "Tombola numbers are generated.", "message-type", "success"));
    }

    @Override
    public Integer pickNext() {
        timeout();
        return tombolaServiceImpl.pickNext();
    }

    @Override
    public List<Integer> pickAll() {
        timeout();
        return tombolaServiceImpl.pickAll();
    }

    public void notifyObserver(Observer observer, final Map<String, Object> properties) {
        observer.update(properties);
    }

    private void timeout() {
        if (!Mode.SIMPLE.equals(mode)) {
            notifyObserver(spinnerComponentUpdateObserver, Map.of("timeout-ms", 1000 / timeoutDivider));
            try {
                Thread.sleep(500 / timeoutDivider);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
