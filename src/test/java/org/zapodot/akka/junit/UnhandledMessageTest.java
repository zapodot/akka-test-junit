package org.zapodot.akka.junit;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class UnhandledMessageTest {

    public static class UnhandledActor extends UntypedActor {
        @Override
        public void onReceive(final Object message) throws Exception {
            unhandled(message);
        }
    }

    @Rule
    public ActorSystemRule actorSystemRule = ActorSystemRule.builder().build();

    @Test
    public void testUnhandled() throws Exception {
        final ActorRef unhandledActor = actorSystemRule.system().actorOf(Props.create(UnhandledActor.class));
        final String letter = "test";
        unhandledActor.tell(letter, ActorRef.noSender());
        Thread.sleep(TimeUnit.SECONDS.toMillis(1L));
        assertTrue(actorSystemRule.isUnhandled(letter));

    }
}
