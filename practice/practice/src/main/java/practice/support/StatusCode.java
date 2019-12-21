package practice.support;

import org.springframework.util.StringUtils;

public enum StatusCode {

    OACK, NACK, DATA, FAKE;

    public static StatusCode of(String text) {
        if (StringUtils.isEmpty(text)) return NACK;

        for (StatusCode statusCode : values()) {
            if (statusCode.name().equals(text)) {
                return statusCode;
            }
        }
        return NACK;
    }
}
