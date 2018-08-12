package com.swing_boot.reactive.registry.pojo;

import com.swing_boot.reactive.registry.ComponentWrapper;
import lombok.NonNull;

public abstract
                  class ReactiveDuplexComponentWrapper<Content extends java.awt.Component>
                extends ReactiveDuplex
             implements ComponentWrapper<Content>
{

    public final ComponentWrapper<Content> wrapper;

    protected ReactiveDuplexComponentWrapper(@NonNull final Content content) {
        wrapper = new ComponentWrapperImpl<>(content);
    }

    @Override
    public Content getContent() {
        return this.wrapper.getContent();
    }
}
