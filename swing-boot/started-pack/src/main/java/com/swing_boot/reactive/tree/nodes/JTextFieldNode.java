package com.swing_boot.reactive.tree.nodes;

import javax.swing.JTextField;

public abstract class JTextFieldNode extends JTextComponentNode<JTextField> {
    public JTextFieldNode(JTextField jTextComponent, String nodeName) {
        super(jTextComponent, nodeName);
    }

}
