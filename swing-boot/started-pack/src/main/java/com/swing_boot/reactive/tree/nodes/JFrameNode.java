package com.swing_boot.reactive.tree.nodes;

import lombok.NonNull;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

public abstract class JFrameNode<J extends JFrame> extends ContainerNode<J> {

    public JFrameNode(@NonNull final J jComponent,
                      @NonNull final String nodeName)
    {
        super(jComponent, nodeName);
    }

    public void addRoot(ContainerNode<?> containerNode) {
        super.addChild(containerNode);
        super.it.getContentPane().add(containerNode.it);
    }

}
