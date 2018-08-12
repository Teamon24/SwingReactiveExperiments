package com.swing_boot.reactive.registry.pojo;


import com.swing_boot.reactive.Events;

public interface Observable {

    void register(Observable observable, Events event);

    void unregister(Events event);

    void send(Observable observable, Object message, Events event);

    void unsubscribe(Observer observer, Events event, Observable observable);
}
