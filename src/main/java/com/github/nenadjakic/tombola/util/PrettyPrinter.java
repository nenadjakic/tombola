package com.github.nenadjakic.tombola.util;

import lombok.Getter;
import lombok.Setter;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class PrettyPrinter {

    private final Terminal terminal;

    @Autowired
    public PrettyPrinter(@Lazy Terminal terminal) {
        this.terminal = terminal;
    }


    public String getColored(String message, int attributedStyle) {
        return (new AttributedStringBuilder())
                .append(message, AttributedStyle.DEFAULT.foreground(attributedStyle))
                .toAnsi();
    }
    public String info(String message) {
        return getColored(message, AttributedStyle.BLACK);
    }
    public String success(String message) {
        return getColored(message, AttributedStyle.GREEN);
    }
    public String warning(String message) {
        return getColored(message, AttributedStyle.YELLOW);
    }
    public String error(String message) {
        return getColored(message, AttributedStyle.RED);
    }

    public void print(String message, int attributedStyle) {
        String toPrint = getColored(message, attributedStyle);

        terminal.writer().println(toPrint);
        terminal.flush();
    }
}
