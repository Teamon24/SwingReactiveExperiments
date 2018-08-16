package com.swing_boot.reactive.tree.components.colored;

import com.swing_boot.reactive.ChangeColorEvents;
import com.swing_boot.reactive.tree.nodes.JButtonNode;
import com.swing_boot.reactive.tree.nodes.JTextComponentNode;
import com.swing_boot.reactive.Events;
import com.swing_boot.reactive.SearchEvents;
import lombok.NonNull;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.Color;
import java.awt.Dimension;

public final class NodeSearchFrame extends RandomColorFrame {

    public static final Color FOUND_BACKGROUND = new Color(0, 255, 113);
    private static NodeSearchFrame nodeSearchFrame = new NodeSearchFrame();
    private static final String PLACE_HOLDER = "Введите текст";

    private NodeSearchFrame() {
        super(new JFrame(), "searchFrame");
    }

    public static NodeSearchFrame instance() {
        return nodeSearchFrame;
    }

    static {
        nodeSearchFrame.it.setPreferredSize(new Dimension(300, 300));
        nodeSearchFrame.it.setMinimumSize(new Dimension(300, 300));
        nodeSearchFrame.it.setSize(new Dimension(300, 300));

        final JTextField threadAmountArea = new JTextField();
        threadAmountArea.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {

            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }

        });

        final JTextField threadSleepMillisArea = new JTextField();
        threadSleepMillisArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {

            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        final JTextComponentNode<JTextField> threadsAmount = new JTextFieldNode(threadAmountArea, "");
        final JTextComponentNode<JTextField> threadsSleep = new JTextFieldNode(threadSleepMillisArea, "");

        final JTextArea jTextArea = new JTextArea();
        jTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(@NonNull final DocumentEvent e) {
                final Document document = e.getDocument();
                final String text = getText(document);
                nodeSearchFrame.dispatch(nodeSearchFrame, SearchEvents.SEARCH_NODE_BY_NAME, text);
            }

            @Override
            public void removeUpdate(@NonNull final DocumentEvent e) {
                final Document document = e.getDocument();
                final String text = getText(document);
                nodeSearchFrame.dispatch(nodeSearchFrame, SearchEvents.SEARCH_NODE_BY_NAME, text);
            }

            @Override
            public void changedUpdate(@NonNull final DocumentEvent e) {
                final Document document = e.getDocument();
                final String text = getText(document);
                nodeSearchFrame.dispatch(nodeSearchFrame, SearchEvents.SEARCH_NODE_BY_NAME, text);
            }
        });

        jTextArea.setPreferredSize(new Dimension(150, 40));
        jTextArea.setMinimumSize(new Dimension(150, 40));
        jTextArea.setSize(new Dimension(300, 300));
        jTextArea.setLocation(0, 0);
        final RandomColorPanel containerNode = new RandomColorPanel();
        jTextArea.setText(PLACE_HOLDER);


        containerNode.add(new JTextComponentNode<JTextArea>(jTextArea, "searchTextArea") { public void update(Events e, Object v) {} });

        final JButtonNode jButtonNode = new JButtonNode(new JButton("Очистить"), "clearColorButton") { public void update(Events e, Object v) {}};
        jButtonNode.addDispatcher(actionEvent -> jButtonNode.dispatch(jButtonNode, new ChangeColorEvents(Color.WHITE), 0));
        containerNode.add(jButtonNode);
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
}
