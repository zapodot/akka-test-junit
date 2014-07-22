package org.zapodot.akka.junit;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.UUID;

/**
 * A simple builder that may be used to define ActorSystemRule instances to be used in tests
 *
 * @author zapodot at gmail dot com
 */
public class ActorSystemRuleBuilder {

    public static final String IMPLICIT_NAME_PREFIX = "test";
    private String name = defaultActorSystemName();
    private Config config = null;


    public static ActorSystemRuleBuilder builder() {
        return new ActorSystemRuleBuilder();
    }

    public ActorSystemRuleBuilder setName(final String name) {
        this.name = name;
        return this;
    }

    public ActorSystemRuleBuilder setConfig(final Config config) {
        this.config = config;
        return this;
    }

    public ActorSystemRuleBuilder setConfigFromString(final String configString) {
        return setConfig(ConfigFactory.parseString(configString));
    }

    public ActorSystemRule build() {

        return config == null ? new ActorSystemRule(name) : new ActorSystemRule(name, config);
    }

    public static String defaultActorSystemName() {
        return IMPLICIT_NAME_PREFIX + UUID.randomUUID().toString().replace("-", "");
    }
}
