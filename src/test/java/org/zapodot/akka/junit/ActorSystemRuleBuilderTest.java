package org.zapodot.akka.junit;

import akka.event.slf4j.Slf4jLogger;
import akka.testkit.TestEventListener;
import com.typesafe.config.Config;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ActorSystemRuleBuilderTest {

    @Test
    public void testEnableEventListener() throws Exception {
        final ActorSystemRuleBuilder actorSystemRuleBuilder = ActorSystemRule.builder().enableEventTestListener();
        final Config config = actorSystemRuleBuilder.currentConfig();

        assertEquals(TestEventListener.class.getName(), config.getStringList("akka.loggers").get(0));

    }

    @Test
    public void testEnableEventLogging() throws Exception {
        final ActorSystemRuleBuilder actorSystemRuleBuilder = ActorSystemRule.builder().enableEventLogging();
        final Config config = actorSystemRuleBuilder.currentConfig();
        assertEquals(Slf4jLogger.class.getName(), config.getStringList("akka.loggers").get(0));
        assertEquals("DEBUG", config.getString("akka.loglevel"));
    }

    @Test
    public void testEnableReceiveDebugLogging() throws Exception {
        assertEquals("on", ActorSystemRule.builder().enableReceiveDebugLogging().currentConfig().getString(
                "akka.actor.debug.receive"));
    }

    @Test
    public void testEnableLifecycleDebugLogging() throws Exception {
        assertEquals("on", ActorSystemRule.builder().enableLifecycleDebugLogging().currentConfig().getString("akka.actor.debug.lifecycle"));

    }
}