package framework.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.typesafe.config.Config;
import play.Environment;
import play.Logger;

/**
 * it loads the configuration values from application.conf
 *
 * Created by gibson.luo on 2018-09-06.
 */
public class ConfValueModule extends AbstractModule {

    private final Environment environment;

    private final Config configuration;

    private static final String prefix = "vinego.";

    public ConfValueModule(Environment environment, Config configuration) {
        this.environment = environment;
        this.configuration = configuration;
    }

    @Override
    protected void configure() {
        Logger.info("Injecting ConfValue ...");

        configuration.entrySet().stream()
            .filter(item -> item.getKey().startsWith(prefix))
            .forEach(item -> {
                String key = item.getKey().replaceFirst(prefix, "");
                Object value = item.getValue().unwrapped();
                Logger.info("{} --- {}", key, value.toString());
                addConfigurationValueBinding(key, value);
            });
    }

    private <T> void addConfigurationValueBinding(String key, T value) {
        bind((Class<T>)value.getClass()).annotatedWith(Names.named(key)).toInstance(value);
    }

    public Environment getEnvironment() {
        return environment;
    }
}
