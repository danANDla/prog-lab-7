package commands;

import collection.MusicBand;
import commands.types.RichResponseCommand;
import udp.Response;
import utils.DBmanager;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class Show implements RichResponseCommand {
    private final DBmanager dbmanager;

    public Show(DBmanager dbmanager) {
        this.dbmanager = dbmanager;
    }

    @Override
    public ArrayList<Response> richExecute() {
        int rows = dbmanager.countRows();
        ArrayList<Response> respList = new ArrayList<>();
        String msg;
        if (rows > 0) {
            LinkedHashSet<MusicBand> list = dbmanager.show();
            for (MusicBand mband : list) {
                respList.add(new Response("showbody", mband.toString(), null));
            }
        }
        return respList;
    }
}
