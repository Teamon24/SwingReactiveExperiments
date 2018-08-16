package com.swing_boot.reactive.tree.nodes;


import com.swing_boot.reactive.Events;

public interface Observer {

    void update(Events e, Object v);
}
