package utils;

import collection.MusicBand;
import utils.Asker;

import java.io.Serializable;

public class MusicBandFactory implements Serializable {
    Asker asker;

    public MusicBandFactory(Asker ask){
        asker = ask;
    }

    public MusicBand makeBand(){
        return new MusicBand(
                asker.askName(),
                asker.askCoordinates(),
                asker.generateDate(),
                asker.askParticipants(),
                asker.askAlbums(),
                asker.askDescription(),
                asker.askGenre(),
                asker.askBest()
        );
    }
}
