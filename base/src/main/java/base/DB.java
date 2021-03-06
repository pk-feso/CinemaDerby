package base;

import java.lang.ref.PhantomReference;
import java.sql.*;

/**
 * Created by aleksejpluhin on 01.03.16.
 */
public class DB {
    public DB() {
    }

    public Connection getConnection(boolean isMemory) throws SQLException {
        // Connection db = DriverManager.getConnection("jdbc:postgresql://localhost/Cinema", "", "");
        Connection connection;


        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (isMemory) {
            connection = DriverManager.getConnection("jdbc:derby:memory:test;create=true");
            createDB(connection);

        } else {
            try {
                connection = DriverManager.getConnection("jdbc:derby:CinemaDB;create=false");
            } catch (SQLException e) {
                connection = DriverManager.getConnection("jdbc:derby:CinemaDB;create=true");
                createDB(connection);
                System.out.println("ad");
                insertHalls(connection);

            }
        }

        return connection;
    }

    private void insertHalls(Connection connection) throws SQLException {
        PreparedStatement state1 = connection.prepareStatement("INSERT INTO HALLS (nrows,seats) VALUES (20,20)");
        PreparedStatement state2 = connection.prepareStatement("INSERT INTO HALLS (nrows,seats) VALUES (30,30)");
        PreparedStatement state3 = connection.prepareStatement("INSERT INTO HALLS (nrows,seats) VALUES (40,40)");
        PreparedStatement state4 = connection.prepareStatement("INSERT INTO HALLS (nrows,seats) VALUES (50,50)");
        state1.execute();
        state2.execute();
        state3.execute();
        state4.execute();
        state1.close();
        state2.close();
        state3.close();
        state4.close();
    }


    public void createDB(Connection db) throws SQLException {

        PreparedStatement halls = db.prepareStatement("CREATE TABLE HALLS (" +
                "id INT NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH 100) PRIMARY KEY, " +
                "nrows INT NOT NULL, " +
                "seats INT NOT NULL)");
        halls.execute();
        PreparedStatement seances = db.prepareStatement("CREATE TABLE SEANCES (" +
                "id INT NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH 100) PRIMARY KEY, " +
                "stime TIMESTAMP NOT NULL," +
                "FILM VARCHAR(15), " +
                "hall INT NOT NULL," +
                "price DOUBLE NOT NULL," +
                "age  INT NOT NULL, " +
                "FOREIGN KEY (hall) REFERENCES HALLS (id)" +
                ")");
        seances.execute();
        PreparedStatement reservation = db.prepareStatement("CREATE TABLE RESERVATIONS (" +
                "id  INT NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH 100) PRIMARY KEY, " +
                "userid int not null," +
                "row INT NOT NULL, " +
                "seat INT NOT NULL, " +
                "seance INT NOT NULL," +
                "FOREIGN KEY (seance) REFERENCES SEANCES (id))");
        reservation.execute();


    }


}
