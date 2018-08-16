package com.swing_boot.reactive.registry.components;

import com.swing_boot.reactive.registry.pojo.ObservableComponentWrapper;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.swing_boot.reactive.CommonEvents.PRINT_APP_ROOT_INFO;

@Component
public class PrintButton extends ObservableComponentWrapper<JButton> {

    public PrintButton() {
        super(new JButton());
        super.getContent().setText("PrintButton");
        super.register(this, PRINT_APP_ROOT_INFO);
        PrintButton.addActionListener(this);
    }

    private static ActionListener addActionListener(final PrintButton jButton) {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jButton.send(jButton, "Print!!!!!", PRINT_APP_ROOT_INFO);
            }
        };
        jButton.getContent().addActionListener(actionListener);
        return actionListener;
    }
}
