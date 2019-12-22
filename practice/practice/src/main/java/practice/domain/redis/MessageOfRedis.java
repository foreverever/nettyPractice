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
    private LocalDateTime createTime;

    @JsonFormat(pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS")
    @DateTimeFormat(pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime processingTime;

    public MessageOfRedis(String redisKey, String requestIpAddress, int content, LocalDateTime createTime) {
        this.redisKey = redisKey;
        this.requestIpAddress = requestIpAddress;
        this.content = content;
        this.createTime = createTime;
    }

    public MessageOfRedis(String requestIpAddress, int content, LocalDateTime createTime) {
        this.requestIpAddress = requestIpAddress;
        this.content = content;
        this.createTime = createTime;
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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(LocalDateTime processingTime) {
        this.processingTime = processingTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageOfRedis that = (MessageOfRedis) o;
        return content == that.content &&
                Objects.equals(redisKey, that.redisKey) &&
                Objects.equals(requestIpAddress, that.requestIpAddress) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(processingTime, that.processingTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(redisKey, requestIpAddress, content, createTime, processingTime);
    }

    @Override
    public String toString() {
        return "MessageOfRedis{" +
                "redisKey='" + redisKey + '\'' +
                ", requestIpAddress='" + requestIpAddress + '\'' +
                ", content=" + content +
                ", createTime=" + createTime +
                ", processingTime=" + processingTime +
                '}';
    }

    public Message createMessage() {
        return new Message(requestIpAddress, content, createTime, processingTime);
    }
}
