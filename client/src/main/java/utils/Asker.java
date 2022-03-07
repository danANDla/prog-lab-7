package utils;

import collection.Album;
import collection.Coordinates;
import collection.MusicGenre;

import java.io.Serializable;
import java.util.Date;

public class Asker implements Serializable {
    private final IOutil io;

    public Asker(IOutil ioutil){
        io = ioutil;
    }

    private String getNonEmpty(){
        String str;
        str = io.readLine().trim();
        while(str.equals("")){
            io.printError("Это обязательно поле");
            str = io.readLine().trim();
        }
        return str;
    }

    private void printGenres(){
        io.printText("список жанров:");
        io.printText("  (1) Progressive rock");
        io.printText("  (2) Jazz");
        io.printText("  (3) Blues");
        io.printText("  (4) Pop");
        io.printText("  (5) Math rock");
    }

    public String askName(){
        io.printText("Введите название группы");
        String bandName;
        bandName = io.readLine().trim();
        while (bandName.equals("")){
            io.printError("Название группы не может быть пустой строкой");
            bandName = io.readLine().trim();
        }
        return bandName;
    }

    public Coordinates askCoordinates(){
        boolean xvalid = false;
        boolean yvalid = false;
        double x = 0;
        int y = 0;
        Coordinates bandCoordinates = new Coordinates();
        io.printText("Введите координаты:");
        io.printText("Координата X (вещественное число)");
        while(!xvalid){
            try {
                x = Double.parseDouble(getNonEmpty());
                xvalid = true;
            } catch (NumberFormatException e){
                io.printError("Неправильный формат вещественного числа");
            }
        }
        io.printText("Координата Y (целое число)");
        while(!yvalid){
            try {
                y = Integer.parseInt(getNonEmpty());
                yvalid = true;
            } catch (NumberFormatException e){
                io.printError("Неправильный формат целого числа");
            }
        }
        bandCoordinates.setX(x);
        bandCoordinates.setY(y);
        return bandCoordinates;
    }

    public Date askDate(){
        boolean dayvalid = false;
        boolean monthvalid = false;
        boolean yearvalid = false;
        int day=1;
        int month=2;
        int year=1970;
        Date creationDate = new Date();
        io.printText("Введите дату создания группы:");
        io.printText("год (целое число 0-2022)");
        while(!yearvalid){
            try{
                year = Integer.parseInt(getNonEmpty());
                if(!(year >= 0 && year <= 2022)){
                    io.printError("Неправильные данные");
                }
                else yearvalid = true;
            } catch (NumberFormatException e){
                io.printError("Неправильный формат целого числа");
            }
        }
        io.printText("месяц (целое число 1-12)");
        while(!monthvalid){
            try{
                month = Integer.parseInt(getNonEmpty());
                if(!(month >= 1 && month <= 12)){
                   io.printError("Неправильные данные");
                }
                else monthvalid = true;
            } catch (NumberFormatException e){
                io.printError("Неправильный формат целого числа");
            }
        }
        io.printText("день (целое число 1-31)");
        while(!dayvalid){
            try{
                day = Integer.parseInt(getNonEmpty());
                if(!(day >= 1 && day <= 31)){
                    io.printError("Неправильные данные");
                }
                else dayvalid = true;
            } catch (NumberFormatException e){
                io.printError("Неправильный формат целого числа");
            }
        }
        creationDate.setYear(year-1900);
        creationDate.setMonth(month);
        creationDate.setDate(day);
        return creationDate;
    }

    public long askParticipants(){
        long participantsNumber = 0;
        boolean valid = false;
        io.printText("Введите количество участников");
        while(!valid){
            try {
                participantsNumber = Long.parseLong(getNonEmpty());
                valid = true;
                if(participantsNumber <= 0){
                    io.printError("Количество участников должно быть больше 0");
                    valid = false;
                }
            } catch (NumberFormatException e){
                io.printError("Неправильный формат целого числа (long)");
            }
        }
        return participantsNumber;
    }

    public int askAlbums(){
        int albumsNumber = 0;
        boolean valid = false;
        io.printText("Введите количество записанных альбомов");
        while(!valid){
            try{
                albumsNumber = Integer.parseInt(getNonEmpty());
                valid = true;
                if(albumsNumber <= 0){
                    io.printError("Количество альбомов должно быть больше 0");
                    valid = false;
                }
            } catch (NumberFormatException e){
                io.printError("Неправильный формат целого числа");
            }
        }
        return albumsNumber;
    }

    public String askDescription(){
        String bandDescription = "";
        io.printText("Введите описание");
        bandDescription = getNonEmpty();
        return bandDescription;
    }

    public MusicGenre askGenre(){
        boolean valid = false;
        int intGenre = -1;
        MusicGenre bandGenre = null;
        io.printText("Введите музыкальный жанр (строка или число 1-5)");
        printGenres();
        while(!valid){
            String str = getNonEmpty();
            str = str.toUpperCase();
            switch (str){
                case "PROGRESSIVE ROCK":
                    intGenre = 1;
                    break;
                case "BLUES":
                    intGenre = 2;
                    break;
                case "JAZZ":
                    intGenre = 3;
                    break;
                case "POP":
                    intGenre = 4;
                    break;
                case "MATH ROCK":
                    intGenre = 5;
                default:{
                    try{
                        intGenre = Integer.parseInt(str);
                    } catch (NumberFormatException e){
                        //io.printError("Неправильный формат ввода");
                    }
                }
            }
            switch(intGenre){
                case 1:
                    bandGenre = MusicGenre.PROGRESSIVE_ROCK;
                    valid = true;
                    break;
                case 2:
                    bandGenre = MusicGenre.JAZZ;
                    valid = true;
                    break;
                case 3:
                    bandGenre = MusicGenre.BLUES;
                    valid = true;
                    break;
                case 4:
                    bandGenre = MusicGenre.POP;
                    valid = true;
                    break;
                case 5:
                    bandGenre = MusicGenre.MATH_ROCK;
                    valid = true;
                    break;
                default:
                    io.printError("Неправильный формат ввода");
            }
        }
        return bandGenre;
    }

    public Album askBest(){
        io.printText("Введите данные о лучшем альбоме");
        return new Album(askAlbumName(), askTracksNumber(), askAlbumLength(), askAlbumSales());
    }
    private String askAlbumName(){
        io.printText("название альбома");
        String albumName = getNonEmpty();
        return albumName;
    }
    private int askTracksNumber(){
        io.printText("количество треков в альбоме");
        int tracksNumber = -1;
        boolean valid = false;
        while(!valid){
            try{
                tracksNumber = Integer.parseInt(getNonEmpty());
                valid = true;
                if(tracksNumber <= 0){
                    io.printError("Количество треков должно быть больше 0");
                    valid = false;
                }
            } catch (NumberFormatException e){
                io.printError("Неправильный формат целого числа");
            }
        }
        return tracksNumber;
    }
    private Integer askAlbumLength(){
        io.printText("продолжительность альбома");
        String str;
        int albumLength = -1;
        boolean valid = false;
        while (!valid){
            str = io.readLine().trim();
            if(str.equals("")) return albumLength;
            try{
                albumLength = Integer.parseInt(getNonEmpty());
                valid = true;
                if(albumLength <= 0){
                    io.printError("Продолжительность должна быть быть больше 0");
                    valid = false;
                }
            } catch (NumberFormatException e){
                io.printError("Неправильный формат целого числа");
            }
        }
        return albumLength;
    }
    private double askAlbumSales(){
        io.printText("продажи альбома (вещественное число)");
        String str;
        double albumSales = -1;
        boolean valid = false;
        while (!valid){
            str = io.readLine().trim();
            if(str.equals("")) return -1;
            try{
                albumSales = Double.parseDouble(str);
                valid = true;
                if(albumSales <= 0){
                    io.printError("продажи должны быть быть больше 0");
                    valid = false;
                }
            } catch (NumberFormatException e){
                io.printError("Неправильный формат вещественного числа");
            }
        }
        return albumSales;
    }
}