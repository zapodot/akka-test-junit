package org.zapodot.akka.junit;

import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.slf4j.Slf4jLogger;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ActorSystemUsingFluentEventLogEnablement {

    @Rule
    public ActorSystemRule actorSystemWithEventLoggerEnabled = ActorSystemRuleImpl.builder()
                                                            .enableEventLogging()
                                                            .setName(getClass().getSimpleName())
                                                            .build();

    @Rule
    public ActorSystemRule actorSystemWithoutEventLoggerEnabled = ActorSystemRuleImpl.builder()
                                                            .setName(getClass().getSimpleName())
                                                            .build();



    @Test
    public void testLoggingEnabled() throws Exception {
        final ActorSystem system = actorSystemWithEventLoggerEnabled.system();

        assertEquals("DEBUG", system.settings().LogLevel());
        assertEquals(Slf4jLogger.class.getName(), system.settings().Loggers().head());
    }

    @Test
    public void testLoggingNotEnabled() throws Exception {
        assertEquals(Logging.DefaultLogger.class.getName(), actorSystemWithoutEventLoggerEnabled.system().settings().Loggers().head());

    }
}
