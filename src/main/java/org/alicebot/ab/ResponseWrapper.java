package org.alicebot.ab;

public class ResponseWrapper<T> {

    private T response;

    public ResponseWrapper(T response) {
        this.response = response;
    }

    public T getResponse() {
        return this.response;
    }

}
