# Akka Test JUNIT 

Provides a JUnit rule for testing controlled testing of [Akka](http://akka.io) actors using [JUnit](http://junit.org).

Tested with Akka 2.3.3 and JUnit 4.11.

## Example
The ActorSystemRule may be used either as a @Rule (invoked around test methods) or as a @ClassRule (invoked before/after the TestClass)

### As a @Rule
```java
public class SimpleAkkaTest {

    @Rule
    public ActorSystemRule actorSystemRule = new ActorSystemRule(getClass().getSimpleName());

    @Test
    public void testRuleUsingASingleActor() throws Exception {
        final TestActorRef<SimpleActor> actorTestActorRef = TestActorRef.create(actorSystemRule.system(),
                                                                          Props.create(SimpleActor.class));
        final String message = "test";
        actorTestActorRef.tell(message, ActorRef.noSender());
        assertEquals(message, actorTestActorRef.underlyingActor().received.peek());

    }
}
```

### As a @ClassRule
```java
public class SimpleAkkaTest {

    // ClassRules must be instantiated as public static fields on the test class
    @ClassRule
    public static ActorSystemRule actorSystemRule = new ActorSystemRule(getClass().getSimpleName());

    @Test
    public void testRuleUsingASingleActor() throws Exception {
        final TestActorRef<SimpleActor> actorTestActorRef = TestActorRef.create(actorSystemRule.system(),
                                                                          Props.create(SimpleActor.class));
        final String message = "test";
        actorTestActorRef.tell(message, ActorRef.noSender());
        assertEquals(message, actorTestActorRef.underlyingActor().received.peek());

    }
    
    @Test
    public void testAnotherThing() throws Exception {
        final TestActorRef<SimpleActor> actorTestActorRef = TestActorRef.create(actorSystemRule.system(),
                                                                                  Props.create(SimpleActor.class));
        // Will use the same actorSystem instance as in the previous test. NB! Be aware of JUnit's ordering rules                                                                         
    }
}
```
A few words of caution when using @ClassRule:
* JUnit might not run the tests in the order you predicted. Check [JUnit execution ordering](//github.com/junit-team/junit/wiki/Test-execution-order).
* If you name your actors be aware the Akka expects actor names to be unique within the same actor system