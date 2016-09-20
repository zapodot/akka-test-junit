package org.zapodot.akka.junit;

import akka.testkit.JavaTestKit;
import com.typesafe.config.Config;

/**
 * @author zapodot at gmail dot com
 */
public class TestKitRuleImpl extends ActorSystemRuleImpl implements TestKitRule {

    private JavaTestKit testKit;

    public TestKitRuleImpl(final String name, final Config config) {
        super(name, config);
    }

    @Override
    public JavaTestKit testKit() {
        return testKit;
    }

    @Override
    protected void before() throws Throwable {
        super.before();
        testKit = new JavaTestKit(system());
    }

    @Override
    protected void after() {
        testKit.shutdown(system());
        super.after();
    }
}
