package com.swing_boot.reactive.registry.pojo;


import com.swing_boot.reactive.Events;

public class ObservableImpl implements Observable {

    @Override
    public void register(Observable observable, Events event) {
        Subscriptions.register(this,event);
    }

    @Override
    public void unregister(Events event) {
        Subscriptions.delete(event, this);
    }

    @Override
    public void send(Observable observable, Object message, Events event) {
        Subscriptions.send(observable, message, event);
    }

    @Override
    public void unsubscribe(Observer observer, Events event, Observable observable) {
        Subscriptions.unsubscribe(observer, event, observable);
    }
}
