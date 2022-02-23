package collection;

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
                asker.askDate(),
                asker.askParticipants(),
                asker.askAlbums(),
                asker.askDescription(),
                asker.askGenre(),
                asker.askBest()
        );
    }
}
