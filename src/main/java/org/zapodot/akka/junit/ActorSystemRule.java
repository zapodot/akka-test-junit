package org.zapodot.akka.junit;

import akka.actor.ActorSystem;
import org.junit.rules.TestRule;

/**
 * @author zapodot at gmail dot com
 */
public interface ActorSystemRule extends TestRule {
    boolean isUnhandled(Object letter);

    ActorSystem system();
}
