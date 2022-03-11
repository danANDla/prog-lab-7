package udp;

public enum ResponseError {
    INVALID_COMMAND("invalid command");

    private String description;

    ResponseError(String s) {
        this.description = s;
    }

    public String getDescription() {
        return description;
    }
}
