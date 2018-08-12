package com.swing_boot.reactive.registry.pojo;

import com.swing_boot.reactive.Events;

import java.util.*;

/**
 * Логика подписки на определенные типы событий.
 */
public class Subscriptions {

    private static final
    HashMap<Events, HashMap<
            Observable,
                        Set<Observer>>> REGISTRY = new HashMap<>();

    /**
     * Инстанция, которая производит регистрацию типов событий и подписку на них.
     */
    private static final Subscriptions INSTANCE = new Subscriptions();

    /**
     * @param observable
     * @param event тип события, регистрация которого производится.
     */
    public static void register(Observable observable, Events event) {
        synchronized (REGISTRY) {
            HashMap<Observable, Set<Observer>> eventSubscriptions = REGISTRY.get(event);
            if (eventSubscriptions == null || eventSubscriptions.isEmpty()) {
                final HashMap<Observable, Set<Observer>> observableSubscriptions = new HashMap<>();
                if (observableSubscriptions.isEmpty()) {
                    observableSubscriptions.put(observable, new HashSet<Observer>());
                    REGISTRY.put(event, observableSubscriptions);
                }
            } else {
                eventSubscriptions.put(observable, new HashSet<Observer>());
            }
        }
    }

    /**
     * @param message отсылаемое сообщение.
     * @param event   тип события, с которым отсылается сообщение.
     */
    public static void send(Observable observable, Object message, Events event) {
        synchronized (REGISTRY) {
            final Set<Map.Entry<Observable, Set<Observer>>> subscriptionsEntries = getSubscriptionsByEvent(event);
            for (Map.Entry<Observable, Set<Observer>> subscription : subscriptionsEntries) {
                final Observable registeredObservable = subscription.getKey();
                if (registeredObservable.equals(observable)) {
                    final Set<Observer> value = subscription.getValue();
                    for (Observer observer : value) {
                        observer.update(message, event);
                    }
                }
            }
        }
    }

    /**
     * @param event    тип события, на который происходит подписка.
     * @param observer подписчик.
     */
    public static void subscribe(Events event, Observer observer) {
        synchronized (REGISTRY) {
            final Set<Map.Entry<Observable, Set<Observer>>> subscriptionsEntries = Subscriptions.getSubscriptionsByEvent(event);
            for (Map.Entry<Observable, Set<Observer>> subscription : subscriptionsEntries) {
                final Set<Observer> subscribedObservers = subscription.getValue();
                subscribedObservers.add(observer);
            }
        }
    }

    /**
     * @param event    тип события, на который происходит подписка.
     * @param observer подписчик.
     */
    public static void subscribe(Observable observable, Events event, Observer observer) {
        synchronized (REGISTRY) {
            final Set<Map.Entry<Observable, Set<Observer>>> subscriptionsEntries = Subscriptions.getSubscriptionsByEvent(event);
            for (Map.Entry<Observable, Set<Observer>> subscription : subscriptionsEntries) {
                final Observable registeredObservable = subscription.getKey();
                if (registeredObservable.equals(observable)) {
                    final Set<Observer> subscribedObservers = subscription.getValue();
                    subscribedObservers.add(observer);
                }
            }
        }
    }

    /**
     * Отмена подписок на конкретное событие.
     *
     * @param event событие.
     */
    public static void unsubscribe(Events event) {
        synchronized (REGISTRY) {
            HashMap<Observable, Set<Observer>> subscriptions = REGISTRY.get(event);
            final Iterator<Map.Entry<Observable, Set<Observer>>> iterator = subscriptions.entrySet().iterator();
            while(iterator.hasNext()) {
                iterator.remove();
            }

            final Iterator<Map.Entry<Events, HashMap<Observable, Set<Observer>>>> registryIterator = REGISTRY.entrySet().iterator();
            while (registryIterator.hasNext()) {
                final Map.Entry<Events, HashMap<Observable, Set<Observer>>> next = registryIterator.next();
                if (next.getValue().equals(subscriptions)) {
                    iterator.remove();
                }
            }
        }
    }

    /**
     * Отписка от всех кто рассылает сообщения определенного подписчика от определенного события.
     * @param event    событие.
     * @param observer подписчик.
     */
    public static void unsubscribe(Events event, Observer observer) {
        synchronized (REGISTRY) {
            final Set<Map.Entry<Observable, Set<Observer>>> subscriptionsEntries = Subscriptions.getSubscriptionsByEvent(event);
            final Iterator<Map.Entry<Observable, Set<Observer>>> iterator = subscriptionsEntries.iterator();
            while (iterator.hasNext()) {
                final Map.Entry<Observable, Set<Observer>> next = iterator.next();
                final Set<Observer> subscribedObservers = next.getValue();
                subscribedObservers.remove(observer);
            }
        }
    }

    /**
     * Отписка определенного подписчика от определенного события определенного отправителя.
     * @param observer подписчик.
     * @param event    событие.
     * @param observable отправитель сообщений.
     */
    public static void unsubscribe(Observer observer, Events event, Observable observable) {
        synchronized (REGISTRY) {
            HashMap<Observable, Set<Observer>> subscriptions = REGISTRY.get(event);
            for (Observable registeredObservable : subscriptions.keySet()) {
                if(registeredObservable.equals(observable)) {
                    Subscriptions.unsubscribe(event, observer);
                }
            }
        }
    }

    public static void delete(Events event, Observable observable) {
        final Set<Map.Entry<Observable, Set<Observer>>> subscriptionsEntries = Subscriptions.getSubscriptionsByEvent(event);
        final Iterator<Map.Entry<Observable, Set<Observer>>> iterator = subscriptionsEntries.iterator();
        while (iterator.hasNext()) {
            final Map.Entry<Observable, Set<Observer>> next = iterator.next();
            final Observable registeredObservable = next.getKey();
            if (registeredObservable.equals(observable)) {
                iterator.remove();
            }
        }
    }

    private static Set<Map.Entry<Observable, Set<Observer>>> getSubscriptionsByEvent(Events event) {
        HashMap<Observable, Set<Observer>> subscriptions = REGISTRY.get(event);
        return subscriptions.entrySet();
    }
}
