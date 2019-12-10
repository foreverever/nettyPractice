package practice.domain;

import org.springframework.data.annotation.Id;

import java.util.Objects;

public class Message {

    @Id
    private long id;
    private String contents;

    public Message(String contents) {
        this.contents = contents;
    }

    public Message() {
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(contents, message.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contents);
    }

    @Override
    public String toString() {
        return "Message{" +
                "contents='" + contents + '\'' +
                '}';
    }
}
