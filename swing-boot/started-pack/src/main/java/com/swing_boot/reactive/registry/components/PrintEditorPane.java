package com.swing_boot.reactive.registry.components;

import com.swing_boot.reactive.registry.pojo.AbstractObserverComponentWrapper;
import com.swing_boot.reactive.CommonEvents;
import com.swing_boot.reactive.Events;

import javax.swing.*;

public class PrintEditorPane extends AbstractObserverComponentWrapper<JEditorPane> {

    private String text;

    protected PrintEditorPane() {
        super(new JEditorPane());
        super.component.setEditable(true);
        super.component.setText("Initial text");
        super.observe(CommonEvents.PRINT_APP_ROOT_INFO);
    }

    @Override
    public void update(Object value, Events event) {
        super.update(value, event);
        if (event == CommonEvents.PRINT_APP_ROOT_INFO) {
            if (text == null) {
                text = (String) value;
            } else {
                text += text;
            }
            this.component.setText(text);
        }
    }
}
