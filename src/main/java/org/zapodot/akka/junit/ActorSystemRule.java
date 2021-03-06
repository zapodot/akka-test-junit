package org.zapodot.akka.junit;

import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import org.junit.rules.TestRule;

/**
 * General contract for a JUnit rule that starts an ActorSystem before running a test (or a class of test).
 * May be used both as a @ClassRule and a @Rule
 *
 * @author zapodot at gmail dot com
 */
public interface ActorSystemRule extends TestRule {

    long DEFAULT_SHUTDOWN_TIMEOUT = 2L;

    /**
     * Assertion that is used to check whether a given message was unhandled
     *
     * @param letter the message
     * @return boolean indicating whether the message was unhandled
     */
    boolean isUnhandled(Object letter);

    /**
     * Provides access to the current shutdown timeout setting, which defaults to {@link #DEFAULT_SHUTDOWN_TIMEOUT}
     * if not explicitly set
     *
     * @return the number of seconds that the test runner will wait when shutting down the ActorSystem
     */
    long getShutdownTimeoutSeconds();

    /**
     * Provides access to the {@link ActorSystem} that was instantiated before the test was run
     *
     * @return the currently running {@link ActorSystem}
     */
    ActorSystem system();

    /**
     * Convienience method that provides easy access to a {@link TestKit} instance.
     *
     * @return the {@link TestKit} instance that was created before the test ran
     */
    TestKit testKit();

}
