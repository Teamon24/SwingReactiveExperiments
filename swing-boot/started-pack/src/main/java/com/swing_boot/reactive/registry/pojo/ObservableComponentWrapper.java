package com.swing_boot.reactive.registry.pojo;

import com.swing_boot.reactive.registry.ComponentWrapper;

/**
 * Компонент пользьзовательского интерфейса с возможностью рассылки сообщений компонентам-подписчикам.
 * @param <Content> тип содержащегося компонента.
 */
public class ObservableComponentWrapper<Content extends java.awt.Component>

        extends
        ObservableImpl

        implements
        ComponentWrapper<Content>
{
    /**
     * Содержимое обертки. */
    private ComponentWrapperImpl<Content> wrapper;

    @Override
    public Content getContent() {
        return this.wrapper.content;
    }

    /**
     * @param content оборачиваемый компонент.
     */
    protected ObservableComponentWrapper(Content content) {
        wrapper = new ComponentWrapperImpl(content);
    }


}
