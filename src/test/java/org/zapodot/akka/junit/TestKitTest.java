package org.zapodot.akka.junit;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.testkit.JavaTestKit;
import org.junit.Rule;
import org.junit.Test;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

public class TestKitTest {

    public static class SimpleActor extends UntypedActor {

        public SimpleActor() {

        }

        @Override
        public void onReceive(final Object message) throws Exception {
            if (message != null && String.class.isAssignableFrom(message.getClass())) {
                // If the message is a String, return it
                getContext().sender().tell(message, getSelf());
            } else {
                // All other messages are unhandled
                unhandled(message);
            }
        }
    }

    @Rule
    public ActorSystemRule testKitRule = ActorSystemRuleBuilder.builder().setName(getClass().getSimpleName()).build();

    @Test
    public void testRule() throws Exception {
        final JavaTestKit javaTestKit = testKitRule.testKit();
        final ActorRef testActor = javaTestKit.getTestActor();

        final String message = "test";
        testActor.tell(message, ActorRef.noSender());
        javaTestKit.expectMsgEquals(message);

    }

    @Test
    public void testRuleAsync() throws Exception {
        final Props simpleActorProps = Props.create(SimpleActor.class);
        final ActorRef simpleActorRef = testKitRule.system().actorOf(simpleActorProps);
        final JavaTestKit testProbe = testKitRule.testKit();

        final String msg = "Hello AKKA";
        simpleActorRef.tell(msg, testProbe.getTestActor());
        testProbe.expectMsgEquals(FiniteDuration.apply(3L, TimeUnit.SECONDS), msg);

        final long numberMsg = 28938L;
        simpleActorRef.tell(numberMsg, testProbe.getTestActor());
        testKitRule.isUnhandled(numberMsg);

    }
}