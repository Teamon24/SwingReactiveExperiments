package com.swing_boot.reactive.tree.nodes;

import lombok.NonNull;

import javax.swing.text.JTextComponent;

public abstract class JTextComponentNode<J extends JTextComponent> extends JComponentNode<J> {
    public JTextComponentNode(@NonNull final J jComponent,
                              @NonNull final String nodeName)
    {
        super(jComponent, nodeName);
    }
}
