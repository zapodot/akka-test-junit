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

import java.util.List;

/**
 * An JUnit rule that starts an ActorSystem before running a test (or a class of test).
 * May be used both as a @ClassRule and a @Rule
 *
 * @author zapodot at gmail dot com
 */
public class ActorSystemRule extends ExternalResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActorSystemRule.class);

    private final String name;
    private ActorSystem actorSystem;
    private Config config = null;
    private TestActorRef<ConsumingActor> unhandledMessagesConsumer;


    public ActorSystemRule(final String name,
                           final Config config) {
        this(name);
        this.config = config;
    }

    public ActorSystemRule(final String name) {
        this.name = name;
    }

    /**
     * @deprecated use the @{link ActorSystemBuilder instead}
     */
    @Deprecated
    public ActorSystemRule() {

        this(ActorSystemRuleBuilder.defaultActorSystemName());
    }

    public static ActorSystemRuleBuilder builder() {
        return ActorSystemRuleBuilder.builder();
    }

    public boolean isUnhandled(final Object letter) {
        for(Object message: unhandledMessagesUntyped()) {
            if(message instanceof UnhandledMessage && ((UnhandledMessage) message).getMessage().equals(letter)) {
                return true;
            }
        }
        return false;
    }

    public ActorSystem system() {
        return actorSystem;
    }

    @Override
    protected void before() throws Throwable {
        actorSystem = config == null ? ActorSystem.create(name) : ActorSystem.create(name, config);

        unhandledMessagesConsumer = TestActorRef.create(actorSystem, Props.create(ConsumingActor.class), "unhandledMessagesConsumer");
        actorSystem.eventStream().subscribe(unhandledMessagesConsumer, UnhandledMessage.class);

    }

    @Override
    protected void after() {

        JavaTestKit.shutdownActorSystem(actorSystem);
        actorSystem = null;
    }

    private List<? extends Object> unhandledMessagesUntyped() {
        return unhandledMessagesConsumer.underlyingActor().messagesReceived;
    }

}
