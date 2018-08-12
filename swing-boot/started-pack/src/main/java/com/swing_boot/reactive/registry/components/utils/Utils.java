package com.swing_boot.reactive.registry.components.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;

public class Utils {
    public static JFrame createTestFrame() {
        JFrame jFrame = new JFrame();
        jFrame.setTitle("<<<ТЕСТОВЫЙ ОКНО>>>");
        jFrame.setSize(new Dimension(200, 100));
        TextArea textArea = new TextArea("<<<ТЕСТОВЫЙ ТЕКСТ>>>");
        jFrame.add(textArea);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Utils.getMemoryInfo();
        addWindowListener(jFrame);
        return jFrame;
    }

    public static String getMemoryInfo() {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        long max = Runtime.getRuntime().maxMemory();
        String maxMemory = numberFormat.format(max);
        long free = Runtime.getRuntime().freeMemory();
        String freeMemory = numberFormat.format(free);
        String totalMemory = numberFormat.format(Runtime.getRuntime().totalMemory());
        long runned = max - free;
        String runMemory = numberFormat.format(runned);

        String s = "Max heap size: %s; " +
                "Free memory: %s; " +
                "Runned Memory: %s; " +
                "Total memory is %s;\n";
        String message = String.format(s, maxMemory, freeMemory, runMemory, totalMemory);
        System.out.println(message);
        return message;
    }
    public static final void addWindowListener(Window window) {

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Closed");
                e.getWindow().dispose();
                Utils.getMemoryInfo();
            }
        });
    }
}
