package SQLiteStuff;

import java.io.*;
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

    public synchronized DBResult getResultSet(String sqlStatement, Object... arguments) throws SQLException {
        PreparedStatement preStatement = connection.prepareStatement(sqlStatement);

        for (int i = 0; i < arguments.length; i++)
            preStatement.setObject(i + 1, arguments[i]);

        ResultSet rs = preStatement.executeQuery();

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

                //Deserializes Object if possible, if keep the byte[]
                if (item instanceof byte[]) {
                    Object deserializedObject = deserialize((byte[]) item);
                    if (deserializedObject != null)
                        item = deserializedObject;
                }

                resultQueues.get(i).add(item);
            }

        rs.close();
        preStatement.close();

        return new DBResult(attributes, attributeTypes, resultQueues);
    }

    public synchronized void executeStatement(String sqlStatement, Object... arguments) throws SQLException {
        //Look for arguments that should get serialized
        String[] splitStatement = sqlStatement.split("\\?");
        StringBuilder sb = new StringBuilder(splitStatement[0]);
        for (int i = 0; i < splitStatement.length - 1; i++)
            if (splitStatement[i + 1].charAt(0) == 'S') {
                arguments[i] = serialize(arguments[i]);
                sb.append('?').append(splitStatement[i + 1].substring(1));
            } else
                sb.append('?').append(splitStatement[i + 1]);

        sqlStatement = sb.toString();

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

    private byte[] serialize(Object object) {
        byte[] out;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(object);
            return bos.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }

    private Object deserialize(byte[] bytes) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException ignore) {
            return null;
        }
    }
}
