package com.swing_boot.reactive.registry.pojo;


import com.swing_boot.reactive.Events;

public abstract class AbstractObserver implements Observer {

    @Override
    public void observe(Events event) {
        Subscriptions.subscribe(event, this);
    }

    @Override
    public void stopToObserve(Events event) {
        Subscriptions.unsubscribe(event, this);
    }
}
