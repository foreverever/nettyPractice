package practice.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Message {

    private long id;
    private String contents;

    public Message(String contents) {
        this.contents = contents;
    }

    public Message(long id, String contents) {
        this.id = id;
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

    public int size() {
        return contents.length();
    }

    public Map<String, Object> findHashMapValue() {
        Map<String, Object> map = new HashMap<>();
        //utils로 따로 빼서 상수 정의하던지 해야함
        map.put("id", this.id);
        map.put("contents", this.contents);
        return map;
    }
}
