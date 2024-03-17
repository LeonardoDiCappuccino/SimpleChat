package Communication;

import java.io.Serializable;

public class Container {

    public record LogInData(String username, String password) implements Serializable {
    }

    public record SignInData(String username, String password, SerializableImage profilePicture) implements Serializable {
    }

    public record Profile(int userID, String username, SerializableImage profilePicture) implements Serializable {
    }


}
