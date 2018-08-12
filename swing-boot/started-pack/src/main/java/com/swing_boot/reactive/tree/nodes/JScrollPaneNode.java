package com.swing_boot.reactive.tree.nodes;

import lombok.NonNull;

import javax.swing.JScrollPane;

public abstract class JScrollPaneNode<J extends JScrollPane> extends JComponentNode<J> {

    public JScrollPaneNode(@NonNull final J jComponent,
                           @NonNull final String nodeName)
    {
        super(jComponent, nodeName);
    }
}
