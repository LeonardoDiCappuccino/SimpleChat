package SQLiteStuff;

import Communication.Container;
import Communication.SerializableImage;
import org.sqlite.SQLiteErrorCode;

import javax.imageio.ImageIO;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChatDB extends DBConnector {

    //Make it a Singleton
    private static final ChatDB instance = new ChatDB();

    private static final int MESSAGE_LOAD_LIMIT = 10;

    private ChatDB() {
        super("./rsc/Chat.db");
    }

    public static ChatDB getInstance() {
        return instance;
    }

    public synchronized Container.Profile getUserProfile(String username, String password) {
        try {
            DBResult resultSet = getResultSet(
                    "SELECT id, profilePicture FROM Users WHERE name = ?" +
                            " AND password = ?;", username, password);

            //User not found
            if (resultSet.isEmpty())
                return null;

            //Get user profile information. username already given as parameter
            int userID = resultSet.getInteger(0, 0);
            SerializableImage profilePicture = resultSet.getCast(1, 0);

            return new Container.Profile(userID, username, profilePicture);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized Container.Profile createUser
            (String username, String password, SerializableImage profilePicture) {
        try {
            executeStatement
                    ("INSERT INTO Users (name, password, profilePicture) VALUES (?, ?, ?);",
                            username, password, profilePicture);
        } catch (SQLException e) {
            //If username already exist
            if (e.getErrorCode() == SQLiteErrorCode.SQLITE_CONSTRAINT.code)
                return null;
            else
                throw new RuntimeException(e);
        }
        return getUserProfile(username, password);
    }

    public synchronized List<Container.Profile> getChats(int userID) {
        try {
            DBResult resultSet = getResultSet(
                    "SELECT id, name, profilePicture FROM Users " +
                            "WHERE id IN (SELECT user1 FROM ChatRooms WHERE user2 = ?)" +
                            "OR id IN (SELECT user2 FROM ChatRooms WHERE user1 = ?);", userID, userID);

            int size = resultSet.size();
            List<Container.Profile> profiles = new ArrayList<>(size);

            for (int i = 0; i < size; i++) {
                int id = resultSet.getInteger(0, i);
                String name = resultSet.getString(1, i);
                SerializableImage profilePicture = resultSet.getCast(2, i);

                profiles.add(new Container.Profile(id, name, profilePicture));
            }

            return profiles;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}