package com.swing_boot.reactive.registry;

/**
 * Обретка для объектов, имещих графическое представление, которое
 * может быть отображено на экране и c которой может взаимодействовать пользователь.
 * @param <Content> тип объекта, который будет служить содержимым обертки.
 */
public interface ComponentWrapper<Content extends java.awt.Component> {

    /**
     * Получение сожержимого обертки.
     * @return обернутый компонент.
     */
    Content getContent();

}
