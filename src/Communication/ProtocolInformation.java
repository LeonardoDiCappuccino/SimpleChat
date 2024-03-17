package Communication;

public enum ProtocolInformation {
    //Requests
    LogIn, SignIn, LogOut,

    //Errors
    InvalidLogInData, UsernameTaken, IllegalArgument;
}
