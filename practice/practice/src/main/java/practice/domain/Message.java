package practice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Message implements Serializable {

    private String requestIpAddress;
    private int content;

    @JsonFormat(pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS")
    @DateTimeFormat(pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createDate;

    public Message(String requestIpAddress, int content, LocalDateTime createDate) {
        this.requestIpAddress = requestIpAddress;
        this.content = content;
        this.createDate = createDate;
    }

    public Message(int content) {
        this.content = content;
    }

    public String getRequestIpAddress() {
        return requestIpAddress;
    }

    public void setRequestIpAddress(String requestIpAddress) {
        this.requestIpAddress = requestIpAddress;
    }

    public Message() {
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
        return content == message.content &&
                Objects.equals(requestIpAddress, message.requestIpAddress) &&
                Objects.equals(createDate, message.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestIpAddress, content, createDate);
    }

    @Override
    public String toString() {
        return "Message{" +
                "requestIpAddress='" + requestIpAddress + '\'' +
                ", content=" + content +
                ", createDate=" + createDate +
                '}';
    }
}
