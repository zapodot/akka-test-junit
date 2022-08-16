# Akka Test JUNIT 
__Not maintained any more__

[![Flattr this git repo](http://api.flattr.com/button/flattr-badge-large.png)](https://flattr.com/submit/auto?user_id=zapodot&url=https://github.com/zapodot/akka-test-junit&title=akka-test-junit&language=&tags=github&category=software)
[![Build Status](https://travis-ci.org/zapodot/akka-test-junit.svg?branch=master)](https://travis-ci.org/zapodot/akka-test-junit)
[![Coverage Status](https://img.shields.io/coveralls/zapodot/akka-test-junit.svg)](https://coveralls.io/r/zapodot/akka-test-junit)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.zapodot/akka-test-junit/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.zapodot/akka-test-junit)
[![License: Apache 2](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Libraries.io for GitHub](https://img.shields.io/librariesio/github/zapodot/akka-test-junit.svg)](https://libraries.io/github/zapodot/akka-test-junit)

Provides a JUnit rule for controlled testing of [Akka](http://akka.io) actors using [JUnit](http://junit.org).

Tested with Akka 2.5.11 and JUnit 4.12. This tool, including the source code is made available under an Apache 2.0 license.

## Add dependency
As this library is distributed through the Sonatype OSS repository, it should be easy to add it to your project

### Maven
```xml
<dependency>
    <groupId>org.zapodot</groupId>
    <artifactId>akka-test-junit</artifactId>
    <version>2.0.0</version>
    <scope>test</scope>
</dependency>
```

### SBT
```scala
libraryDependencies += "org.zapodot" % "akka-test-junit" % "2.0.0"
```

### Gradle
```groovy
compile 'org.zapodot:akka-test-junit:2.0.0'
```


## Example
The ActorSystemRule may be used either as a @Rule (invoked around test methods) or as a @ClassRule (invoked before/after the TestClass)

### As a @Rule
```java
public class SimpleAkkaTest {

    @Rule
    public ActorSystemRule actorSystemRule = new ActorSystemRuleBuilder().setName(getClass().getSimpleName()).build();

    @Test
    public void testRuleUsingASingleActor()  {
        final TestActorRef<SimpleActor> actorTestActorRef = TestActorRef.create(actorSystemRule.system(),
                                                                          Props.create(SimpleActor.class));
        final String message = "test";
        actorTestActorRef.tell(message, ActorRef.noSender());
        assertEquals(message, actorTestActorRef.underlyingActor().received.peek());
        
        // Use the testKit() to get an instance of JavaTestKit directly
        // In this example EchoActor simply sends the message to the designated sender actor
        final JavaTestKit testKit = actorSystemRule.testKit();
        final Props simpleActorProps = Props.create(EchoActor.class);
        final ActorRef simpleActor = actorSystemRule.system().actorOf(simpleActorProps);
        simpleActor.tell("A great message", testKit.getTestActor()); // Use testActor as sender
        testKit.expectMsgEquals(FiniteDuration.apply(1L, TimeUnit.SECONDS), "A great message");
           

    }

        
}
```

### As a @ClassRule
```java
public class SimpleAkkaTest {

    // ClassRules must be instantiated as public static fields on the test class
    @ClassRule
    public static ActorSystemRule actorSystemRule = ActorSystemRule.builder().setName(getClass().getSimpleName()).build();

    @Test
    public void testRuleUsingASingleActor() {
        final TestActorRef<SimpleActor> actorTestActorRef = TestActorRef.create(actorSystemRule.system(),
                                                                          Props.create(SimpleActor.class));
        final String message = "test";
        actorTestActorRef.tell(message, ActorRef.noSender());
        assertEquals(message, actorTestActorRef.underlyingActor().received.peek());

    }
    
    @Test
    public void testAnotherThing() {
        final TestActorRef<SimpleActor> actorTestActorRef = TestActorRef.create(actorSystemRule.system(),
                                                                                  Props.create(SimpleActor.class));
        // Will use the same actorSystem instance as in the previous test. NB! Be aware of JUnit's ordering rules                                                                         
    }
}
```
### With event logging enabled (version >= 1.1.0)
```java
@Rule
public ActorSystemRule actorSystemWithEventLoggerEnabled = ActorSystemRule.builder()
                                                        .enableEventLogging()
                                                        .setName(getClass().getSimpleName())
                                                        .build();
```

### With event test listener enabled(version >= 1.1.0)
```java
@Rule
public ActorSystemRule actorSystemRule = ActorSystemRule.builder()
                                                       .setName(getClass().getSimpleName())
                                                       .setConfigFromString("akka.loglevel = DEBUG")
                                                       .enableEventTestListener()
                                                       .build();

@Test
public void testEventFilterEnabled() throws Exception {
   new JavaTestKit(actorSystemRule.system()) {{
       final ActorRef loggingActor = getSystem().actorOf(Props.create(LoggingActor.class), "loggingActor");
       Integer result = new EventFilter<Integer>(Logging.Info.class) {
           @Override
           protected Integer run() {

               loggingActor.tell(LoggingActor.INFO_MESSAGE, ActorRef.noSender());
               return 1;
           }
       }.from(loggingActor.path().toString()).occurrences(1).exec();
       assertEquals(1, result.intValue());
   }};

}
```

### With custom configuration (version >= 1.1.0)
```java
public class SimpleAkkaTest {

    @Rule
    public ActorSystemRule actorSystemRule = ActorSystemRule.builder().setName("test-system").setConfigFromString(
            "akka {\n"
            + "    loggers = [\"akka.event.slf4j.Slf4jLogger\"]\n"
            + "    loglevel = DEBUG\n"
            + "}").build();

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
## Changelog
* Version 2.0.0: Akka 2.5.0 support - support for previous versions are dropped. Java baseline is 1.8
* Version 1.3.0: Verifies that the ActorSystem is indeed properly shutdown. Allows a shutdown timeout to be set
* Version 1.2.0: Add a new testKit() method that to create a JavaTestKit
* Version 1.1.1: Fixed a bug in ActorSystemRuleBuilder that replaced the configuration instead of added to it
* Version 1.1: Added the ActorSystemRuleBuilder and the ability to specify configuration and/or enable logging for the actor system
* Version 1.0: First release

## Limitations
A few words of caution when using @ClassRule:
* JUnit might not run the tests in the order you predicted. Check [JUnit execution ordering](//github.com/junit-team/junit/wiki/Test-execution-order).
* If you name your actors be aware the Akka expects actor names to be unique within the same actor system
