package org.example;

import org.hashids.Hashids;


public class EncryptionTool {
    private final Hashids hashids;

    public EncryptionTool(String salt) {
        int minLength = 10;
        this.hashids = new Hashids(salt, minLength);
    }

    public String hashOn(Long value) {
        return hashids.encode(value);
    }

    public Long hashOff(String value) {
        return hashids.decode(value)[0];
    }
}
