package commands;

import commands.types.Command;
import udp.Response;
import utils.CollectionManager;
import utils.DBmanager;
import utils.IOutil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.util.Properties;

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
        if(rows < 1){
            msg = "Коллекция пуста";
        }
        else{
            msg = "дата инициализации коллекции: " + dbmanager.getCreationDate() + "\n" +
                    "количество элементов в коллекции: " + rows;
        }
        resp.setMsg(msg);
        return resp;
    }

    @Override
    public String getdescription() {
        String descr = "вывести информацию о коллекции";
        return descr;
    }
}
