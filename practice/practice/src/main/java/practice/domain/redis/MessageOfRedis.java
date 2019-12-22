package practice.domain.redis;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.format.annotation.DateTimeFormat;
import practice.domain.Message;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@RedisHash("message")
public class MessageOfRedis implements Serializable {

    @Id
    private String redisKey;
    private String requestIpAddress;
    private int content;

    @JsonFormat(pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS")
    @DateTimeFormat(pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createDate;

    public MessageOfRedis(String redisKey, String requestIpAddress, int content, LocalDateTime createDate) {
        this.redisKey = redisKey;
        this.requestIpAddress = requestIpAddress;
        this.content = content;
        this.createDate = createDate;
    }

    public MessageOfRedis(String requestIpAddress, int content, LocalDateTime createDate) {
        this.requestIpAddress = requestIpAddress;
        this.content = content;
        this.createDate = createDate;
    }

    public MessageOfRedis(String requestIpAddress, int content) {
        this("0", requestIpAddress, content, LocalDateTime.now());
    }

    public String getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }

    public String getRequestIpAddress() {
        return requestIpAddress;
    }

    public void setRequestIpAddress(String requestIpAddress) {
        this.requestIpAddress = requestIpAddress;
    }

    public MessageOfRedis() {
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
        MessageOfRedis messageOfRedis = (MessageOfRedis) o;
        return redisKey == messageOfRedis.redisKey &&
                content == messageOfRedis.content &&
                Objects.equals(requestIpAddress, messageOfRedis.requestIpAddress) &&
                Objects.equals(createDate, messageOfRedis.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(redisKey, requestIpAddress, content, createDate);
    }

    @Override
    public String toString() {
        return "Message{" +
                "redisKey=" + redisKey +
                ", requestIpAddress='" + requestIpAddress + '\'' +
                ", content=" + content +
                ", createDate=" + createDate +
                '}';
    }

    public Message createMessage() {
        return new Message(requestIpAddress, content, createDate);
    }
}
