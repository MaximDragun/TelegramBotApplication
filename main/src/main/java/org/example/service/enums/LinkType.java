package org.example.service.enums;

public enum LinkType {
    GET_DOC("download_file/get-document"),

    GET_PHOTO("download_file/get-photo");
    private final String value;

    LinkType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
