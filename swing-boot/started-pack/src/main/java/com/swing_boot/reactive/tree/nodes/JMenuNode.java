package com.swing_boot.reactive.tree.nodes;

import lombok.NonNull;

import javax.swing.JMenu;

public abstract class JMenuNode<J extends JMenu> extends JComponentNode<J> {
    public JMenuNode(@NonNull final J jComponent,
                     @NonNull final String nodeName)
    {
        super(jComponent, nodeName);
    }
}
