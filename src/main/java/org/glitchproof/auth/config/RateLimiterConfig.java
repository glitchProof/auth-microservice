package org.glitchproof.auth.config;

import io.lettuce.core.RedisClient;
import jakarta.annotation.PreDestroy;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.codec.ByteArrayCodec;
import org.springframework.context.annotation.Bean;
import io.lettuce.core.api.StatefulRedisConnection;
import io.github.bucket4j.redis.lettuce.Bucket4jLettuce;
import org.springframework.context.annotation.Configuration;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.time.Duration;


@Configuration
public class RateLimiterConfig {
    private StatefulRedisConnection<String, byte[]> connection;

    @Bean
    public LettuceBasedProxyManager<String> lettuceBasedProxyManager(LettuceConnectionFactory connectionFactory) {

        AbstractRedisClient nativeConnection = connectionFactory.getNativeClient();

        if(!(nativeConnection instanceof RedisClient)){
            throw new IllegalStateException("RedisClient is not instance of RedisClient");
        }

        connection = ((RedisClient) connectionFactory.getNativeClient())
                .connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));

        return Bucket4jLettuce
                .casBasedBuilder(connection)
                .expirationAfterWrite(
                        ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(
                                Duration.ofSeconds(10)
                        )
                )
                .build();
    }

    @PreDestroy
    public void cleanup() {
        if (connection != null) {
            connection.close();
        }
    }
}
