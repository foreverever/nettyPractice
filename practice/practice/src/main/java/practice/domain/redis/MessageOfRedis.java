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
    private LocalDateTime startTime;

    @JsonFormat(pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS")
    @DateTimeFormat(pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime endTime;

    public MessageOfRedis(String redisKey, String requestIpAddress, int content, LocalDateTime startTime) {
        this.redisKey = redisKey;
        this.requestIpAddress = requestIpAddress;
        this.content = content;
        this.startTime = startTime;
    }

    public MessageOfRedis(String requestIpAddress, int content, LocalDateTime startTime) {
        this.requestIpAddress = requestIpAddress;
        this.content = content;
        this.startTime = startTime;
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageOfRedis that = (MessageOfRedis) o;
        return content == that.content &&
                Objects.equals(redisKey, that.redisKey) &&
                Objects.equals(requestIpAddress, that.requestIpAddress) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(redisKey, requestIpAddress, content, startTime, endTime);
    }

    @Override
    public String toString() {
        return "MessageOfRedis{" +
                "redisKey='" + redisKey + '\'' +
                ", requestIpAddress='" + requestIpAddress + '\'' +
                ", content=" + content +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    public Message createMessage() {
        return new Message(requestIpAddress, content, startTime, endTime);
    }
}
