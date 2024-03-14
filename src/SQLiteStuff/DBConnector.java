package SQLiteStuff;

import java.sql.*;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

@SuppressWarnings("unused")
public class DBConnector {

    Connection connection;

    public DBConnector(String filePath) {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + filePath);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't load sqlite-jdbc driver");
        }
    }

    public synchronized DBResult getResultSet(String sqlStatement) throws SQLException {
        //Make sure SELECT statement is used
        if (!sqlStatement.split(" ")[0].equalsIgnoreCase("SELECT"))
            return null;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sqlStatement);

        int columCount = rs.getMetaData().getColumnCount();

        //Get attributes
        String[] attributes = new String[columCount];
        for (int i = 0; i < columCount; i++)
            attributes[i] = rs.getMetaData().getColumnName(i + 1);

        //Get attribute types
        int[] attributeTypes = new int[columCount];
        for (int i = 0; i < columCount; i++)
            attributeTypes[i] = rs.getMetaData().getColumnType(i + 1);

        //Init result queue
        ArrayList<Queue<Object>> resultQueues = new ArrayList<>(columCount);
        for (int i = 0; i < columCount; i++)
            resultQueues.add(new LinkedBlockingQueue<>());

        //Load data into result queue
        while (rs.next())
            for (int i = 0; i < columCount; i++) {
                Object item = rs.getObject(i + 1);
                if (item == null) item = DBResult.EMPTY;
                resultQueues.get(i).add(item);
            }

        rs.close();
        statement.close();

        return new DBResult(attributes, attributeTypes, resultQueues);
    }

    public synchronized void executeStatement(String sqlStatement, Object... arguments) throws SQLException {
        PreparedStatement preStatement = connection.prepareStatement(sqlStatement);
        // set parameters
        for (int i = 0; i < arguments.length; i++)
            preStatement.setObject(i + 1, arguments[i]);

        preStatement.executeUpdate();
        connection.commit();
        preStatement.close();
    }

    public synchronized void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
