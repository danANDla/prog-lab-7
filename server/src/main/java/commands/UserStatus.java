package commands;

public enum UserStatus {
    FAIL("failed"),
    BAD_LOGIN("badlogin"),
    BAD_AUTH("badauth"),
    OK("ok");

    private String description;

    UserStatus(String s) {
        this.description = s;
    }

    public String getDescription() {
        return description;
    }
}
