package com.swing_boot.reactive.tree.components.colored;

import com.swing_boot.reactive.tree.nodes.JTextComponentNode;
import com.swing_boot.reactive.Events;
import com.swing_boot.reactive.SearchEvents;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.Color;
import java.awt.Dimension;

public final class NodeSearchFrame extends RandomColorFrame {

    public static final Color FOUND_BACKGROUND = new Color(0, 255, 113);
    private static NodeSearchFrame nodeSearchFrame = new NodeSearchFrame();

    private NodeSearchFrame() {
        super(new JFrame(), "searchFrame");
    }



    static {
        nodeSearchFrame.it.setPreferredSize(new Dimension(300, 300));
        nodeSearchFrame.it.setMinimumSize(new Dimension(300, 300));
        nodeSearchFrame.it.setSize(new Dimension(300, 300));
        final JTextArea jTextArea = new JTextArea();
        jTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                final Document document = e.getDocument();
                final String text = getText(document);
                nodeSearchFrame.dispatch(SearchEvents.SEARCH_NODE_BY_NAME, text);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                final Document document = e.getDocument();
                final String text = getText(document);
                nodeSearchFrame.dispatch(SearchEvents.SEARCH_NODE_BY_NAME, text);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                final Document document = e.getDocument();
                final String text = getText(document);
                nodeSearchFrame.dispatch(SearchEvents.SEARCH_NODE_BY_NAME, text);
            }
        });

        jTextArea.setPreferredSize(new Dimension(150, 40));
        jTextArea.setMinimumSize(new Dimension(150, 40));
        jTextArea.setSize(new Dimension(300, 300));
        jTextArea.setLocation(0, 0);
        final RandomColorPanel containerNode = new RandomColorPanel();
        jTextArea.setText("Введите текст");
        containerNode.add(new JTextComponentNode<JTextArea>(jTextArea, "searchTextArea") { public void update(Events e, Object o) {} });
        nodeSearchFrame.addRoot(containerNode);

    }

    private static String getText(Document document) {
        try {
            return document.getText(0, document.getLength());
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
        return "";
    }


    public static NodeSearchFrame instance() {
        return nodeSearchFrame;
    }
}
