package com.github.nenadjakic.tombola.component;

import com.github.nenadjakic.tombola.util.PrettyPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProgressBarComponent {
    private static final String CUU = "\u001B[A";
    private static final String DL = "\u001B[1M";

    private final String doneMarker = "=";
    private final String remainsMarker = "-";
    private final String leftDelimiter = "<";
    private final String rightDelimiter = ">";

    private boolean started = false;

    private final PrettyPrinter prettyPrinter;

    @Autowired
    public ProgressBarComponent(PrettyPrinter prettyPrinter) {
        this.prettyPrinter = prettyPrinter;
    }

    public void display(int percentage) {
        display(percentage, null);
    }

    public void display(int percentage, String statusMessage) {
        if (!started) {
            started = true;
            prettyPrinter.getTerminal().writer().println();
        }
        final int x = (percentage / 5);
        final int y = 20 - x;
        final String message = ((statusMessage == null) ? "" : statusMessage);

        final String done = prettyPrinter.success(new String(new char[x]).replace("\0", doneMarker));
        final String remains = new String(new char[y]).replace("\0", remainsMarker);
        String progressBar = String.format("%s%s%s%s %d", leftDelimiter, done, remains, rightDelimiter, percentage);

        prettyPrinter.getTerminal().writer().println(CUU + "\r" + DL + progressBar + "% " + message);
        prettyPrinter.getTerminal().flush();
    }

    public void reset() {
        started = false;
    }
}
