package org.zapodot.akka.junit;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.UUID;

/**
 * A simple builder that may be used to define ActorSystemRule instances to be used in tests
 *
 * @author zapodot at gmail dot com
 */
public class ActorSystemRuleBuilder {

    public static final String IMPLICIT_NAME_PREFIX = "test";
    public static final String CONFIG_EVENT_LOGGING = "akka {\n"
                                                      + "    loggers = [\"akka.event.slf4j.Slf4jLogger\"]\n"
                                                      + "    loglevel = DEBUG\n"
                                                      + "}";
    public static final String CONFIG_TEST_EVENT_LISTENER = "akka.loggers = [akka.testkit.TestEventListener]";

    private String name = defaultActorSystemName();
    private Config config = null;


    public static ActorSystemRuleBuilder builder() {
        return new ActorSystemRuleBuilder();
    }

    public static String defaultActorSystemName() {
        return IMPLICIT_NAME_PREFIX + UUID.randomUUID().toString().replace("-", "");
    }

    public ActorSystemRuleBuilder setName(final String name) {
        this.name = name;
        return this;
    }

    /**
     * Enables the TestEventListener. NB! only one logger will be configured at the time. Calling both this method and
     * {@link #enableEventLogging()} will result in only the the latest enable call taking effect.
     *
     * @return
     */
    public ActorSystemRuleBuilder enableEventTestListener() {
        setOrAddConfiguration(ConfigFactory.parseString(CONFIG_TEST_EVENT_LISTENER));
        return this;
    }

    /**
     * Enables the TestEventListener. NB! only one logger will be configured at the time. Calling both this method and
     * {@link #enableEventTestListener()} ()} will result in only the the latest enable call taking effect.
     *
     * @return
     */
    public ActorSystemRuleBuilder enableEventLogging() {
        setOrAddConfiguration(ConfigFactory.parseString(CONFIG_EVENT_LOGGING));
        return this;
    }

    /**
     * Enables debug logging of receive events for all actors, i.e sets "akka.actor.debug.receive = on"
     *
     * @return
     */
    public ActorSystemRuleBuilder enableReceiveDebugLogging() {
        setOrAddConfiguration(ConfigFactory.parseString("akka.actor.debug.receive = on"));
        return this;
    }

    /**
     * Enables lifecycle debug logging by setting "akka.actor.debug.lifecycle = on"
     *
     * @return
     */
    public ActorSystemRuleBuilder enableLifecycleDebugLogging() {
        setOrAddConfiguration(ConfigFactory.parseString("akka.actor.debug.lifecycle = on"));
        return this;
    }

    /**
     * Sets the global log level for Akka to "debug", i.e sets "akka.loglevel= debug"
     *
     * @return
     */
    public ActorSystemRuleBuilder setEventLogLevelDebug() {
        setOrAddConfiguration(ConfigFactory.parseString("akka.loglevel = debug"));
        return this;
    }


    /**
     * Enables the In-Memory Journal which is very useful for testing event sourced actors
     *
     * @return
     * @see <a href="https://github.com/michaelpisula/akka-journal-inmemory/">InMemory journal project page</a>
     */
    public ActorSystemRuleBuilder enableInmemoryJournal() {
        try {
            getClass().getClassLoader().loadClass("akka.persistence.journal.inmemory.InMemoryJournal");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(
                    "Could not find the InMemoryJournal class on the classpath, did you add the akka-persistence-inmemory dependency?",
                    e);
        }
        setOrAddConfiguration(ConfigFactory.parseString("akka.persistence.journal.plugin = \"in-memory-journal\""));
        return this;
    }

    private void setOrAddConfiguration(final Config config) {
        if (this.config == null) {
            this.config = config;
        } else {
            this.config = config.withFallback(this.config);
        }
    }

    public ActorSystemRuleBuilder setConfig(final Config config) {
        setOrAddConfiguration(config);
        return this;
    }

    public ActorSystemRuleBuilder setConfigFromString(final String configString) {
        return setConfig(ConfigFactory.parseString(configString));
    }

    Config currentConfig() {
        if (this.config == null) {
            return ConfigFactory.empty();
        } else {
            return ConfigFactory.empty().withFallback(config);
        }
    }

    public ActorSystemRule build() {

        return config == null ? new ActorSystemRule(name) : new ActorSystemRule(name,
                                                                                config);
    }
}
