package commands;

import collection.MusicBand;
import commands.types.RichResponseCommand;
import udp.Response;
import utils.CollectionManager;
import utils.DBmanager;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class Show implements RichResponseCommand {

    private CollectionManager collectionManager;

    public Show(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public ArrayList<Response> richExecute() {
        ArrayList<Response> respList = new ArrayList<>();
        LinkedHashSet<MusicBand> list = collectionManager.getBandsList();
        for (MusicBand mband : list) {
            respList.add(new Response("showbody", mband.toString(), null));
        }
        return respList;
    }
}
