package com.maximilianmichels.paxos;

import java.util.concurrent.TimeoutException;

public class Main {

    public static void main(String[] args) throws TimeoutException, InterruptedException {

        int numProposers = 2;
        int numAcceptors = 4;
        int numLearners = 5;

        System.out.println("numProposers: " + numProposers);
        System.out.println("numAcceptors: " + numAcceptors);
        System.out.println("numLearners: " + numLearners);

        Paxos paxos = new Paxos(numProposers, numAcceptors, numLearners);
        paxos.startActors();

        paxos.doUpdate(42);
        paxos.doUpdate(23);
        paxos.doUpdate(5);

        paxos.shutdown();
    }
}
