package org.project.sfc.com.OpenBaton.Configuration;

/**
 * Created by mah on 2/25/16.
 */
public enum Flavor {
    TINY("d1.tiny"),
    SMALL("d1.small"),
    MEDIUM("d1.medium"),
    LARGE("d1.large"),
    CUSTOM("custom");

    private final String value;

    Flavor(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
