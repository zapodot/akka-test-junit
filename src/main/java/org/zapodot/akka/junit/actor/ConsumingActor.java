package org.zapodot.akka.junit.actor;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * A simple consuming actor that stores all messages that was received
 */
public class ConsumingActor extends UntypedActor {

    public List<Object> messagesReceived = new LinkedList<>();
    private LoggingAdapter logger = Logging.getLogger(context().system(), self());

    @Override
    public void onReceive(final Object message) throws Exception {
        logger.debug("Received message \"{}\". Adding it to the list of received messages");

        messagesReceived.add(message);

    }
}
