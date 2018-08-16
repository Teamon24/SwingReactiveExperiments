package com.swing_boot.reactive.tree.utils;

import com.swing_boot.reactive.Events;
import com.swing_boot.reactive.tree.nodes.ReactiveTreeNode;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

public final class TreeUtils {

    public static List<String> getNodeNamesOf(@NonNull final ReactiveTreeNode<?> reactiveTreeNode) {
        final List<String> names = new ArrayList<>();
        names.add(reactiveTreeNode.name);
        for (ReactiveTreeNode<?> treeNode : reactiveTreeNode.getChildren()) {
            names.add(treeNode.name);
            final List<String> grandChildrenNames = TreeUtils.getNodeNamesOf(treeNode);
            names.addAll(grandChildrenNames);
        }
        return names;
    }

    public static Collection<Class<? extends Events>> getAllConsumableEventsBelow(@NonNull final ReactiveTreeNode<?> reactiveTreeNode) {
        LinkedHashSet<Class<? extends Events>> acceptable = new LinkedHashSet<>();
        for (ReactiveTreeNode<?> child : reactiveTreeNode.getChildren()) {
            acceptable.addAll(child.getConsumableEvents());
            acceptable.addAll(TreeUtils.getAllConsumableEventsBelow(child));
        }
        return acceptable;
    }

    public static Collection<Events> getAllEmittedEventsBelow(@NonNull final ReactiveTreeNode<?> reactiveTreeNode) {
        LinkedHashSet<Events> emittedEvents = new LinkedHashSet<>();
        for (ReactiveTreeNode<?> child : reactiveTreeNode.getChildren()) {
            emittedEvents.addAll(child.getEmittedEvents());
            emittedEvents.addAll(TreeUtils.getAllEmittedEventsBelow(child));
        }
        return emittedEvents;
    }

    public static boolean isEventConsumable(Collection<Class<? extends Events>> grandChildrenConsumableEvents, Events event) {
        for (Class<? extends Events> grandChildEvent : grandChildrenConsumableEvents) {
            if (event.getClass().equals(grandChildEvent)) {
                return true;
            }


        }

        return false;
    }
}
