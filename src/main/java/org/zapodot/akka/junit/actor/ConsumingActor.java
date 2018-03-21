package org.zapodot.akka.junit.actor;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * A simple consuming actor that stores all messages that was received
 */
public class ConsumingActor extends AbstractActor {

    public List<Object> messagesReceived = new LinkedList<>();
    private LoggingAdapter logger = Logging.getLogger(context().system(), self());

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchAny(this::onReceive)
                .build();
    }

    private void onReceive(final Object message) {
        logger.debug("Received message \"{}\". Adding it to the list of received messages");

        messagesReceived.add(message);

    }
}
