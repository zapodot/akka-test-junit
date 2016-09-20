package org.zapodot.akka.junit;

import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ActorSystemMultipleLoggersEnabled {

    @Rule
    public ActorSystemRule actorSystemRule = ActorSystemRuleImpl.builder()
                                                            .setName(getClass().getSimpleName())
                                                            .enableEventLogging()
                                                            .enableEventTestListener()
                                                            .build();

    @Test
    public void testHasBoth() throws Exception {
        assertEquals(1, actorSystemRule.system().settings().Loggers().size());

    }
}
