package framework.configuration;

import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.typesafe.config.ConfigValue;
import play.Configuration;
import play.Environment;
import play.Logger;

/**
 *
 * it loads the configuration values from application.conf
 *
 * Created by gibson.luo on 2018-09-06.
 */
public class ConfValueModule extends AbstractModule {

    private final Environment environment;

    private final Configuration configuration;

    private static final String prefix = "vinego.";

    public ConfValueModule(Environment environment, Configuration configuration) {
        this.environment = environment;
        this.configuration = configuration;
    }

    @Override
    protected void configure() {
        Logger.info("Injecting ConfValue ...");
        Set<Entry<String, ConfigValue>> applicationConfigEntry = configuration.entrySet().stream()
            .filter(item -> item.getKey().startsWith(prefix))
            .collect(Collectors.toSet());

        for (Entry<String, ConfigValue> item : applicationConfigEntry) {
            String key = item.getKey().replaceFirst(prefix, "");
            Object value = item.getValue().unwrapped();
            Logger.info("{} --- {}", key, value.toString());
            addConfigurationValueBinding(key, value);
        }
    }

    private <T> void addConfigurationValueBinding(String key, T value) {
        bind((Class<T>)value.getClass()).annotatedWith(Names.named(key)).toInstance(value);
    }
}
