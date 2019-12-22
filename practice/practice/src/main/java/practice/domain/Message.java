package practice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

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
    private LocalDateTime createTime;

    @JsonFormat(pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS")
    @DateTimeFormat(pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime processingTime;

    public Message(String requestIpAddress, int content, LocalDateTime createTime, LocalDateTime processingTime) {
        this.requestIpAddress = requestIpAddress;
        this.content = content;
        this.createTime = createTime;
        this.processingTime = processingTime;

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
        Message message = (Message) o;
        return id == message.id &&
                content == message.content &&
                Objects.equals(requestIpAddress, message.requestIpAddress) &&
                Objects.equals(createTime, message.createTime) &&
                Objects.equals(processingTime, message.processingTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, requestIpAddress, content, createTime, processingTime);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", requestIpAddress='" + requestIpAddress + '\'' +
                ", content=" + content +
                ", createDate=" + createTime +
                ", processingTime=" + processingTime +
                '}';
    }
}
