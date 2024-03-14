package Communication;

import java.io.Serializable;

public class Container {
    public record Profile(int userID, String username, SerializableImage profilePicture) implements Serializable {
    }


}
