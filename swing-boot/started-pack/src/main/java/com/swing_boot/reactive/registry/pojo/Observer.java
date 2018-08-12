package com.swing_boot.reactive.registry.pojo;


import com.swing_boot.reactive.Events;

public interface Observer {

    void observe(Events event);

    void update(Object value, Events event);

    void stopToObserve(Events event);
}
