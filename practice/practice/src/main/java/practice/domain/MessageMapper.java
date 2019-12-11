package practice.domain;

import java.util.List;

public interface MessageMapper {
    void save(Message message);

    Message findById(long id);

    List<Message> findAll();
}
