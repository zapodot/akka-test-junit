package org.zapodot.akka.junit;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import org.junit.rules.ExternalResource;

/**
 * An JUnit rule that starts an ActorSystem before running a test (or a class of test).
 * May be used both as a @ClassRule and a @Rule
 *
 * @author zapodot at gmail dot com
 */
public class ActorSystemRule extends ExternalResource {

    private final String name;
    private ActorSystem actorSystem;
    private Config config = null;

    public ActorSystemRule(final String name, final Config config) {
        this.name = name;
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

    public ActorSystem system() {
        return actorSystem;
    }

    @Override
    protected void before() throws Throwable {
        actorSystem = config == null ? ActorSystem.create(name) : ActorSystem.create(name, config);

    }

    @Override
    protected void after() {
        actorSystem.shutdown();
        actorSystem = null;
    }
}
