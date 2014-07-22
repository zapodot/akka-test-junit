package org.zapodot.akka.junit;

import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ActorSystemProvidingConfigTest {

    private final static String SYSTEM_NAME = ActorSystemProvidingConfigTest.class.getSimpleName();

    @Rule
    public ActorSystemRule actorSystemRule = ActorSystemRule.builder().setName(SYSTEM_NAME).setConfigFromString(
            "akka {\n"
            + "    loggers = [\"akka.event.slf4j.Slf4jLogger\"]\n"
            + "    loglevel = DEBUG\n"
            + "}").build();

    @Test
    public void testConfigSet() throws Exception {
        assertEquals(SYSTEM_NAME, actorSystemRule.system().name());
        assertEquals("DEBUG", actorSystemRule.system().settings().config().getString("akka.loglevel"));
    }
}
