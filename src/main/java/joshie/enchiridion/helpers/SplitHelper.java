package joshie.enchiridion.helpers;

import java.util.Arrays;

public class SplitHelper {
    public static String[] splitStringEvery(String string, int interval) {
        int arrayLength = (int) Math.ceil(((string.length() / (double) interval)));
        String[] result = new String[arrayLength];

        int j = 0;
        int lastIndex = result.length - 1;
        for (int i = 0; i < lastIndex; i++) {
            result[i] = string.substring(j, j + interval);
            j += interval;
        }

        result[lastIndex] = string.substring(j);
        return result;
    }

    public static byte[][] splitByteArrayEvery(byte[] bytes, int interval) {
        int size = ((bytes.length - 1) / interval) + 1;
        byte[][] newArray = new byte[size][];
        int counter = 0;

        for (int i = 0; i < bytes.length - interval + 1; i += interval)
            newArray[counter++] = Arrays.copyOfRange(bytes, i, i + interval);

        if (bytes.length % interval != 0)
            newArray[counter] = Arrays.copyOfRange(bytes, bytes.length - bytes.length % interval, bytes.length);

        return newArray;
    }
}