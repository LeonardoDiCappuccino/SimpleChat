package SQLiteStuff;

import Communication.Container;
import Communication.SerializableImage;
import org.sqlite.SQLiteErrorCode;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChatDB extends DBConnector {

    //Make it a Singleton
    private static final ChatDB instance = new ChatDB();
    private static SerializableImage defaultProfilePicture;

    private ChatDB() {
        super("./rsc/Chat.db");
        try {
            defaultProfilePicture = new SerializableImage(ImageIO.read(
                    new File("./rsc/DefaultProfilePicture.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ChatDB getInstance() {
        return instance;
    }

    private static final int MESSAGE_LOAD_LIMIT = 10;

    public synchronized Container.Profile getUser(String username, String password) {
        try {
            DBResult resultSet = getResultSet(
                    "SELECT id, profilePicture FROM Users WHERE name = '" + username +
                            "' AND password = '" + password + "';");

            //User not found
            if (resultSet.isEmpty())
                return null;

            //Get user profile information. username already given as parameter
            int userID = resultSet.getInteger(0, 0);
            SerializableImage profilePicture = resultSet.getCast(1, 0);

            if(profilePicture == null)
                profilePicture = defaultProfilePicture;

            return new Container.Profile(userID, username, profilePicture);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized Container.Profile createUser(String username, String password) {
        try {
            executeStatement(
                    "INSERT INTO Users (name, password) VALUES (?, ?);", username, password);
        } catch (SQLException e) {
            if (e.getErrorCode() == SQLiteErrorCode.SQLITE_CONSTRAINT.code)
                return null;
            else
                throw new RuntimeException(e);
        }
        return getUser(username, password);
    }

    public synchronized ArrayList<Container.Profile> getProfile(int userID) {
        return null;
    }
}
