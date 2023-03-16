package client.utils;

public class ControllerCommunicater {
    private static String key;

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        ControllerCommunicater.key = key;
    }
}
