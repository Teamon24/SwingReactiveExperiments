package com.swing_boot.reactive.registry.components;

import com.swing_boot.reactive.registry.components.utils.Utils;
import com.swing_boot.reactive.registry.components.utils.ContextProvider;
import com.swing_boot.reactive.registry.pojo.ObservableButtonWrapper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.swing_boot.reactive.CommonEvents.OPEN;

public class OpenButton extends ObservableButtonWrapper {


    {
        super.register(this, OPEN);
        super.getContent().setText("OPEN BUTTON");
    }

    {
        addActionNotificator(this, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utils.createTestFrame();
                final AppFrame bean = ContextProvider.getBean(AppFrame.class);
                bean.setVisible(true);
                bean.setEnabled(true);
                bean.setFocusable(true);
                bean.setResizable(true);
            }
        });
    }
}
