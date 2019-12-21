package practice.configuration.redis;

import org.springframework.data.repository.CrudRepository;
import practice.domain.Message;

public interface RedisRepository extends CrudRepository<Message, String> {
}
