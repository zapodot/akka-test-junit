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
    private long shutdownTimeoutInSeconds = ActorSystemRule.DEFAULT_SHUTDOWN_TIMEOUT;

    /**
     * Builds a plain instance using only default settings
     *
     * @return a new {@link ActorSystemRule} instance with default settings applied
     */
    public static ActorSystemRule buildWithDefaults() {
        return builder().build();
    }

    /**
     * Creates a new {@link ActorSystemRuleBuilder} allowing for customization of the underlying {@link akka.actor.ActorSystem}
     *
     * @return a new {@link ActorSystemRuleBuilder} instance
     */
    public static ActorSystemRuleBuilder builder() {
        return new ActorSystemRuleBuilder();
    }

    public static String defaultActorSystemName() {
        return IMPLICIT_NAME_PREFIX + UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Allows you to set a name for the underlying {@link akka.actor.ActorSystem} explicitly
     *
     * @param name the name to use
     * @return the same {@link ActorSystemRuleBuilder} with the given name applied
     */
    public ActorSystemRuleBuilder setName(final String name) {
        this.name = name;
        return this;
    }

    /**
     * Enables the TestEventListener. NB! only one logger will be configured at the time. Calling both this method and
     * {@link #enableEventLogging()} will result in only the the latest enable call taking effect.
     *
     * @return the same {@link ActorSystemRuleBuilder} instance with the EventTestListener enabled
     */
    public ActorSystemRuleBuilder enableEventTestListener() {
        setOrAddConfiguration(ConfigFactory.parseString(CONFIG_TEST_EVENT_LISTENER));
        return this;
    }

    /**
     * Enables the TestEventListener. NB! only one logger will be configured at the time. Calling both this method and
     * {@link #enableEventTestListener()} ()} will result in only the the latest enable call taking effect.
     *
     * @return the same {@link ActorSystemRuleBuilder} instance with event logging enabled
     */
    public ActorSystemRuleBuilder enableEventLogging() {
        setOrAddConfiguration(ConfigFactory.parseString(CONFIG_EVENT_LOGGING));
        return this;
    }

    /**
     * Enables debug logging of receive events for all actors, i.e sets "akka.actor.debug.receive = on"
     *
     * @return the same {@link ActorSystemRuleBuilder} instance with receive debug logging enabled
     */
    public ActorSystemRuleBuilder enableReceiveDebugLogging() {
        setOrAddConfiguration(ConfigFactory.parseString("akka.actor.debug.receive = on"));
        return this;
    }

    /**
     * Enables lifecycle debug logging by setting "akka.actor.debug.lifecycle = on"
     *
     * @return the same {@link ActorSystemRuleBuilder} instance with lifecycle debug logging enabled
     */
    public ActorSystemRuleBuilder enableLifecycleDebugLogging() {
        setOrAddConfiguration(ConfigFactory.parseString("akka.actor.debug.lifecycle = on"));
        return this;
    }

    /**
     * Sets the global log level for Akka to "debug", i.e sets "akka.loglevel= debug"
     *
     * @return the same {@link ActorSystemRuleBuilder} instance with loglevel for the event log set to DEBUG
     */
    public ActorSystemRuleBuilder setEventLogLevelDebug() {
        setOrAddConfiguration(ConfigFactory.parseString("akka.loglevel = debug"));
        return this;
    }


    /**
     * Enables the In-Memory Journal which is very useful for testing event sourced actors
     *
     * @return the same {@link ActorSystemRuleBuilder} instance with an in-memory journal enabled
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

    /**
     * Allows the developer to tune the shutdown timeout which is the maximum number of seconds to wait while shutting
     * down the ActorSystem. Will default to {@link ActorSystemRule#DEFAULT_SHUTDOWN_TIMEOUT} if this method is not called.
     *
     * @param shutdownTimeoutInSeconds
     * @return the same builder with the shutdown timeout set
     */
    public ActorSystemRuleBuilder withShutdownTimeoutInSeconds(final long shutdownTimeoutInSeconds) {
        this.shutdownTimeoutInSeconds = shutdownTimeoutInSeconds;
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

        return new ActorSystemRuleImpl(name, config, shutdownTimeoutInSeconds);
    }

}
