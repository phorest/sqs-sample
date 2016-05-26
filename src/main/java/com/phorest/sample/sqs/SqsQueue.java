package com.phorest.sample.sqs;


public class SqsQueue {

    private final String name;
    private final String url;

    public SqsQueue(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
