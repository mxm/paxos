package com.maximilianmichels.paxos;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;

public class Client extends AbstractActor {

    private final ActorRef[] proposers;

    private ActorRef client;

    private long proposalNo;

    Client(ActorRef[] proposers) {
        this.proposers = proposers;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.Request.class,
                        (req) -> {
                            client = sender();
                            for (ActorRef proposer : proposers) {
                                proposer.tell(req, self());
                            }
                        }
                )
                .match(Messages.Response.class,
                        (response) -> {
                            if (response.proposalNo > proposalNo) {
                                client.tell(response, self());
                                proposalNo = response.proposalNo;
                            }
                        }
                )
                .build();
    }
}
