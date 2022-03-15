package utils;

import collection.Album;
import collection.Coordinates;
import collection.MusicBand;
import collection.MusicGenre;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Properties;

public class DBmanager {
    private static Connection connection;
    private static String DBurl;
    private static String DBusername;
    private static String DBpassword;
    private static IOutil io;
    private LocalDate creationDate;

    private static final String ADD = "INSERT INTO BANDS (name, x, y, creation, participants, albums, description," +
            "genre, bestname, besttracks, bestlength, bestsales) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String UPDATE = "UPDATE BANDS SET (name, x, y, creation, participants, albums, description," +
            "genre, bestname, besttracks, bestlength, bestsales) = (?,?,?,?,?,?,?,?,?,?,?,?) WHERE id=?";
    private static final String COUNT = "SELECT COUNT(*) FROM BANDS";
    private static final String DELETE = "DELETE FROM BANDS";
    private static final String DELETE_BY_ID = "DELETE FROM BANDS WHERE id=?";
    private static final String SHOW = "SELECT * FROM BANDS";

    static {
        try {
            Class.forName("org.postgresql.Driver");
            while (!initConnect()) {
                Thread.sleep(1000);
            }
            ;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public DBmanager(IOutil ioutil) {
        io = ioutil;
        creationDate = LocalDate.now();
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    private static boolean getProps() {
        Properties prop = new Properties();
        String path = "";
        String fileName = "/home/danandla/BOTAY/programming/labs/prog-7/prog-lab-7/app.config";
        try (FileInputStream fis = new FileInputStream(fileName)) {
            prop.load(fis);
            DBurl = prop.getProperty("app.dburl");
            DBusername = prop.getProperty("app.dbusername");
            DBpassword = prop.getProperty("app.dbpassword");
            return true;
        } catch (FileNotFoundException ex) {
            io.printError("Can't find config file");
        } catch (IOException ex) {
            io.printError("Unknown error while getting DB props");
        }
        return false;
    }

    private static boolean initConnect() {
        try {
            if (getProps()) connection = DriverManager.getConnection(DBurl, DBusername, DBpassword);
            System.out.println(DBurl + DBusername + DBpassword);
            if (connection != null) {
                System.out.println("Connected to DB");
                return true;
            } else {
                System.out.println("Failed connect to DB");
            }
        } catch (SQLException ex) {
            System.out.println("Exception while connecting to DB");
        }
        return false;
    }

    public boolean add(MusicBand band) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(ADD, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, band.getName());
            preparedStatement.setDouble(2, band.getCoordinates().getX());
            preparedStatement.setInt(3, band.getCoordinates().getY());
            preparedStatement.setString(4, band.getCreationDate().toString());
            preparedStatement.setLong(5, band.getNumberOfParticipants());
            preparedStatement.setInt(6, band.getAlbumsCount());
            preparedStatement.setString(7, band.getDescription());
            preparedStatement.setString(8, band.getGenre().toString());
            preparedStatement.setString(9, band.getBestAlbum().getName());
            preparedStatement.setInt(10, band.getBestAlbum().getTracks());
            preparedStatement.setInt(11, band.getBestAlbum().getLength());
            preparedStatement.setDouble(12, band.getBestAlbum().getSales());
            System.out.println(preparedStatement.toString());
            if (preparedStatement.executeUpdate() != 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    long id = resultSet.getLong(1);
                    io.printText("added band, id = " + id);
                    return true;
                }
            }

        } catch (SQLException e) {
            io.printError("Exception while adding band to DB " + e);
        }
        return false;
    }

    public boolean update(int bandid, MusicBand band) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, band.getName());
            preparedStatement.setDouble(2, band.getCoordinates().getX());
            preparedStatement.setInt(3, band.getCoordinates().getY());
            preparedStatement.setString(4, band.getCreationDate().toString());
            preparedStatement.setLong(5, band.getNumberOfParticipants());
            preparedStatement.setInt(6, band.getAlbumsCount());
            preparedStatement.setString(7, band.getDescription());
            preparedStatement.setString(8, band.getGenre().toString());
            preparedStatement.setString(9, band.getBestAlbum().getName());
            preparedStatement.setInt(10, band.getBestAlbum().getTracks());
            preparedStatement.setInt(11, band.getBestAlbum().getLength());
            preparedStatement.setDouble(12, band.getBestAlbum().getSales());
            preparedStatement.setInt(13, bandid);
            System.out.println(preparedStatement.toString());
            if (preparedStatement.executeUpdate() != 0) {
                return true;
            }

        } catch (SQLException e) {
            io.printError("Exception while updating band " + e);
        }
        return false;
    }

    public boolean clearTable() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE);
            if (preparedStatement.executeUpdate() != 0) {
                return true;
            }
        } catch (SQLException e) {
            io.printError("Exception while clearing the table" + e);
        }
        return false;
    }

    public boolean removeById(int bandid) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID);
            preparedStatement.setInt(1, bandid);
            if (preparedStatement.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            io.printError("Exception while deleting element");
        }
        return false;
    }

    public int countRows() {
        int rows = -1;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(COUNT);
            ResultSet res = preparedStatement.executeQuery();
            if (res.next()) {
                rows = res.getInt(1);
            }
        } catch (SQLException e) {
            io.printError("Exception while counting rows " + e);
        }
        return rows;
    }

    public LinkedHashSet<MusicBand> show() {
        LinkedHashSet<MusicBand> list = new LinkedHashSet<MusicBand>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SHOW);
            ResultSet res = preparedStatement.executeQuery();
            while (res.next()) {
                MusicBand band = new MusicBand(
                        res.getString("name"),
                        new Coordinates(res.getDouble("x"), res.getInt("y")),
                        new Date(System.currentTimeMillis()),
                        res.getLong("participants"),
                        res.getInt("albums"),
                        res.getString("description"),
                        MusicGenre.BLUES,
                        new Album(
                                res.getString("bestname"),
                                res.getInt("besttracks"),
                                res.getInt("bestlength"),
                                res.getDouble("bestsales")
                        )
                );
                band.setId(res.getInt("id"));
                list.add(band);
            }
        } catch (NullPointerException | SQLException e) {
            io.printError("Exception while showing the table" + e);
        }
        return list;
    }
}
