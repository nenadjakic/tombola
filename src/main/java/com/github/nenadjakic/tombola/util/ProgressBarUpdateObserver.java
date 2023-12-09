package com.github.nenadjakic.tombola.util;

import com.github.nenadjakic.tombola.component.ProgressBarComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProgressBarUpdateObserver implements Observer {
    private final PrettyPrinter prettyPrinter;
    private final ProgressBarComponent progressBarComponent;

    @Autowired
    public ProgressBarUpdateObserver(PrettyPrinter prettyPrinter, ProgressBarComponent progressBarComponent) {
        this.prettyPrinter = prettyPrinter;
        this.progressBarComponent = progressBarComponent;
    }

    @Override
    public void update(final Map<String, Object> properties) {
        if (properties != null) {
            final int progress = (int)properties.getOrDefault("progress", 0);
            final String rawMessage = (String)properties.getOrDefault("message", null);
            final String messageType = (String)properties.getOrDefault("message-type", "");
            final String message = switch (messageType) {
                case "success" -> prettyPrinter.success(rawMessage);
                case "error" -> prettyPrinter.error(rawMessage);
                case "warning" -> prettyPrinter.warning(rawMessage);
                default -> prettyPrinter.info(rawMessage);
            };


            progressBarComponent.display(progress, message);
        }
    }
}
