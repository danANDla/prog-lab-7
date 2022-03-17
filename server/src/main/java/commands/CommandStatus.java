package commands;

public enum CommandStatus {
    OK("command executed successfully"),
    NO_RIGHTS("no rights"),
    BAD_ID("no such id"),
    FAIL("command failed"),
    EMPTY_COLLECTION("collection is already empty");

    private String description;

    CommandStatus(String s) {
        this.description = s;
    }

    public String getDescription(){
        return description;
    }
}
