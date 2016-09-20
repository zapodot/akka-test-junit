package org.zapodot.akka.junit;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.testkit.TestActorRef;
import org.junit.Rule;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.assertEquals;

public class ActorSystemRuleUsingTestkitTest {

    public static class SimpleActor extends UntypedActor {

        public final Queue<String> received;

        public SimpleActor() {
            received = new LinkedList<>();

        }

        @Override
        public void onReceive(final Object message) throws Exception {
            if(message != null && String.class.isAssignableFrom(message.getClass())) {
                received.add((String) message);
            } else {
                getContext().system().deadLetters().tell(message, self());
            }
        }
    }

    @Rule
    public ActorSystemRule actorSystemRule = new ActorSystemRuleImpl(getClass().getSimpleName());

    @Test
    public void testRuleUsingASingleActor() throws Exception {
        final TestActorRef<SimpleActor> actorTestActorRef = TestActorRef.create(actorSystemRule.system(),
                                                                          Props.create(SimpleActor.class));
        final String message = "test";
        actorTestActorRef.tell(message, ActorRef.noSender());
        assertEquals(message, actorTestActorRef.underlyingActor().received.peek());

    }
}