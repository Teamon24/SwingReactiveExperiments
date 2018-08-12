package com.swing_boot.reactive.registry.components;

import com.swing_boot.reactive.registry.ComponentWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
public class ActionPanel implements ComponentWrapper<JPanel> {

    public final JPanel content = new JPanel();
    public final PrintButton printButton;
    public final OpenButton openButton = new OpenButton();

    {
        content.add(openButton.getContent());
    }

    @Autowired
    public ActionPanel(PrintButton printButton) {
        this.printButton = printButton;
        content.add(this.printButton.getContent());
    }

    @Override
    public JPanel getContent() {
        return this.content;
    }
}
