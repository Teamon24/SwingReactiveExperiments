package com.swing_boot.reactive.tree.nodes;

import lombok.NonNull;

import javax.swing.JTabbedPane;

public abstract class JTabbedPaneNode<J extends JTabbedPane> extends JComponentNode<J> {

    public JTabbedPaneNode(@NonNull final J jComponent,
                           @NonNull final String nodeName)
    {
        super(jComponent, nodeName);
    }
}
