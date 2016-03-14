package org.project.sfc.com.OpenBaton.Configuration;

/**
 * Created by mah on 2/25/16.
 */
public enum Flavor {
    TINY("m1.tiny"),
    SMALL("m1.small"),
    MEDIUM("m1.medium"),
    LARGE("m1.large"),
    CUSTOM("custom");

    private final String value;

    Flavor(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
