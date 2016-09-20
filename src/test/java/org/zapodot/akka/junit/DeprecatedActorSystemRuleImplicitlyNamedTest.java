package org.zapodot.akka.junit;

import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
@SuppressWarnings("deprecation")
public class DeprecatedActorSystemRuleImplicitlyNamedTest {

    @Rule
    public ActorSystemRule actorSystemRule = new ActorSystemRuleImpl();

    @Test
    public void testActorSystemIsRunning() throws Exception {
        assertNotNull(actorSystemRule.system());
        assertTrue(actorSystemRule.system().name().startsWith(ActorSystemRuleBuilder.IMPLICIT_NAME_PREFIX));

    }
}