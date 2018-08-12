package com.swing_boot.reactive.tree.nodes;

import lombok.NonNull;

import javax.swing.JComponent;
import javax.swing.JPanel;

public abstract class JPanelNode extends JComponentNode<JPanel> {
    public JPanelNode(@NonNull final JPanel jPanel, @NonNull final String nodeName) {
        super(jPanel, nodeName);
    }
}
