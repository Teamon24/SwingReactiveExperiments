package com.swing_boot.reactive.registry.components;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
@Scope(scopeName = "prototype")
public class AppFrame extends JFrame {

    public final MainPanel mainPanel;

    @Autowired
    public AppFrame(@NonNull final MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        this.getContentPane().add(mainPanel.content);
        this.setSize(new Dimension(400, 400));
    }
}
