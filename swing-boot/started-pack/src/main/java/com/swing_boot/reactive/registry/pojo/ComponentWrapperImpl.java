package com.swing_boot.reactive.registry.pojo;

import com.swing_boot.reactive.registry.ComponentWrapper;

/**
 * Абстрактная обертка, содержащая логику, связанную с внутренним ui-компонентом.
 * @param <Content> тип содержимого ui-компонента обертки.
 */
public class ComponentWrapperImpl<Content extends java.awt.Component> implements ComponentWrapper<Content> {

    /**
     * Содержимое обертки. */
    public final Content content;

    @Override
    public Content getContent() {
        return this.content;
    }

    /**
     * @param content оборачиваемый компонент.
     */
    protected ComponentWrapperImpl(Content content) {
        this.content = content;
    }

}
