package com.maximilianmichels.paxos;

import akka.actor.ActorRef;

class Messages {

    static class Request {

        final long value;

        Request(long value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Request{" +
                    "value=" + value +
                    '}';
        }
    }


    static class Prepare {

        final long proposalNo;

        Prepare(long proposalNo) {
            this.proposalNo = proposalNo;
        }
    }


    static class Promise {

        final long proposalNo;
        final long previousProposalNo;
        final long previousValue;

        Promise(long proposalNo, long previousProposalNo, long previousValue) {
            this.proposalNo = proposalNo;
            this.previousProposalNo = previousProposalNo;
            this.previousValue = previousValue;
        }

        @Override
        public String toString() {
            return "Promise{" +
                    "proposalNo=" + proposalNo +
                    ", previousProposalNo=" + previousProposalNo +
                    ", previousValue=" + previousValue +
                    '}';
        }
    }

    static class Accept {

        final long proposalNo;
        final long value;
        final ActorRef client;

        Accept(long proposalNo, long value, ActorRef client) {
            this.proposalNo = proposalNo;
            this.value = value;
            this.client = client;
        }
    }


    static class Accepted extends Accept {

        public Accepted(long proposalNo, long value, ActorRef client) {
            super(proposalNo, value, client);
        }
    }


    static class Response {

        final long proposalNo;

        Response(long proposalNo) {
            this.proposalNo = proposalNo;
        }
    }
}
