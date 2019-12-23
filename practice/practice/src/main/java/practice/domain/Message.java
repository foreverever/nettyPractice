package practice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.engine.jdbc.Size;
import org.springframework.format.annotation.DateTimeFormat;
import sun.security.util.Length;

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
    @Column(columnDefinition = "TIMESTAMP(3)")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS")
    @DateTimeFormat(pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS")
    @Column(columnDefinition = "TIMESTAMP(3)")
    private LocalDateTime endTime;

    public Message(String requestIpAddress, int content, LocalDateTime startTime, LocalDateTime endTime) {
        this.requestIpAddress = requestIpAddress;
        this.content = content;
        this.startTime = startTime;
        this.endTime = endTime;

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
        Message message = (Message) o;
        return id == message.id &&
                content == message.content &&
                Objects.equals(requestIpAddress, message.requestIpAddress) &&
                Objects.equals(startTime, message.startTime) &&
                Objects.equals(endTime, message.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, requestIpAddress, content, startTime, endTime);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", requestIpAddress='" + requestIpAddress + '\'' +
                ", content=" + content +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
