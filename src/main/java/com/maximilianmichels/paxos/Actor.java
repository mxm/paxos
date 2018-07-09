package com.maximilianmichels.paxos;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
abstract class Actor extends Thread {

    private final ConcurrentHashMap<Actor, Deque<Message>> messageQueues;
    private int currIdx;

    private Object lock = new Object();

    protected Actor() {
        this.messageQueues = new ConcurrentHashMap<>();
    }

    protected Message readMessage() throws InterruptedException {
        while (true) {
            Actor[] actors = messageQueues.keySet().toArray(new Actor[]{});
            if (actors.length == 0) {
                wait();
                continue;
            }
            for (int i = 0; i < actors.length; i++) {
                Deque<Message> messages = messageQueues.get(actors[currIdx]);
                currIdx = (currIdx + 1) % actors.length;
                if (messages != null) {
                    synchronized (messages) {
                        if (!messages.isEmpty()) {
                            return messages.removeFirst();
                        }
                    }
                }
            }
            wait();
        }
    }

    private void receiveMessage(Message message, Actor actor) {
        messageQueues.compute(actor, (actor1, messages) -> {
            if (messages == null) {
                messages = new ArrayDeque<>();
            }
            messages.addLast(message);
            return messages;
        });
    }

    public void sendMessage(Message message, Actor actor) {
        actor.receiveMessage(message, this);
    }

}
