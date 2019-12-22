package practice.domain.redis;

import org.springframework.data.repository.CrudRepository;

public interface RedisRepository extends CrudRepository<MessageOfRedis, String> {
}
