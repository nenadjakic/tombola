package com.github.nenadjakic.tombola.component;

import com.github.nenadjakic.tombola.util.PrettyPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpinnerComponent {
    private static final String CUU = "\u001B[A";
    private boolean started = false;
    private int spinCounter = 0;
    private final char[] spinner = {'|', '/', '-', '\\'};
    private final PrettyPrinter prettyPrinter;

    @Autowired
    public SpinnerComponent(PrettyPrinter prettyPrinter) {
        this.prettyPrinter = prettyPrinter;
    }

    public void display(String message) {
        if (!started) {
            prettyPrinter.getTerminal().writer().println();
            started = true;
        }
        String progress = String.format("%s", message);

        prettyPrinter.getTerminal().writer().println(CUU + "\r" + getSpinnerChar() + progress);
        prettyPrinter.getTerminal().flush();
    }

    public void display() {
        if (!started) {
            prettyPrinter.getTerminal().writer().println();
            started = true;
        }
        prettyPrinter.getTerminal().writer().println(CUU + "\r" + getSpinnerChar());
        prettyPrinter.getTerminal().flush();
    }

    public void reset() {
        spinCounter = 0;
        started = false;
    }

    private char getSpinnerChar() {
        char spinChar = spinner[spinCounter];
        spinCounter++;
        if (spinCounter == spinner.length) {
            spinCounter = 0;
        }
        return spinChar;
    }
}
