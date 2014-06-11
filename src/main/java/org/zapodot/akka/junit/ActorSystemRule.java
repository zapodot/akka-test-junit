package org.zapodot.akka.junit;

import akka.actor.ActorSystem;
import org.junit.rules.ExternalResource;

import java.util.UUID;

/**
 * An JUnit rule that starts an ActorSystem before running a test (or a class of test). May be used both as a @ClassRule and a @Rule
 *
 * @author sek
 */
public class ActorSystemRule extends ExternalResource {

    private ActorSystem actorSystem;
    private final String name;

    public ActorSystemRule(final String name) {
        this.name = name;
    }

    public ActorSystemRule() {

        this("test#" + UUID.randomUUID().toString());
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
