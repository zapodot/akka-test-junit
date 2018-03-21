package org.zapodot.akka.junit;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;

@SuppressWarnings("deprecation")
public class DeprecatedActorSystemRuleImplicitlyNamedTest {

    private static final String SECOND_SYSTEM_NAME = "AnotherActorSystem";

    @Rule
    public ActorSystemRule actorSystemRule = new ActorSystemRuleImpl();

    @Rule
    public ActorSystemRule anotherActorSystemRule = ActorSystemRuleImpl.builder().setName(SECOND_SYSTEM_NAME).build();

    @Test
    public void testActorSystemIsRunning()  {
        assertNotNull(actorSystemRule.system());
        assertTrue(actorSystemRule.system().name().startsWith(ActorSystemRuleBuilder.IMPLICIT_NAME_PREFIX));

        assertThat(anotherActorSystemRule.system().name(), equalTo(SECOND_SYSTEM_NAME));

    }
}