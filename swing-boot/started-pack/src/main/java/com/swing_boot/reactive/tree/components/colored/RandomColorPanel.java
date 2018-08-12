package com.swing_boot.reactive.tree.components.colored;

import com.google.common.collect.Sets;
import com.swing_boot.reactive.tree.nodes.JPanelNode;
import com.swing_boot.reactive.ChangeColorEvents;
import com.swing_boot.reactive.Events;
import com.swing_boot.reactive.tree.utils.LogUtils;
import com.swing_boot.reactive.SearchEvents;
import lombok.NonNull;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import java.awt.Color;

import static com.swing_boot.reactive.tree.components.colored.NodeSearchFrame.FOUND_BACKGROUND;

public class RandomColorPanel extends JPanelNode {

    public static int counter;
    private final TitledBorder border = new TitledBorder(super.name);
    private final JLabel label = new JLabel();
    private Color color;

    {
        super.consumableEvents.addAll(Sets.newHashSet(ChangeColorEvents.TYPE));
        super.consumableEvents.addAll(Sets.newHashSet(SearchEvents.values()));
    }

    public RandomColorPanel() {
        this("panel_" + ++RandomColorPanel.counter);
    }

    public RandomColorPanel(@NonNull final String name) {
        super(new JPanel(), name);
        label.setText("                                            ");
        super.it.add(label);
        super.it.setBorder(border);
    }

    @Override
    public void update(@NonNull final Events event, Object value) {
        if (value instanceof Integer && event instanceof ChangeColorEvents) {
            final Integer i = (Integer) value;
            final ChangeColorEvents changeColorEvent = (ChangeColorEvents) event;
            this.color = changeColorEvent.color;
            super.it.setBackground(this.color);
            super.it.setForeground(Color.WHITE);

            this.border.setTitleColor(Color.WHITE);
            this.border.setTitle(super.name + " <- " + i);
            this.label.setForeground(Color.WHITE);
            LogUtils.LOGGER.soutln(this, "update", event, value);
        }

        if (event == SearchEvents.SEARCH_NODE_BY_NAME && value instanceof String) {
            final String searchingText = String.valueOf(value);
            if (!searchingText.isEmpty()) {
                final String text = this.border.getTitle();
                final boolean contains = text.contains(searchingText);
                final boolean startsWith = text.startsWith(searchingText);
                if (contains && startsWith) {
                    super.it.setBackground(FOUND_BACKGROUND);
                    LogUtils.LOGGER.soutln(this, "update", event, value);
                } else {
                    super.it.setBackground(this.color);
                }
            } else {
                super.it.setBackground(this.color);
            }
        }
    }
}
