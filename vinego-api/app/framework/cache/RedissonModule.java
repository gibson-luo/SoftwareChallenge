package framework.cache;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import play.Logger;

/**
 * Created by gibson.luo on 2018-09-05.
 */
public class RedissonModule extends AbstractModule {

    @Override
    protected void configure() {
        Logger.info("Injecting redissonClient ...");
        bind(RedissonClient.class).toProvider(() -> Redisson.create()).in(Scopes.SINGLETON);
    }

}
