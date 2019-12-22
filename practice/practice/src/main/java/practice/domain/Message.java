package practice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import practice.domain.redis.MessageOfRedis;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String requestIpAddress;
    @Column
    private int content;

    @JsonFormat(pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS")
    @DateTimeFormat(pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createDate;

    //객체지향 관점에서 getter를 지양
    public Message(MessageOfRedis messageOfRedis) {
        this.requestIpAddress = messageOfRedis.getRequestIpAddress();
        this.content = messageOfRedis.getContent();
        this.createDate = messageOfRedis.getCreateDate();
    }

    public Message() {
    }

    public Message(String requestIpAddress, int content, LocalDateTime createDate) {
        this.requestIpAddress = requestIpAddress;
        this.content = content;
        this.createDate = createDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRequestIpAddress() {
        return requestIpAddress;
    }

    public void setRequestIpAddress(String requestIpAddress) {
        this.requestIpAddress = requestIpAddress;
    }

    public int getContent() {
        return content;
    }

    public void setContent(int content) {
        this.content = content;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return id == message.id &&
                content == message.content &&
                Objects.equals(requestIpAddress, message.requestIpAddress) &&
                Objects.equals(createDate, message.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, requestIpAddress, content, createDate);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", requestIpAddress='" + requestIpAddress + '\'' +
                ", content=" + content +
                ", createDate=" + createDate +
                '}';
    }
}
