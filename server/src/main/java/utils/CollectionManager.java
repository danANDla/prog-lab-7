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
            newBand.setUserLogin(login);
            bandsList.add(newBand);
            unlock();
            return CommandStatus.OK;
        }
        unlock();
        return CommandStatus.FAIL;
    }

    public CommandStatus removeBand(String login, Integer idRemove) {
        lock();
        CommandStatus res = db.removeById(login, idRemove);
        if(res == CommandStatus.OK){
            for (MusicBand band : bandsList) {
                if (idRemove == band.getId()) {
                    bandsList.remove(band);
                    unlock();
                    return res;
                }
            }
        }
        unlock();
        return CommandStatus.FAIL;
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

    public CommandStatus clearList(String login) {
        lock();
        CommandStatus res = db.clearTable(login);
        if(res == CommandStatus.OK){
            bandsList.clear();
            unlock();
            return res;
        }
        unlock();
        return res;
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

    public CommandStatus removeLower(String login, MusicBand musicBand) {
        lock();
        for(MusicBand band: this.bandsList){
            if(band.getUserLogin().equals(login) && band.compareTo(musicBand) < 0){
                if(db.removeById(login, band.getId()) == CommandStatus.OK){
                    this.bandsList.remove(band);
                }
            }
        }
        unlock();
        return CommandStatus.OK;
    }

    public CommandStatus removeByAlbumsCount(String login, int albumNumber) {
        lock();
        for (MusicBand band : bandsList) {
            if (band.getAlbumsCount() == albumNumber) {
                if(db.removeById(login, band.getId()) == CommandStatus.OK){
                    this.bandsList.remove(band);
                }
            }
        }
        unlock();
        return CommandStatus.OK;
    }

    public String groupByDescription() {
        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        int z = 0;
        for (MusicBand band : bandsList) {
            if(band.getDescription().length() == 0) z += 1;
            else if(band.getDescription().length() > 0 && band.getDescription().length() < 100){
                a+=1;
            }
            else if(band.getDescription().length() > 99 && band.getDescription().length() < 150){
                b+=1;
            }
            else if(band.getDescription().length() > 149 && band.getDescription().length() < 200){
                c+=1;
            }
            else{
                d+=1;
            }
        }
        String str = "group by words \n 0-99: " + Integer.toString(a) +
                ", 100 - 149: " + Integer.toString(b) +
                ", 150 - 199: " + Integer.toString(c) +
                ", 200 - ...: " + Integer.toString(d) +
                ", empty: " + Integer.toString(z);
        return str;
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

    public String albumsCount(){
        long count = 0;
        for(MusicBand band: bandsList){
            count += band.getAlbumsCount();
        }
        return Long.toString(count);
    }
}
