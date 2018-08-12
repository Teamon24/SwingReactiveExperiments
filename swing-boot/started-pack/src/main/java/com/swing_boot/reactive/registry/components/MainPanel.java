package com.swing_boot.reactive.registry.components;

import com.swing_boot.reactive.registry.ComponentWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
public class MainPanel implements ComponentWrapper<JPanel> {

    public JPanel content = new JPanel();

    public ActionPanel actionPanel;
    public PrintPanel printPanel = new PrintPanel();

    @Autowired
    public MainPanel(ActionPanel actionPanel) {
        this.actionPanel = actionPanel;
        content.add(this.actionPanel.content);
        content.add(printPanel);
    }


    @Override
    public JPanel getContent() {
        return this.content;
    }
}
