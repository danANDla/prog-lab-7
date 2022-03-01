package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DBmanager {
    private String DBurl;
    private String DBusername;
    private String DBpassword;
    private IOutil io;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public DBmanager (IOutil ioutil){
        this.io = ioutil;
    }

    private void getProps() {
        Properties prop = new Properties();
        String path = "";
        String fileName = "/home/danandla/BOTAY/programming/labs/prog-7/prog-lab-7/app.config";
        try (FileInputStream fis = new FileInputStream(fileName)) {
            prop.load(fis);
            DBurl = prop.getProperty("app.dburl");
            DBusername = prop.getProperty("app.dbusername");
            DBpassword = prop.getProperty("app.dbpassword");
        } catch (FileNotFoundException ex) {
            io.printError("Can't find config file");
        } catch (IOException ex) {
            io.printError("Unknown error while getting db props");
        }
    }

    private void initConnect(){
        try {
            getProps();
            Connection conn = DriverManager.getConnection(DBurl, DBusername, DBpassword);
            if (conn != null) {
                System.out.println("Connected");
            }
        } catch (SQLException ex) {
            io.printError("Can't connect to db");
        }
    }
}
