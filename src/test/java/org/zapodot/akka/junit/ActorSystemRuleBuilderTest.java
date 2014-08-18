package org.zapodot.akka.junit;

import akka.event.slf4j.Slf4jLogger;
import akka.testkit.TestEventListener;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
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
    public void testEmptyConfig() throws Exception {

        assertEquals(ConfigFactory.empty(), ActorSystemRule.builder().currentConfig());
    }

    @Test
    public void testAddExistingConfig() throws Exception {
        assertEquals(ConfigFactory.empty(), ActorSystemRule.builder().setConfig(ConfigFactory.empty()).currentConfig());
    }

    @Test
    public void testAddExistingConfigTwice() throws Exception {
        assertEquals(ConfigFactory.empty(),
                     ActorSystemRule.builder()
                                    .setConfig(ConfigFactory.empty())
                                    .setConfig(ConfigFactory.empty())
                                    .currentConfig());
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
        assertEquals("on",
                     ActorSystemRule.builder()
                                    .enableLifecycleDebugLogging()
                                    .currentConfig()
                                    .getString("akka.actor.debug.lifecycle"));

    }

    @Test
    public void testEnableLifecycleAndReceiveDebugLogging() throws Exception {
        final Config config = ActorSystemRule.builder()
                                             .enableLifecycleDebugLogging()
                                             .enableReceiveDebugLogging().currentConfig();
        assertEquals("on", config.getString("akka.actor.debug.lifecycle"));
        assertEquals("on", config.getString("akka.actor.debug.receive"));

    }

    @Test
    public void testEnableInMemoryJournal() throws Exception {

        final Config config = ActorSystemRule.builder().enableInmemoryJournal().currentConfig();
        assertEquals("in-memory-journal", config.getString("akka.persistence.journal.plugin"));
    }
}