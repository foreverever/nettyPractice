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
    private long num;
    @Column
    private String ip;
    @Column
    private int content;

    @JsonFormat(pattern = "uuuu-MM-dd'T'HH:mm:ss")
    @DateTimeFormat(pattern = "uuuu-MM-dd'T'HH:mm:ss")
    @Column(columnDefinition = "TIMESTAMP", name = "startTime")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "uuuu-MM-dd'T'HH:mm:ss")
    @DateTimeFormat(pattern = "uuuu-MM-dd'T'HH:mm:ss")
    @Column(columnDefinition = "TIMESTAMP", name = "endTime")
    private LocalDateTime endTime;

    public Message(String ip, int content, LocalDateTime startTime, LocalDateTime endTime) {
        this.ip = ip;
        this.content = content;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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
        return num == message.num &&
                content == message.content &&
                Objects.equals(ip, message.ip) &&
                Objects.equals(startTime, message.startTime) &&
                Objects.equals(endTime, message.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(num, ip, content, startTime, endTime);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + num +
                ", requestIpAddress='" + ip + '\'' +
                ", content=" + content +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
