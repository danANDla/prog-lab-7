package commands;

public enum CommandStatus {
    OK("command executed successfully");

    private String description;

    CommandStatus(String s) {
        this.description = s;
    }

    public String getDescription(){
        return description;
    }
}
