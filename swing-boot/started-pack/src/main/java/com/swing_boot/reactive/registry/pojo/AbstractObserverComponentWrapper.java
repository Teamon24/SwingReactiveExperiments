package com.swing_boot.reactive.registry.pojo;

import com.swing_boot.reactive.registry.ComponentWrapper;
import com.swing_boot.reactive.Events;

public abstract class AbstractObserverComponentWrapper<C extends java.awt.Component>
        extends AbstractObserver
        implements ComponentWrapper<C>
{

    public final C component;

    protected AbstractObserverComponentWrapper(C component) {
        super();
        this.component = component;
    }

    @Override
    public C getContent() {
        return this.component;
    }

    @Override
    public void update(Object value, Events event) {
    }
}
