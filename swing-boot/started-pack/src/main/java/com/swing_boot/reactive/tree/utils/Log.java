package com.swing_boot.reactive.tree.utils;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.fusesource.jansi.Ansi;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public final class Log {

    private Log() {}
    public static final Log logger = new Log();
    private static StringBuffer builder = new StringBuffer();


    private static int nodeIndent   = 65;
    private static int methodIndent = 45;
    private static int threadIndent = 35;
    private static int numberIndent = 15;

    private static String object = "%-" + nodeIndent + "s";
    private static String method = "#%-" + methodIndent + "s";
    private static String threadTemplate = " %-" + threadIndent + "s";
    private static String numberTemplate = " #%-" + numberIndent + "d";

    private static String dateTemplate = "dd.MM.YYYY HH:mm:ss:SS";
    private static String timeTemplate = "mm:ss:SS ";

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(dateTemplate, Locale.getDefault());
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(timeTemplate, Locale.getDefault());

    private static Integer lineLength = nodeIndent + methodIndent + threadIndent + 100;

    public static Ansi.Color dateColor = Ansi.Color.BLUE;
    public static AtomicInteger operations = new AtomicInteger(0);

    public synchronized Log log(@NonNull final String method,
                                @NonNull final String template,
                                @NonNull final Object...params)
    {
        date(dateColor).thread().counter().method(method).text(template, params).ln().flush();
        return logger;
    }

    public synchronized Log log(@NonNull final Object object,
                                @NonNull final String method,
                                @NonNull final Object...methodArgs)
    {
        StringBuilder argsTemplate= new StringBuilder();
        for (Object o : methodArgs) {
            argsTemplate.append("%s").append(", ");
        }

        date(dateColor).thread().counter().object(object).method(method).text(argsTemplate.toString(), methodArgs).ln().flush();
        return logger;
    }

    public synchronized Log log(@NonNull final Object object,
                                @NonNull final String method,
                                @NonNull final Collection collection)
    {
        String items = StringUtils.join(collection, "\n");
        date(dateColor).thread().counter().object(object).method(method).text("{%s}", items).ln().flush();
        return logger;
    }

    public synchronized Log time(@NonNull final String template,
                                 @NonNull final Long time)
    {
        final String dateString = colorText(Ansi.Color.MAGENTA, timeStr(time));
        date(dateColor).thread().counter().text(template, dateString).ln().flush();
        return logger;
    }

    public synchronized Log line() {
        builder.append(getLine(lineLength));
        flush();
        return logger;
    }





    private Log text(@NonNull final String template,
                     @NonNull final Object... params)
    {
        builder.append(String.format(template, params));
        return logger;
    }

    private Log date(@NonNull final Ansi.Color color) {
        final String dateString = colorText(color, dateStr(new Date()));
        builder.append(dateString);
        return logger;
    }

    private Log date() {
        final String dateString = colorText(dateColor, dateStr(new Date()));
        builder.append(dateString);
        return logger;
    }

    private Log date(@NonNull final Long date) {
        builder.append(dateStr(new Date(date)));
        return logger;
    }

    private Log thread() {
        final String name = Thread.currentThread().getName();
        final String toPrint = String.format(threadTemplate, colorText(Ansi.Color.CYAN, name));
        builder.append(toPrint);
        return logger;
    }

    private Log counter() {
        final int i = operations.incrementAndGet();
        builder.append(String.format(numberTemplate, i));
        return logger;
    }

    private Log object(@NonNull final Object object) {
        final String toPrint = String.format(Log.object, object);
        builder.append(toPrint);
        return logger;
    }

    private Log method(@NonNull final String m) {
        final String toPrint = String.format(method, m);
        builder.append(toPrint);
        return logger;
    }

    private Log cln() {
        builder.append(":");
        return logger;
    }

    private Log ln() {
        builder.append("\n");
        return logger;
    }

    private String getLine(@NonNull final Integer lineLength) {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < lineLength; i++) {
            line.append("-");
        }
        return line.append("\n").toString();
    }

    private String colorText(Ansi.Color color, String text) {
        return Ansi.ansi().fg(color).a(text).reset().toString();
    }

    private String dateStr(Date date) {
        return "[" + DATE_FORMAT.format(date) + "]";
    }

    private String timeStr(@NonNull final Long time) {
        return "[" + TIME_FORMAT.format(new Date(time)) + "]";
    }

    private void flush() {
        final String x = builder.toString();
        System.out.print(x);
        builder.replace(0, x.length(), "");
    }

    public void log(Exception e) {
        final StackTraceElement[] stackTrace = e.getStackTrace();
        for (StackTraceElement element : stackTrace) {
            text("%s", element).ln();
        }
    }
}
