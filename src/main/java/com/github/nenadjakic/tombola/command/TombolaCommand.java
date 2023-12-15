package com.github.nenadjakic.tombola.command;

import com.github.nenadjakic.tombola.config.TombolaProperties;
import com.github.nenadjakic.tombola.model.entity.Log;
import com.github.nenadjakic.tombola.service.LogService;
import com.github.nenadjakic.tombola.service.LazyTombolaServiceDecorator;
import com.github.nenadjakic.tombola.util.PrettyPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@ShellCommandGroup("Tombola commands")
@ShellComponent
public class TombolaCommand {

    private final LazyTombolaServiceDecorator lazyTombolaServiceDecorator;
    private final TombolaProperties tombolaProperties;
    private final PrettyPrinter prettyPrinter;
    private final LogService logService;
    @Autowired
    public TombolaCommand(final LazyTombolaServiceDecorator lazyTombolaServiceDecorator,
                          final TombolaProperties tombolaProperties,
                          final PrettyPrinter prettyPrinter, LogService logService) {
        this.lazyTombolaServiceDecorator = lazyTombolaServiceDecorator;
        this.tombolaProperties = tombolaProperties;
        this.prettyPrinter = prettyPrinter;
        this.logService = logService;
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

    @ShellMethod(value = "Show previous tombola/lottery logs", key = "logs")
    public String history() {
        var result =  logService.getAll();

        if (result.isEmpty()) {
            return prettyPrinter.warning("No logs.");
        }
        Object[][] data = preparaHistoryData(result);
        TableModel tableModel = new ArrayTableModel(data);
        var tableBuilder = new TableBuilder(tableModel);
        tableBuilder.addFullBorder(BorderStyle.oldschool);
        return prettyPrinter.info(tableBuilder.build().render(80));
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

    private Object[][] preparaHistoryData(final List<Log> logList) {
        String[] header = { "Id", "Tombola/loterry datetime", "Combination", "Picked numbers" };
        Object[][] data = new Object[logList.size() + 1][];
        data[0] = header;
        for (int i = 1; i <= logList.size(); i++) {
            var history = logList.get(i-1);
            data[i] = getHistoryParts(history);
        }

        return data;
    }

    private Object[] getHistoryParts(Log log) {
        var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss");

        return new Object[] {
                log.getId(),
                log.getDateTime().format(dateTimeFormatter),
                log.getCombination(),
                log.getPickedNumbers().stream().map(Object::toString).collect(Collectors.joining(", "))
        };
    }
}
