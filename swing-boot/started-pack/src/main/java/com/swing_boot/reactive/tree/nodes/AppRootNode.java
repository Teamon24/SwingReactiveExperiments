package com.swing_boot.reactive.tree.nodes;

import com.google.common.collect.Sets;
import com.swing_boot.reactive.tree.nodes.ReactiveTreeNode;
import com.swing_boot.reactive.ChangeColorEvents;
import com.swing_boot.reactive.Events;
import com.swing_boot.reactive.tree.utils.LogUtils;
import lombok.NonNull;

import java.awt.Component;

public class AppRootNode extends ReactiveTreeNode<Component> {

    {
        super.consumableEvents.addAll(Sets.newHashSet(ChangeColorEvents.TYPE));
    }

    public AppRootNode(@NonNull final Component component, @NonNull final String nodeName) {
        super(component, nodeName);
    }

    @Override
    public void update(Events event, Object value) {
        LogUtils.LOGGER.soutln(this, "update", event, value);
    }

}
