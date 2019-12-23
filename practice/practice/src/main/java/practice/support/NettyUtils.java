package practice.support;

public class NettyUtils {
    public static final String PACKET_LENGTH = "0004";
    public static final int IP_ADDRESS_RANGE = 256;
    public static final int FAKE_COUNT_FIELD = 4;
    public static final int PACKET_LENGTH_FIELD = 4;

    public static boolean isNotNumber(String length) {
        try {
            Integer.parseInt(length);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }
}
