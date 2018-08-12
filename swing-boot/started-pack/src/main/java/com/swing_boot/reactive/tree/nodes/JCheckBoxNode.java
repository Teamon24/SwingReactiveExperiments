package com.swing_boot.reactive.tree.nodes;

import lombok.NonNull;

import javax.swing.JCheckBox;

public abstract class JCheckBoxNode<J extends JCheckBox> extends JComponentNode<J> {

    public JCheckBoxNode(@NonNull final J jComponent, @NonNull final String nodeName) {
        super(jComponent, nodeName);
    }
}
