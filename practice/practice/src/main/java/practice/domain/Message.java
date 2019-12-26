package practice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@NoArgsConstructor
public class Message implements Serializable {
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

    @Builder
    private Message(long num, String ip, int content, LocalDateTime startTime, LocalDateTime endTime) {
        this.num = num;
        this.ip = ip;
        this.content = content;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Deprecated
    public Message(String ip, int content, LocalDateTime startTime) {
        this.ip = ip;
        this.content = content;
        this.startTime = startTime;
    }

    @Deprecated
    public Message (String ip, int content) {
        this(0L, ip, content, LocalDateTime.now(), LocalDateTime.now());
    }

    /*정적 팩토리 메소드를 사용하면 객체 생성시에 의미를 부여할 수 있어요*/
    public static Message makeData(String ip, int content, LocalDateTime startTime) {
        return Message.builder()
                .ip(ip)
                .content(content)
                .startTime(startTime)
                .build();
    }

    /*정적 팩토리 메소드를 사용하면 객체 생성시에 의미를 부여할 수 있어요 test에서만 사용하는 생성자*/
    public static Message testData(String ip, int content) {
        return Message.builder()
                .ip(ip)
                .content(content)
                .build();
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

    public String calcSecond() {
        return Integer.toString(endTime.getSecond() % 10);
    }
}
