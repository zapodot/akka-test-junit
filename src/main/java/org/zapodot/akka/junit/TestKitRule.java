package org.zapodot.akka.junit;

import akka.testkit.JavaTestKit;

/**
 * @author zapodot at gmail
 */
public interface TestKitRule extends ActorSystemRule {

    JavaTestKit testKit();
}
