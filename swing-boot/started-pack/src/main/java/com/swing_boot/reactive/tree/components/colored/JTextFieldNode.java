package com.swing_boot.reactive.tree.components.colored;

import com.swing_boot.reactive.Events;
import com.swing_boot.reactive.tree.nodes.JTextComponentNode;

import javax.swing.JTextField;

public class JTextFieldNode extends JTextComponentNode<JTextField> {
    public JTextFieldNode(JTextField jTextComponent, String nodeName) {
        super(jTextComponent, nodeName);
    }

    @Override public void update(Events e, Object v) {}
}
