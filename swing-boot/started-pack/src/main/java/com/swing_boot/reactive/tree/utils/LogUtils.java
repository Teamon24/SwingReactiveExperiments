package com.swing_boot.reactive.tree.utils;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.fusesource.jansi.Ansi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public final class LogUtils {

    public static final LogUtils LOGGER = new LogUtils();
    private static final StringBuilder history = new StringBuilder();
    private static final StringBuilder builder = new StringBuilder();

    private LogUtils() {}

    private static int nodeIndent   = 65;
    private static int methodIndent = 45;
    private static int threadIndent = 35;
    private static int numberIndent = 7;

    private static String object = "%-" + nodeIndent + "s";
    private static String method = "#%-" + methodIndent + "s";
    private static String threadTemplate = " %-" + threadIndent + "s";
    private static String numberTemplate = " #%-" + numberIndent + "d";

    private static String dateTemplate = "dd.MM.YYYY HH:mm:ss:SS";
    private static String timeTemplate = "mm:ss:SS ";

    private static SimpleDateFormat dateFormat = new SimpleDateFormat(dateTemplate, Locale.getDefault());
    private static SimpleDateFormat timeFormat = new SimpleDateFormat(timeTemplate, Locale.getDefault());

    private static Collection<String> filters = new ArrayList<>();
    private static Collection<String> exclusions = new ArrayList<>();

    private static Integer lineLength = nodeIndent + methodIndent + threadIndent + 100;

    public static Ansi.Color dateColor = Ansi.Color.BLUE;

    public static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void setFilters(Collection<String> filters) {
        LogUtils.filters.addAll(filters);
    }

    public static void setExclusions(Collection<String> exclusions) {
        LogUtils.exclusions.addAll(exclusions);
    }

    public synchronized LogUtils soutLine() {
        String line = getLine(lineLength);
        builder.append(line).append("\n");
        return LOGGER;
    }

    private static String getLine(@NonNull final Integer lineLength) {
        String line = "";
        for (int i = 0; i < lineLength; i++) {
            line += "-";
        }
        return line;
    }

    public synchronized LogUtils text(@NonNull final String template,
                                      @NonNull final Object...params)
    {
        final String toPrint = String.format(template, params);
        builder.append(toPrint);
        return LOGGER;
    }

    public synchronized LogUtils soutln(@NonNull final String method, @NonNull final String template, @NonNull final Object...params) {
        date(dateColor).thread().counter().method(method).text(template, params).ln();
        return LOGGER;
    }

    public synchronized LogUtils sout(@NonNull final String method, @NonNull final String template, @NonNull final Object...params) {
        date(dateColor).thread().counter().method(method).text(template, params);
        return LOGGER;
    }

    public synchronized LogUtils soutln(@NonNull final Object object, @NonNull final String method, @NonNull final Object...methodArgs) {
        StringBuilder argsTemplate= new StringBuilder();
        for (Object o : methodArgs) {
            argsTemplate.append("%s").append(", ");
        }
        date(dateColor).thread().counter().object(object).method(method).text(argsTemplate.toString(), methodArgs).ln();
        return LOGGER;
    }

    public synchronized LogUtils soutln(@NonNull final Object object, @NonNull final String method, @NonNull final Collection collection) {
        String items = StringUtils.join(collection, ", ");
        date(dateColor).thread().counter().object(object).method(method).text("{%s}", items).ln();
        return LOGGER;
    }








    public synchronized LogUtils date(@NonNull final Ansi.Color color) {
        final String dateString = colorText(color, dateStr(new Date()));
        builder.append(dateString);
        return LOGGER;
    }

    public synchronized LogUtils date() {
        final String dateString = colorText(dateColor, dateStr(new Date()));
        builder.append(dateString);
        return LOGGER;
    }

    public synchronized LogUtils date(@NonNull final Long date) {
        builder.append(dateStr(new Date(date)));
        return LOGGER;
    }

    public synchronized LogUtils thread() {
        final String name = Thread.currentThread().getName();
        final String toPrint = String.format(threadTemplate, colorText(Ansi.Color.CYAN, name));
        builder.append(toPrint);
        return LOGGER;
    }

    public synchronized LogUtils counter() {
        final int i = atomicInteger.incrementAndGet();
        builder.append(String.format(numberTemplate, i));
        return LOGGER;
    }

    private synchronized LogUtils object(@NonNull final Object reactiveTreeNode) {
        final String toPrint = String.format(object, reactiveTreeNode);
        builder.append(toPrint);
        return LOGGER;
    }

    private synchronized LogUtils method(@NonNull final String m) {
        final String toPrint = String.format(method, m);
        builder.append(toPrint);
        return LOGGER;
    }

    public synchronized LogUtils time(@NonNull final Long time) {
        final String dateString = colorText(Ansi.Color.MAGENTA, timeStr(time));
        builder.append(dateString);
        return LOGGER;
    }

    public synchronized LogUtils cln() {
        builder.append(":");
        return LOGGER;
    }

    public synchronized LogUtils ln() {
        builder.append("\n");
        return LOGGER;
    }



    public static String colorText(Ansi.Color color, String text) {
        return Ansi.ansi().fg(color).a(text).reset().toString();
    }

    private static String dateStr(Date date) {
        return "[" + dateFormat.format(date) + "]";
    }

    private String timeStr(@NonNull Long time) {
        return "[" + timeFormat.format(new Date(time)) + "]";
    }

    public synchronized void flush() {
        final String unfiltered = builder.toString();
        System.out.println(unfiltered);

        final StringBuilder notExculded = new StringBuilder();
        final String[] splits = unfiltered.split("\n");

            for (String split : splits) {
                boolean excluded = false;
                for (String exclusion : exclusions) {
                    if (split.contains(exclusion)) {
                        excluded = true;
                        break;
                    }
                }

                if (!excluded) {
                    notExculded.append(split).append("\n");
                }
        }

        final StringBuilder filtered = new StringBuilder();
        final String[] splits2 = notExculded.toString().split("\n");
        for (String split : splits2) {
            boolean passed = false;
            for (String filter : filters) {
                if (split.contains(filter)) {
                    passed = true;
                    break;
                }
            }

            if (passed) {
                filtered.append(split).append("\n");
            }
        }

        System.out.println(filtered.toString());
        history.append(builder.toString());
        builder.delete(0, builder.toString().length());
    }
}
