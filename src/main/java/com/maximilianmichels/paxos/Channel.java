package com.maximilianmichels.paxos;

public interface Channel {

    void send(Message msg);

    Message receive();
}
