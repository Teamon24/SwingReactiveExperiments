package com.swing_boot.reactive.registry.components;

import javax.swing.*;

public class PrintPanel extends JPanel {

    public final PrintEditorPane printEditorPane = new PrintEditorPane();

    {
        this.add(printEditorPane.component);
    }

}
