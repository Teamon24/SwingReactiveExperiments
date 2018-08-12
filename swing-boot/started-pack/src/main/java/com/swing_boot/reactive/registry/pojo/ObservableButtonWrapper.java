package com.swing_boot.reactive.registry.pojo;

import com.swing_boot.reactive.Events;
import lombok.NonNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ObservableButtonWrapper extends ObservableComponentWrapper<JButton> {

    protected ObservableButtonWrapper() {
        super(new JButton());
    }

    public static void addActionNotificator(@NonNull final ObservableButtonWrapper wrapper,
                                            @NonNull final Object message,
                                            @NonNull final Events emmitedEvent)
    {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wrapper.send(wrapper, message, emmitedEvent);
            }
        };

        wrapper.getContent().addActionListener(actionListener);
    }

    public static void addActionNotificator(@NonNull final ObservableButtonWrapper button,
                                            @NonNull final ActionListener actionListener)
    {
        button.getContent().addActionListener(actionListener);
    }
}
