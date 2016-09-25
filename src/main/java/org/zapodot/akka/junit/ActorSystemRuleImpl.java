package org.zapodot.akka.junit;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UnhandledMessage;
import akka.testkit.JavaTestKit;
import akka.testkit.TestActorRef;
import com.typesafe.config.Config;
import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zapodot.akka.junit.actor.ConsumingActor;
import scala.concurrent.duration.Duration;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * An JUnit rule that starts an ActorSystem before running a test (or a class of test).
 * May be used both as a @ClassRule and a @Rule
 *
 * @author zapodot at gmail dot com
 */
public class ActorSystemRuleImpl extends ExternalResource implements ActorSystemRule {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActorSystemRuleImpl.class);

    protected final String name;
    private ActorSystem actorSystem;
    private Config config = null;
    private TestActorRef<ConsumingActor> unhandledMessagesConsumer;
    private JavaTestKit javaTestKit;
    private final long shutdownTimeoutSeconds;


    public ActorSystemRuleImpl(final String name,
                               final Config config,
                               final long shutdownTimeoutSeconds) {
        this.name = name;
        this.config = config;
        this.shutdownTimeoutSeconds = shutdownTimeoutSeconds;
    }

    public ActorSystemRuleImpl(final String name,
                               final Config config) {
        this(name, config, DEFAULT_SHUTDOWN_TIMEOUT);
    }

    public ActorSystemRuleImpl(final String name) {
        this(name, null);
    }

    /**
     * @deprecated use the @{link ActorSystemBuilder instead}
     */
    @Deprecated
    public ActorSystemRuleImpl() {

        this(ActorSystemRuleBuilder.defaultActorSystemName());
    }

    /**
     * @return a new {@link ActorSystemRuleBuilder} instance
     * @deprecated use {@link ActorSystemRuleBuilder#builder()} instead
     */
    @Deprecated
    public static ActorSystemRuleBuilder builder() {
        return ActorSystemRuleBuilder.builder();
    }

    @Override
    public boolean isUnhandled(final Object letter) {
        for (Object message : unhandledMessagesUntyped()) {
            if (message instanceof UnhandledMessage && ((UnhandledMessage) message).getMessage().equals(letter)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public long getShutdownTimeoutSeconds() {
        return shutdownTimeoutSeconds;
    }

    @Override
    public ActorSystem system() {
        return actorSystem;
    }

    @Override
    public JavaTestKit testKit() {
        return javaTestKit;
    }

    @Override
    protected void before() throws Throwable {
        LOGGER.debug("Instantiating ActorSystem \"{}\"", name);
        actorSystem = config == null ? ActorSystem.create(name) : ActorSystem.create(name, config);

        unhandledMessagesConsumer = TestActorRef.create(actorSystem, Props.create(ConsumingActor.class), "unhandledMessagesConsumer");
        actorSystem.eventStream().subscribe(unhandledMessagesConsumer, UnhandledMessage.class);
        javaTestKit = new JavaTestKit(actorSystem);

    }

    @Override
    protected void after() {

        javaTestKit = null;
        if (!actorSystem.isTerminated()) {
            LOGGER.debug("Shutting down ActorSystem \"{}\"", name);
            JavaTestKit.shutdownActorSystem(actorSystem, Duration.apply(shutdownTimeoutSeconds, TimeUnit.SECONDS), true);
        }
        actorSystem = null;
    }

    private List<? extends Object> unhandledMessagesUntyped() {
        return unhandledMessagesConsumer.underlyingActor().messagesReceived;
    }

}
