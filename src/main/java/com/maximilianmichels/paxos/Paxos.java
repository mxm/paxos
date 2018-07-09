package com.maximilianmichels.paxos;

/**
 * A Paxos example implementation
 */
public class Paxos {
    public static void main(String[] args) throws InterruptedException {

        Actor dummy = new DummyActor();

        System.out.println(dummy.readMessage());

        dummy.sendMessage(new Message() {
            @Override
            public String toString() {
                return "bla";
            }
        }, dummy);

        System.out.println(dummy.readMessage());
        System.out.println(dummy.readMessage());
    }
}
