package com.swing_boot.reactive.tree.components.colored;

import com.swing_boot.reactive.ChangeColorEvents;
import com.swing_boot.reactive.tree.nodes.JButtonNode;
import com.swing_boot.reactive.tree.nodes.JTextComponentNode;
import com.swing_boot.reactive.Events;
import com.swing_boot.reactive.SearchEvents;
import com.swing_boot.reactive.tree.nodes.JTextFieldNode;
import lombok.NonNull;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.Color;
import java.awt.Dimension;

public final class DebugFrame extends RandomColorFrame {

    public static final Color FOUND_BACKGROUND = new Color(0, 255, 113);
    private static DebugFrame debugFrame = new DebugFrame();
    private static final String PLACE_HOLDER = "Введите текст";

    private DebugFrame() {
        super(new JFrame(), "searchFrame");
    }

    public static DebugFrame instance() {
        return debugFrame;
    }

    static {
        debugFrame.it.setPreferredSize(new Dimension(300, 300));
        debugFrame.it.setMinimumSize(new Dimension(300, 300));
        debugFrame.it.setSize(new Dimension(300, 300));
        debugFrame.it.setAlwaysOnTop(true);

        final JTextField threadAmountArea = new JTextField();
        threadAmountArea.getDocument().addDocumentListener(new DocumentListener() {

            @Override public void insertUpdate(DocumentEvent e) {}
            @Override public void removeUpdate(DocumentEvent e) {}
            @Override public void changedUpdate(DocumentEvent e) {}

        });

        final JTextField threadSleepMillisArea = new JTextField();
        threadSleepMillisArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) {}
            @Override public void removeUpdate(DocumentEvent e) {}
            @Override public void changedUpdate(DocumentEvent e) {}
        });

        final JTextComponentNode<JTextField> threadsAmount = new JTextFieldNode(threadAmountArea, "") { public void update(Events e, Object v) {} };
        final JTextComponentNode<JTextField> threadsSleep  = new JTextFieldNode(threadSleepMillisArea, "") { public void update(Events e, Object v) {} };

        final JTextArea jTextArea = new JTextArea();

        jTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(@NonNull final DocumentEvent e) {
                final Document document = e.getDocument();
                final String text = getText(document);
                debugFrame.dispatchUp(debugFrame, SearchEvents.SEARCH_NODE_BY_NAME, text);
            }

            @Override
            public void removeUpdate(@NonNull final DocumentEvent e) {
                final Document document = e.getDocument();
                final String text = getText(document);
                debugFrame.dispatchUp(debugFrame, SearchEvents.SEARCH_NODE_BY_NAME, text);
            }

            @Override
            public void changedUpdate(@NonNull final DocumentEvent e) {
                final Document document = e.getDocument();
                final String text = getText(document);
                debugFrame.dispatchUp(debugFrame, SearchEvents.SEARCH_NODE_BY_NAME, text);
            }
        });

        jTextArea.setPreferredSize(new Dimension(150, 40));
        jTextArea.setMinimumSize(new Dimension(150, 40));
        jTextArea.setSize(new Dimension(300, 300));
        jTextArea.setLocation(0, 0);
        final RandomColorPanel containerNode = new RandomColorPanel();
        jTextArea.setText(PLACE_HOLDER);

        final JButtonNode clearColorButton = new JButtonNode(new JButton("Очистить"), "clearColorButton") { public void update(Events e, Object v) {}};
        final JButtonNode colorizeButton = new JButtonNode(new JButton("Окрасить"), "colorizeButton") { public void update(Events e, Object v) {}};

        clearColorButton.addActionDispatcher(
                actionEvent ->
                        SwingUtilities.invokeLater(() ->
                                clearColorButton.dispatchUp(clearColorButton, ChangeColorEvents.ClearColor.CLEAR_COLOR, 0)));

        colorizeButton.addActionDispatcher(
                actionEvent ->
                    SwingUtilities.invokeLater(() ->
                            colorizeButton.dispatchUp(colorizeButton, new ChangeColorEvents(new Color(34, 34, 34)), 1)
                    )
        );

        final JTextComponentNode<JTextArea> searchTreeNode = new JTextComponentNode<JTextArea>(jTextArea, "searchTextArea") { public void update(Events e, Object v) {} };

        containerNode.add(searchTreeNode);
        containerNode.add(clearColorButton);
        containerNode.add(colorizeButton);
        debugFrame.addRoot(containerNode);
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
