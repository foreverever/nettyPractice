package practice.service;

import practice.domain.Message;

import java.util.List;

public interface MessageMapper {
    //    void save(Message message);
    void save(String message);

    Message findById(long id);

    List<Message> findAll();
}
