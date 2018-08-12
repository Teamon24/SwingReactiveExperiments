package com.swing_boot.reactive.tree.nodes;

import com.swing_boot.reactive.Events;
import com.swing_boot.reactive.tree.utils.LogUtils;
import com.swing_boot.reactive.tree.utils.TreeUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public abstract class ReactiveTreeNode<It extends Component>
        implements Observer {

    public String name;

    public final It it;

    @Getter
    @Setter
    private ReactiveTreeNode<?> parent;

    @Getter
    protected Set<Events> consumableEvents = new LinkedHashSet<>();

    @Getter
    protected Set<Events> emittedEvents = new LinkedHashSet<>();

    @Getter
    private List<ReactiveTreeNode<?>> children = new ArrayList<>();

    public ReactiveTreeNode(It it, @NonNull final String nodeName) {
        this.it = it;
        this.name = nodeName;
    }

    public ReactiveTreeNode(It it) {
        this.it = it;
        this.name = "default_node_name";
    }

    public void dispatch(@NonNull final Events event,
                         @NonNull final Object message) {

        ReactiveTreeThreads.dispatchUp(this, this, event, message);
        ReactiveTreeThreads.dispatchDown(this, this, event, message);
    }

    public void dispatchUp(@NonNull final ReactiveTreeNode<?> dispatchingNode,
                           @NonNull final Events event,
                           @NonNull final Object message) {
        if (TreeUtils.isEventConsumable(this.consumableEvents, event)) {
            this.update(event, message);
        }
        if (this.hasParent()) {
            LogUtils.LOGGER.soutln(this, "dispatchUp", event, message);
            if (TreeUtils.isEventConsumable(this.parent.consumableEvents, event)) {
                this.parent.update(event, message);
                ReactiveTreeThreads.dispatchUp(this.parent, this.parent, event, message);
            }
            ReactiveTreeNode<?> childToExceptFromTraversing = dispatchingNode;
            this.parent.dispatchDown(childToExceptFromTraversing, event, message);
        }
    }

    public void dispatchDown(@NonNull final ReactiveTreeNode<?> dispatchingChild,
                             @NonNull final Events event,
                             @NonNull final Object message) {

        ReactiveTreeNode<?> childForCurrentThread = this.getChildForThread(dispatchingChild);

        for (ReactiveTreeNode<?> currentChild : this.children) {
            LogUtils.LOGGER.soutln(currentChild, "dispatchDown", event, message);
            if (currentChild != dispatchingChild) {
                LogUtils.LOGGER.soutln(currentChild, "consumableEvents", currentChild.consumableEvents);
                if (TreeUtils.isEventConsumable(currentChild.consumableEvents, event)) {
                    currentChild.update(event, message);
                }
                final Collection<Events> grandChildrenConsumableEvents = TreeUtils.getAllConsumableEventsBelow(currentChild);
                LogUtils.LOGGER.soutln(currentChild, "childrenConsumableEvents", grandChildrenConsumableEvents);
                if (TreeUtils.isEventConsumable(grandChildrenConsumableEvents, event)) {
                    if (childForCurrentThread == currentChild || this.children.size() == 1) {
                        currentChild.dispatchDown(dispatchingChild, event, message);
                    } else {
                        ReactiveTreeThreads.dispatchDown(currentChild, dispatchingChild, event, message);
                    }
                }
            }
        }
    }

    private ReactiveTreeNode<?> getChildForThread(@NonNull ReactiveTreeNode<?> dispatchingChild) {
        final boolean hasMoreOneChild = this.children.size() > 1;

        int i = RandomUtils.nextInt(0, this.children.size());
        ReactiveTreeNode<?> childForCurrentThread = hasMoreOneChild ? this.children.get(i) : null;
        while (dispatchingChild == childForCurrentThread && childForCurrentThread != null) {
            i = RandomUtils.nextInt(0, this.children.size());
            childForCurrentThread = this.children.get(i);
        }
        return childForCurrentThread;
    }

    public void addChild(@NonNull final ReactiveTreeNode<?> child) {
        this.children.add(child);
        child.setParent(this);
    }

    public void deleteChild(@NonNull final ReactiveTreeNode<?> child) {
        final String className = this.getClass().getSimpleName();
        throw new UnsupportedOperationException(className + "#deleteChild() has no implementation");
    }

    public boolean hasParent() {
        return this.parent != null;
    }

    public boolean hasChildren() {
        return !this.children.isEmpty();
    }

    public String getGenealogyName() {
        final String grandParentGeneology;
        if (this.hasParent()) {
            grandParentGeneology = this.parent.getGenealogyName();
        } else {
            grandParentGeneology = "";
        }
        return StringUtils.join(grandParentGeneology, this.name, ".");
    }



    @Override
    public String toString() {
        return this.name;
    }

}
