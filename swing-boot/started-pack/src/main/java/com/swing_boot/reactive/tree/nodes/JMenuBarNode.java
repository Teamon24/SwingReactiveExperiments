package com.swing_boot.reactive.tree.nodes;

import lombok.NonNull;

import javax.swing.JMenuBar;

public abstract class JMenuBarNode<J extends JMenuBar> extends JComponentNode<J> {

    public JMenuBarNode(@NonNull final J jComponent,
                        @NonNull final String nodeName)
    {
        super(jComponent, nodeName);
    }
}
