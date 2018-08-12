package com.swing_boot.reactive.tree.components.colored;

import com.google.common.collect.Sets;
import com.swing_boot.reactive.tree.nodes.JButtonNode;
import com.swing_boot.reactive.ChangeColorEvents;
import com.swing_boot.reactive.Events;
import com.swing_boot.reactive.tree.utils.LogUtils;
import com.swing_boot.reactive.SearchEvents;
import lombok.NonNull;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

import static com.swing_boot.reactive.tree.components.colored.NodeSearchFrame.FOUND_BACKGROUND;

public class RandomColorButton extends JButtonNode {

    private static AtomicInteger counter = new AtomicInteger(0);
    private int number;
    private Color color;

    {
        super.consumableEvents.addAll(Sets.newHashSet(ChangeColorEvents.TYPE));
        super.consumableEvents.addAll(Sets.newHashSet(SearchEvents.values()));
    }

    public RandomColorButton() {
        this("randomColorButton_" + RandomColorButton.counter.incrementAndGet());
    }

    private RandomColorButton(@NonNull final String nodeName) {
        super(new JButton(), nodeName);
        super.it.setSize(new Dimension(80, 80));
        this.number = counter.get();
        super.it.setText("#" + this.number);
        super.addDispatcher(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object message = 1;
                final HashSet<Pair<Events, Object>> pairs = Sets.newHashSet(Pair.of((Events) new ChangeColorEvents(new Color(34, 34, 34)), message));
                final ActionListener dispatcher = RandomColorButton.super.getDispatcher(pairs);
                dispatcher.actionPerformed(e);
            }
        });
    }

    public void update(Events event, Object value) {
        if (value instanceof Integer && event instanceof ChangeColorEvents) {
            final Integer i = (Integer) value;
            super.it.setText("#" + this.number + " <- " + String.valueOf(i));
            super.it.setForeground(Color.WHITE);
            this.color = ((ChangeColorEvents) event).color;
            super.it.setBackground(this.color);
            LogUtils.LOGGER.soutln(this, "update", event, value);
        }

        if (event == SearchEvents.SEARCH_NODE_BY_NAME && value instanceof String) {
            final String searchingText = String.valueOf(value);
            if (!searchingText.isEmpty()) {
                final String text = super.it.getText();
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
