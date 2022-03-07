package utils;


import collection.Coordinates;
import collection.MusicBand;
import commands.CommandStatus;
import exceptions.InvalidIdException;

import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedHashSet;

public class CollectionManager {
    private Integer bandId = 0;
    private LinkedHashSet<MusicBand> bandsList;
    private IOutil io;
    private LocalDate creationDate;

    public CollectionManager(IOutil ioutil){
        io = ioutil;
        bandsList = new LinkedHashSet<MusicBand>();
        creationDate = LocalDate.now();
    }

    public CollectionManager(){
        io = new IOutil();
        bandsList = new LinkedHashSet<MusicBand>();
        creationDate = LocalDate.now();
    }

    public void setBandsList(LinkedHashSet<MusicBand> bandsList) {
        this.bandsList = bandsList;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public boolean validate(MusicBand band){
        System.out.println(band);
        boolean res = true;
        if(band.getId() == null){
            io.printWarning("Id элемента не задан и будет сгенерирован автоматически");
        }
        else if(band.getId() < 0){
            io.printWarning("Введенный Id меньше нуля, правильный Id будет сгенерирован автоматически");
        }
        else if(idTakenTwice(band.getId())){
            io.printWarning("Введенный Id уже занят, правильный Id будет сгенерирован автоматически");
        }

        if (band.getName().equals("")){
            io.printError("Название группы не может быть пустой строкой");
            res = false;
        }

        if(band.getCoordinates().getX() == null){
            io.printError("Координата X не может быть null");
            res = false;
        }
        if(band.getCoordinates().getY() == null){
            io.printError("Координата Y не может быть null");
            res = false;
        }
        else if(band.getCoordinates().getY() <=-620){
            io.printError("Координата Y не может быть меньше -620");
            res = false;
        }

        if(band.getCreationDate() == null){
            io.printError("creationDate не может быть null");
            res = false;
        }

        if(band.getNumberOfParticipants() <= 0){
            io.printError("Количество участников не может быть меньше 0");
            res = false;
        }

        if(band.getAlbumsCount() <= 0){
            io.printError("Количество альбомов не может быть меньше 0");
            res = false;
        }

        if(band.getGenre() == null){
            io.printError("genre не может быть null");
            res = false;
        }

        if(band.getBestAlbum() == null){
            io.printError("bestAlbum не может быть null");
            res = false;
        }
        else{
            if(band.getBestAlbum().getName().equals("")){
                io.printError("Название альбома не может быть пустой строкой");
                res = false;
            }
            if(band.getBestAlbum().getTracks()<= 0){
                io.printError("Количество треков в альбоме не может быть меньше 0");
                res = false;
            }
            if(band.getBestAlbum().getLength()<= 0){
                io.printError("Продолжительность альбома не может быть меньше 0");
                res = false;
            }
            if(band.getBestAlbum().getSales()<= 0){
                io.printError("Продажи альбома не могут быть меньше 0");
                res = false;
            }
        }
        return res;
    }

    public CommandStatus insertBand(MusicBand newBand){
        bandId+=1;
        if(idTaken(bandId) || (bandId > bandsList.size()*2)){
            bandId = findNewId(bandId);
        }
        newBand.setId(bandId);
        bandsList.add(newBand);
        return CommandStatus.OK;
    }

    public LinkedHashSet<MusicBand> getBandsList() {
        return bandsList;
    }

    private boolean idTaken(Integer checkId){
        for(MusicBand band: bandsList){
            if(band.getId() == checkId) return true;
        }
        return false;
    }

    private boolean idTakenTwice(Integer checkId){
        int k = 0;
        for(MusicBand band: bandsList){
            if(band.getId() == checkId) k+=1;
            if(k >= 2) return true;
        }
        return false;
    }

    private Integer findNewId(Integer badId){
        badId = 1;
        while(idTaken(badId)){
            badId+=1;
        }
        return badId;
    }

    private MusicBand getBandById(Integer id) throws InvalidIdException{
        boolean found = false;
        for(MusicBand band: bandsList){
            if(band.getId() == id){
                return band;
            }
        }
        if(!found) throw new InvalidIdException();
        return null;
    }

    public boolean updateBand(Integer idUpdate){
        return true;
    }

    public boolean updateBandField(Integer idUpdate, String field){
        return true;
    }

    public boolean removeBand(Integer idRemove){
        if(bandsList.isEmpty()){
            io.printError("Коллекция пуста");
            return false;
        }
        boolean found = false;
        for(MusicBand band: bandsList){
            if(idRemove == band.getId()){
                found = true;
                bandsList.remove(band);
                bandId = idRemove;
                return found;
            }
        }
        return found;
    }

    public void clearList(){
        bandsList.clear();
    }

    public void saveToXML(){
        // TODO XML save
    }

    public void removeGreater(){
        // todo removeGreater
    }

    public void removeLower(){
        // todo removeLower
    }

    public int albumsCount(){
        int albumCounter = 0;
        for(MusicBand band: bandsList){
            albumCounter += band.getAlbumsCount();
        }
        return albumCounter;
    }

    public void removeByAlbumsCount(int albumNumber){
        for(MusicBand band: bandsList){
            if(band.getAlbumsCount() == albumNumber){
                bandId = band.getId();
                bandsList.remove(band);
            }
        }
    }

    public void groupByDescription(){
        // todo groupByDescription
    }

    public String info(){
        String msg;
        if(bandsList.isEmpty()){
            msg = "Коллекция пуста";
        }
        else{
            msg = "дата инициализации коллекции: " + creationDate + "\n" +
                    "количество элементов в коллекции: " + bandsList.size();
        }
        return msg;
    }

    public void show(){
        if(bandsList.isEmpty()){
            io.printError("Коллекция пуста");
        }
        else{
            for(MusicBand band: bandsList){
                io.printText(band.toString());
            }
        }
    }
}
