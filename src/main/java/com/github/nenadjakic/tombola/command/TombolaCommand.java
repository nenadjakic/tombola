package com.github.nenadjakic.tombola.command;

import com.github.nenadjakic.tombola.config.TombolaProperties;
import com.github.nenadjakic.tombola.service.LazyTombolaServiceDecorator;
import com.github.nenadjakic.tombola.util.PrettyPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;
import java.util.stream.Collectors;

@ShellCommandGroup("Tombola commands")
@ShellComponent
public class TombolaCommand {

    private final LazyTombolaServiceDecorator lazyTombolaServiceDecorator;
    private final TombolaProperties tombolaProperties;
    private final PrettyPrinter prettyPrinter;

    @Autowired
    public TombolaCommand(final LazyTombolaServiceDecorator lazyTombolaServiceDecorator,
                          final TombolaProperties tombolaProperties,
                          final PrettyPrinter prettyPrinter) {
        this.lazyTombolaServiceDecorator = lazyTombolaServiceDecorator;
        this.tombolaProperties = tombolaProperties;
        this.prettyPrinter = prettyPrinter;
    }

    @ShellMethod(value = "Generate tombola/lottery numbers.")
    public void generate(@ShellOption(value = {"-F", "--from"}, defaultValue = "-1") Integer from,
                           @ShellOption(value = {"-T", "--to"}, defaultValue = "-1") Integer to,
                           @ShellOption(value = {"-P", "--pick"}, defaultValue = "-1") Integer pick) {
        if (-1 == from) {
            from = tombolaProperties.getFrom();
        }

        if (-1 == to) {
            to = tombolaProperties.getTo();
        }

        if (-1 == pick) {
            pick = tombolaProperties.getPick();
        }

        lazyTombolaServiceDecorator.generate(from, to, pick);
    }

    @ShellMethod(value = "Pick next number.", key = { "next", "pick-next" })
    public String next() {
        Integer next = lazyTombolaServiceDecorator.pickNext();
        if (next == null) {
            return prettyPrinter.warning("No more numbers!");
        } else {
            return prettyPrinter.success(String.format("Next number is %s", next));
        }
    }

    @ShellMethod(value = "Pick all remaining numbers.", key = { "all", "pick-all" })
    public String all() {
        List<Integer> result = lazyTombolaServiceDecorator.pickAll();
        if (result == null || result.isEmpty()) {
            return prettyPrinter.warning("No more numbers!");
        } else {
            return prettyPrinter.success(String.format("Remaining numbers are %s.", result.stream().map(Object::toString).collect(Collectors.joining(", "))));
        }
    }

    public Availability generateAvailability() {
        return !lazyTombolaServiceDecorator.isNumberGenerated() ? Availability.available() : Availability.unavailable("Tomobola is not generated yet or all numbers are picked.");
    }

    public Availability nextAvailability() {
        return pickCommandsAvailability();
    }

    public Availability allAvailability() {
        return pickCommandsAvailability();
    }

    private Availability pickCommandsAvailability() {
        return lazyTombolaServiceDecorator.isNumberGenerated() ? Availability.available() : Availability.unavailable("Tomobola is not generated yet or all numbers are picked.");
    }

}
