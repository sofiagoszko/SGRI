package com.api.sgri.response;

public class HttpBodyResponse {

    private String status;
    private int statusCode;
    private String message;
    private Object data;
    private long timestamp;
    private String path; 
    private String userFriendlyMessage;

    private HttpBodyResponse(Builder builder) {
        this.status = builder.status;
        this.statusCode = builder.statusCode;
        this.message = builder.message;
        this.data = builder.data;
        this.timestamp = builder.timestamp;
        this.path = builder.path;
        this.userFriendlyMessage = builder.userFriendlyMessage;
    }

    public String getStatus() {
        return status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getPath() {
        return path;
    }

    public String getUserFriendlyMessage() {
        return userFriendlyMessage;
    }


    public static class Builder {
        private String status;
        private int statusCode;
        private String message;
        private Object data;
        private long timestamp;
        private String path; 
        private String userFriendlyMessage;

        public Builder() {
            this.timestamp = System.currentTimeMillis();
            this.statusCode = 200;
            this.status = "Success";
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder message(String ...messages) {
            this.message = String.join(" ", messages);
            return this;
        }

        public Builder data(Object data) {
            this.data = data;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder userFriendlyMessage(String userFriendlyMessage) {
            this.userFriendlyMessage = userFriendlyMessage;
            return this;
        }

        public HttpBodyResponse build() {
            return new HttpBodyResponse(this);
        }
    }
}
