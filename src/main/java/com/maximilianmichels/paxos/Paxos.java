package com.maximilianmichels.paxos;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.Objects;
import java.util.concurrent.TimeoutException;

/**
 * A Paxos example implementation
 */
public class Paxos {

    private final int numProposers;
    private final int numAcceptors;
    private final int numLearners;

    private ActorSystem actorSystem;
    private ActorRef client;

    Paxos(int numProposers, int numAcceptors, int numLearners) {
        this.numProposers = numProposers;
        this.numAcceptors = numAcceptors;
        this.numLearners = numLearners;
    }

    void startActors() {

        actorSystem = ActorSystem.create("paxos");

        final ActorRef[] learners = new ActorRef[numLearners];
        for (int i = 0; i < numLearners; i++) {
            int idx = i;
            learners[i] = actorSystem.actorOf(
                    Props.create(Learner.class, () -> new Learner(idx)), "learner" + i);
        }

        final ActorRef[] acceptors = new ActorRef[numAcceptors];
        for (int i = 0; i < numAcceptors; i++) {
            int idx = i;
            acceptors[i] = actorSystem.actorOf(
                    Props.create(Acceptor.class, () -> new Acceptor(idx, learners)), "acceptor" + i);
        }

        final ActorRef[] proposers = new ActorRef[numProposers];
        for (int i = 0; i < numProposers; i++) {
            int idx = i;
            proposers[i] = actorSystem.actorOf(Props.create(Proposer.class, () -> new Proposer(idx, acceptors)), "proposer" + i);
        }

        client = actorSystem.actorOf(Props.create(Client.class, () -> new Client(proposers)), "client");
    }

    void doUpdate(long value) throws TimeoutException, InterruptedException {
        Objects.requireNonNull(actorSystem, "Actor system not yet started.");
        Future<Object> ask = Patterns.ask(client, new Messages.Request(value), 15000);
        Await.ready(ask, Duration.create("15 seconds"));
    }


    void shutdown() {
        actorSystem.terminate();
    }
}
