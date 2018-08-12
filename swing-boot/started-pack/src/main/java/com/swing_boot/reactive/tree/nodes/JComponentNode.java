package com.swing_boot.reactive.tree.nodes;

import lombok.NonNull;

import javax.swing.JComponent;

public abstract class JComponentNode<J extends JComponent> extends ContainerNode<J> {
    public JComponentNode(@NonNull final J jComponent, @NonNull final String nodeName) {
        super(jComponent, nodeName);
    }

    public JComponentNode(@NonNull final J jComponent) {
        super(jComponent);
    }
}
