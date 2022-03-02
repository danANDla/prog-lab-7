package utils;

import collection.MusicBand;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DBmanager {
    private static Connection connection;
    private static String DBurl;
    private static String DBusername;
    private static String DBpassword;
    private static IOutil io;

    private static final String ADD = "INSERT INTO BANDS (name, x, y, creation, participants, albums, description, " +
            "genre, bestname, besttracks, bestlength, bestsales) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

    static {
        try {
            Class.forName("org.postgresql.Driver");
            initConnect();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public DBmanager(IOutil ioutil) {
        io = ioutil;
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

    private static void initConnect() {
        try {
            if (getProps()) connection = DriverManager.getConnection(DBurl, DBusername, DBpassword);
            System.out.println(DBurl + DBusername + DBpassword);
            if (connection != null) {
                System.out.println("Connected to DB");
            } else {
                System.out.println("Failed connect to DB");
            }
        } catch (SQLException ex) {
            System.out.println("Exception while connecting to DB");
        }
    }

    public boolean add(MusicBand band) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(ADD, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, band.getName());
            preparedStatement.setString(2, band.getCoordinates().getX().toString());
            preparedStatement.setString(3, band.getCoordinates().getY().toString());
            preparedStatement.setString(4, band.getCreationDate().toString());
            preparedStatement.setString(5, Long.toString(band.getNumberOfParticipants()));
            preparedStatement.setString(6, Integer.toString(band.getAlbumsCount()));
            preparedStatement.setString(7, band.getDescription());
            preparedStatement.setString(8, band.getGenre().toString());
            preparedStatement.setString(9, band.getBestAlbum().getName());
            preparedStatement.setString(10, Integer.toString(band.getBestAlbum().getTracks()));
            preparedStatement.setString(11, band.getBestAlbum().getLength().toString());
            preparedStatement.setString(12, Double.toString(band.getBestAlbum().getSales()));
            if (preparedStatement.executeUpdate() != 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    Integer id = resultSet.getInt("id");
                    band.setId(id);
                }
                io.printText("added new band");
                return true;
            }

        } catch (SQLException e) {
            io.printError("Exception while adding band to BD");
        }
        return false;
    }
}
