package SQLiteStuff;

@SuppressWarnings("unused")
public enum DBType {

    Integer(4),
    String(12),
    Bytes(2004),
    Float(7),
    Double(2);

    public final int code;

    DBType(int code) {
        this.code = code;
    }

    public static DBType valueOf(int code) {
        for (DBType type : values())
            if (type.code == code)
                return type;
        return null;
    }
}
