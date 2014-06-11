package org.zapodot.akka.junit;

import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ActorSystemRuleExplicitlyNamedTest {

    @Rule
    public ActorSystemRule actorSystemRule = new ActorSystemRule(getClass().getSimpleName());

    @Test
    public void testNamedActorSystemIsRunning() throws Exception {
        assertNotNull(actorSystemRule.system());
        assertEquals(getClass().getSimpleName(), actorSystemRule.system().name());
    }

}