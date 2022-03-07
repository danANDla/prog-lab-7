package commands;

import commands.types.Command;
import udp.Response;
import utils.DBmanager;

public class Clear implements Command {
    private DBmanager dbmanager;

    public Clear(DBmanager dbmanager) {
        this.dbmanager = dbmanager;
    }

    @Override
    public Response execute() {
        CommandStatus res = CommandStatus.FAIL;
        if(dbmanager.clearTable()) res = CommandStatus.OK;
        return new Response("clear", res.getDescription(), null);
    }

    @Override
    public String getdescription() {
        String descr = "очистить коллекцию";
        return descr;
    }
}
