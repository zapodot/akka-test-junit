package org.zapodot.akka.junit;

import akka.actor.ActorSystem;
import org.junit.rules.ExternalResource;

import java.util.UUID;

/**
 * An JUnit rule that starts an ActorSystem before running a test (or a class of test).
 * May be used both as a @ClassRule and a @Rule
 *
 * @author zapodot at gmail dot com
 */
public class ActorSystemRule extends ExternalResource {

    public static final String IMPLICIT_NAME_PREFIX = "test";
    private final String name;
    private ActorSystem actorSystem;

    public ActorSystemRule(final String name) {
        this.name = name;
    }

    public ActorSystemRule() {

        this(IMPLICIT_NAME_PREFIX + UUID.randomUUID().toString().replace("-", ""));
    }

    public ActorSystem system() {
        return actorSystem;
    }

    @Override
    protected void before() throws Throwable {
        actorSystem = ActorSystem.create(name);

    }

    @Override
    protected void after() {
        actorSystem.shutdown();
        actorSystem = null;
    }
}
