package commands;

import commands.types.Command;
import udp.Response;
import utils.DBmanager;

public class Info implements Command {
    private static String dbpath;
    DBmanager dbmanager;

    public Info(DBmanager dbmanager) {
        this.dbmanager = dbmanager;
    }

    @Override
    public Response execute() {
        Response resp = new Response();
        resp.setReceiver(null);
        resp.setCommand("info");
        int rows = dbmanager.countRows();
        String msg = "";
        if (rows < 1) {
            msg = "Коллекция пуста";
        } else {
            msg = "дата инициализации коллекции: " + dbmanager.getCreationDate() + "\n" +
                    "количество элементов в коллекции: " + rows;
        }
        resp.setMsg(msg);
        return resp;
    }
}
