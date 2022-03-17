package utils;


import collection.MusicBand;
import commands.CommandStatus;
import exceptions.InvalidIdException;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.concurrent.locks.ReentrantLock;

public class CollectionManager {
    private DBmanager db;
    private Integer bandId = 0;
    private LinkedHashSet<MusicBand> bandsList;
    private IOutil io;
    private LocalDate creationDate;
    private final ReentrantLock lock = new ReentrantLock();

    public CollectionManager(IOutil ioutil, DBmanager dbmanager) {
        io = ioutil;
        db = dbmanager;
        bandsList = new LinkedHashSet<MusicBand>();
        creationDate = LocalDate.now();
    }

    public CollectionManager() {
        io = new IOutil();
        bandsList = new LinkedHashSet<MusicBand>();
        creationDate = LocalDate.now();
    }

    public void sync(){
        bandsList = db.show();
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setBandsList(LinkedHashSet<MusicBand> bandsList) {
        this.bandsList = bandsList;
    }

    private void lock() {
        while (!lock.tryLock()) ;
    }

    private void unlock(){
        lock.unlock();
    }

    public CommandStatus insertBand(String login, MusicBand newBand) {
        lock();
        Integer newId = db.add(login, newBand);
        if(newId != null){
            newBand.setId(newId);
            bandsList.add(newBand);
            unlock();
            return CommandStatus.OK;
        }
        unlock();
        return CommandStatus.FAIL;
    }

    public boolean removeBand(Integer idRemove) {
        lock();
        if(db.removeById(idRemove)){
            for (MusicBand band : bandsList) {
                if (idRemove == band.getId()) {
                    bandsList.remove(band);
                    unlock();
                    return true;
                }
            }
        }
        unlock();
        return false;
    }

    public CommandStatus updateBand(String login, Integer idUpdate, MusicBand newBand) {
        lock();
        CommandStatus res = db.update(login, idUpdate, newBand);
        if(res == CommandStatus.OK){
            for (MusicBand band : bandsList) {
                if (band.getId() == idUpdate) {
                    bandsList.remove(band);
                    newBand.setId(idUpdate);
                    bandsList.add(newBand);
                    unlock();
                    return res;
                }
            }
        }
        unlock();
        return res;
    }

    public boolean clearList() {
        lock();
        if(db.clearTable()){
            bandsList.clear();
            unlock();
            return true;
        }
        unlock();
        return false;
    }

    public String info() {
        String msg;
        if (bandsList.isEmpty()) {
            msg = "Коллекция пуста";
        } else {
            msg = "дата инициализации коллекции: " + creationDate + "\n" +
                    "количество элементов в коллекции: " + bandsList.size();
        }
        return msg;
    }

    public LinkedHashSet<MusicBand> getBandsList() {
        return bandsList;
    }

    private MusicBand getBandById(Integer id) throws InvalidIdException {
        boolean found = false;
        for (MusicBand band : bandsList) {
            if (band.getId() == id) {
                return band;
            }
        }
        if (!found) throw new InvalidIdException();
        return null;
    }

    public void removeGreater() {
        // todo removeGreater
    }

    public void removeLower() {
        // todo removeLower
    }

    public int albumsCount() {
        int albumCounter = 0;
        for (MusicBand band : bandsList) {
            albumCounter += band.getAlbumsCount();
        }
        return albumCounter;
    }

    public void removeByAlbumsCount(int albumNumber) {
        for (MusicBand band : bandsList) {
            if (band.getAlbumsCount() == albumNumber) {
                bandId = band.getId();
                bandsList.remove(band);
            }
        }
    }

    public void groupByDescription() {
        // todo groupByDescription
    }


    public void show() {
        if (bandsList.isEmpty()) {
            io.printError("Коллекция пуста");
        } else {
            for (MusicBand band : bandsList) {
                io.printText(band.toString());
            }
        }
    }
}
