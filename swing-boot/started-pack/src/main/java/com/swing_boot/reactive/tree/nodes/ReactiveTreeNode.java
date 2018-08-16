package com.swing_boot.reactive.tree.nodes;

import com.swing_boot.reactive.Events;
import com.swing_boot.reactive.tree.threads.ReactiveTreeThreads;
import com.swing_boot.reactive.tree.utils.LogUtils;
import com.swing_boot.reactive.tree.utils.TreeUtils;
import lombok.Getter;
import lombok.NonNull;
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
    private ReactiveTreeNode<?> parent;

    public void setParent(ReactiveTreeNode<? extends Component> parent) {
        this.parent = parent;
    }

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

    public void dispatch(@NonNull final ReactiveTreeNode<?> dispatchingChild,
                         @NonNull final Events event,
                         @NonNull final Object message) {



        if (this.hasParent()) {
            if (this.parent.hasOnlyOneChild()) {
                this.parent.dispatch(this, event, message);
            } else {
                ReactiveTreeThreads.dispatchUp(this, dispatchingChild, event, message);
            }
        }

        if (this.hasNoParent() && this.hasChildren()) {
            this.dispatchDown(dispatchingChild, event, message);
        }
    }

    public void dispatchDown(@NonNull final ReactiveTreeNode<?> dispatchingChild,
                             @NonNull final Events event,
                             @NonNull final Object message) {

        LogUtils.LOGGER.soutln(this, "consumableEvents", this.consumableEvents);
        if (TreeUtils.isEventConsumable(this.consumableEvents, event)) {
            this.update(event, message);
        }

        final ReactiveTreeNode<?> nextChildForDispatching = this.getChildForThread(dispatchingChild);

        for (ReactiveTreeNode<?> currentChild : this.children) {

            if (dispatchingChild != currentChild) {

                final Collection<Events> childrenConsumableEvents = TreeUtils.getAllConsumableEventsBelow(this);
                LogUtils.LOGGER.soutln(currentChild, "childrenConsumableEvents", childrenConsumableEvents);
                if (TreeUtils.isEventConsumable(childrenConsumableEvents, event)) {

                    if (nextChildForDispatching != null && nextChildForDispatching == currentChild) {

                        LogUtils.LOGGER.soutln(nextChildForDispatching, "dispatchDown", event, message);
                        nextChildForDispatching.dispatchDown(nextChildForDispatching, event, message);

                    } else if (this.hasOnlyOneChild()) {

                        LogUtils.LOGGER.soutln(currentChild, "dispatchDown", event, message);
                        currentChild.dispatchDown(currentChild, event, message);

                    } else {
                        ReactiveTreeThreads.dispatchDown(currentChild, event, message);
                    }
                }
            }
        }
    }

    private boolean hasNoParent() {
        return this.parent == null;
    }

    private boolean hasNoChildren() {
        return this.children.isEmpty();
    }

    private boolean hasOnlyOneChild() {
        return this.children.size() == 1;
    }

    private boolean hasMoreOneChild() {
        return this.children.size() > 1;
    }

    private ReactiveTreeNode<?> getChildForThread(@NonNull final ReactiveTreeNode<?> dispatchingChild) {
        if (this.hasNoChildren()) {
            return null;
        }

        int i = RandomUtils.nextInt(0, this.children.size());
        ReactiveTreeNode<?> childForCurrentThread = this.children.get(i);
        while (dispatchingChild == childForCurrentThread) {
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
