package org.zapodot.akka.junit;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.testkit.JavaTestKit;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ActorSystemUsingFluentTestEventListenerEnablement {

    @Rule
    public ActorSystemRule actorSystemRule = ActorSystemRuleImpl.builder()
                                                            .setName(getClass().getSimpleName())
                                                            .enableEventTestListener()
                                                            .enableReceiveDebugLogging()
                                                            .enableLifecycleDebugLogging()
                                                            .setEventLogLevelDebug()
                                                            .build();

    @Test
    public void testEventFilterEnabled() throws Exception {
        new JavaTestKit(actorSystemRule.system()) {{
            final ActorRef loggingActor = getSystem().actorOf(Props.create(LoggingActor.class), "loggingActor");
            Integer result = new EventFilter<Integer>(Logging.Info.class) {
                @Override
                protected Integer run() {

                    loggingActor.tell(LoggingActor.INFO_MESSAGE, ActorRef.noSender());
                    return 1;
                }
            }.from(loggingActor.path().toString()).occurrences(1).exec();
            assertEquals(1, result.intValue());
        }};

    }

    @Test
    public void testEventFilterEnabledDebug() throws Exception {
        new JavaTestKit(actorSystemRule.system()) {{
            final ActorRef loggingActor = getSystem().actorOf(Props.create(LoggingActor.class), "loggingActor");
            Integer result = new EventFilter<Integer>(Logging.Debug.class) {
                @Override
                protected Integer run() {

                    loggingActor.tell(LoggingActor.DEBUG_MESSAGE, ActorRef.noSender());
                    return 1;
                }
            }.from(loggingActor.path().toString()).occurrences(1).exec();
            assertEquals(1, result.intValue());
        }};

    }
}

class LoggingActor extends UntypedActor {

    public static final String INFO_MESSAGE = "info";
    public static final String DEBUG_MESSAGE = "debug";
    public static final String WARNING_MESSAGE = "warning";
    public static final String ERROR_MESSAGE = "error";
    private LoggingAdapter logger = Logging.getLogger(context().system(), self());

    @Override
    public void onReceive(final Object message) throws Exception {
        if (INFO_MESSAGE.equals(message)) {
            logger.info((String) message);
        } else if (DEBUG_MESSAGE.equals(message)) {
            logger.debug((String) message);
        } else if (WARNING_MESSAGE.equals(message)) {
            logger.warning((String) message);
        } else if (ERROR_MESSAGE.equals(message)) {
            logger.error((String) message);
        } else {
            unhandled(message);
        }
    }
}
