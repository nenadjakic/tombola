package com.github.nenadjakic.tombola.util;

import com.github.nenadjakic.tombola.component.SpinnerComponent;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SpinnerComponentUpdateObserver implements Observer {
    private final PrettyPrinter prettyPrinter;
    private final SpinnerComponent spinnerComponent;

    public SpinnerComponentUpdateObserver(PrettyPrinter prettyPrinter, SpinnerComponent spinnerComponent) {
        this.prettyPrinter = prettyPrinter;
        this.spinnerComponent = spinnerComponent;
    }

    @Override
    public void update(Map<String, Object> properties) {
        if (properties != null) {
            int timeout = (int)properties.getOrDefault("timeout-ms", 2000);
            if (timeout < 2000) {
                timeout = 2000;
            }

            for (int i = 1; i <= timeout / 50; i++) {
                spinnerComponent.display();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            spinnerComponent.reset();
        }
    }
}
