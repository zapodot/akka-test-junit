package org.zapodot.akka.junit;

import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ActorSystemRuleImplicitlyNamedTest {

    @Rule
    public ActorSystemRule actorSystemRule = new ActorSystemRule();

    @Test
    public void testActorSystemIsRunning() throws Exception {
        assertNotNull(actorSystemRule.system());
        assertTrue(actorSystemRule.system().name().startsWith(ActorSystemRule.IMPLICIT_NAME_PREFIX));

    }
}