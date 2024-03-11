package SQLiteStuff;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class DBResult {

    /**
     * Placeholder for queue
     */
    public static final Object EMPTY = new Object();

    private final String[] attributes;
    private final DBType[] types;
    private final Object[][] dataSet;

    public DBResult(String[] attributes, int[] typeCodes, ArrayList<Queue<Object>> resultQueues) {
        this.attributes = new String[attributes.length];
        System.arraycopy(attributes, 0, this.attributes, 0, attributes.length);

        this.types = new DBType[typeCodes.length];
        for (int i = 0; i < typeCodes.length; i++)
            this.types[i] = DBType.valueOf(typeCodes[i]);

        //Copy result queues to result set
        dataSet = new Object[resultQueues.size()][resultQueues.getFirst().size()];
        for (int column = 0; column < dataSet.length; column++) {
            Queue<Object> queue = resultQueues.get(column);
            for (int row = 0; row < dataSet[column].length; row++) {
                Object item = queue.remove();
                if (item != EMPTY)
                    dataSet[column][row] = item;
            }
        }
    }

    public Integer getInteger(String attribute, int rowIndex) {
        return getInteger(getColumnIndex(attribute), rowIndex);
    }

    public Integer getInteger(int columnIndex, int rowIndex) {
        if (!types[columnIndex].equals(DBType.Integer))
            throw new ClassCastException("Attribute is not an Integer");

        return (Integer) dataSet[columnIndex][rowIndex];
    }

    public int[] getIntegers(String attribute) {
        return getIntegers(getColumnIndex(attribute));
    }

    public int[] getIntegers(int columnIndex) {
        int[] out = new int[dataSet[columnIndex].length];

        if (!types[columnIndex].equals(DBType.Integer))
            throw new ClassCastException("Attribute is not an Integer");

        for (int i = 0; i < out.length; i++)
            out[i] = (int) dataSet[columnIndex][i];

        return out;
    }

    public String getString(String attribute, int rowIndex) {
        return getString(getColumnIndex(attribute), rowIndex);
    }

    public String getString(int columnIndex, int rowIndex) {
        if (!types[columnIndex].equals(DBType.String))
            throw new ClassCastException("Attribute is not an String");

        return (String) dataSet[columnIndex][rowIndex];
    }

    public String[] getStrings(String attribute) {
        return getStrings(getColumnIndex(attribute));
    }

    public String[] getStrings(int columnIndex) {
        String[] out = new String[dataSet[columnIndex].length];

        if (!types[columnIndex].equals(DBType.String))
            throw new ClassCastException("Attribute is not an String");

        for (int i = 0; i < out.length; i++)
            out[i] = (String) dataSet[columnIndex][i];

        return out;
    }

    public byte[] getBytes(String attribute, int rowIndex) {
        return getBytes(getColumnIndex(attribute), rowIndex);
    }

    public byte[] getBytes(int columnIndex, int rowIndex) {
        return (byte[]) dataSet[columnIndex][rowIndex];
    }

    public List<byte[]> getBytesList(String attribute) {
        return getBytesList(getColumnIndex(attribute));
    }

    public List<byte[]> getBytesList(int columnIndex) {
        ArrayList<byte[]> out = new ArrayList<>(dataSet[columnIndex].length);

        for (int i = 0; i < out.reversed().size(); i++)
            out.set(i, (byte[]) dataSet[columnIndex][i]);

        return out;
    }

    public Float getFloat(String attribute, int rowIndex) {
        return getFloat(getColumnIndex(attribute), rowIndex);
    }

    public Float getFloat(int columnIndex, int rowIndex) {
        if (!types[columnIndex].equals(DBType.Float))
            throw new ClassCastException("Attribute is not an Float");

        return (Float) dataSet[columnIndex][rowIndex];
    }

    public float[] getFloats(String attribute) {
        return getFloats(getColumnIndex(attribute));
    }

    public float[] getFloats(int columnIndex) {
        float[] out = new float[dataSet[columnIndex].length];

        if (!types[columnIndex].equals(DBType.Float))
            throw new ClassCastException("Attribute is not an Float");

        for (int i = 0; i < out.length; i++)
            out[i] = (float) dataSet[columnIndex][i];

        return out;
    }

    public Double getDouble(String attribute, int rowIndex) {
        return getDouble(getColumnIndex(attribute), rowIndex);
    }

    public Double getDouble(int columnIndex, int rowIndex) {
        if (!types[columnIndex].equals(DBType.Double))
            throw new ClassCastException("Attribute is not an Double");

        return (Double) dataSet[columnIndex][rowIndex];
    }

    public double[] getDoubles(String attribute) {
        return getDoubles(getColumnIndex(attribute));
    }

    public double[] getDoubles(int columnIndex) {
        double[] out = new double[dataSet[columnIndex].length];

        if (!types[columnIndex].equals(DBType.Double))
            throw new ClassCastException("Attribute is not an Double");

        for (int i = 0; i < out.length; i++)
            out[i] = (double) dataSet[columnIndex][i];

        return out;
    }

    public <T> T getCast(String attribute, int rowIndex) {
        return getCast(getColumnIndex(attribute), rowIndex);
    }

    public <T> T getCast(int columnIndex, int rowIndex) {
        return (T) dataSet[columnIndex][rowIndex];
    }

    public final String[] getAttributes() {
        return attributes;
    }

    public DBType[] getTypes() {
        return types;
    }

    public Object[][] getDataSet() {
        return dataSet;
    }

    public int getColumnIndex(String attribute) {
        for (int i = 0; i < attributes.length; i++)
            if (attribute.equals(attributes[i]))
                return i;
        throw new IllegalArgumentException();
    }

    public DBType getType(String attribute) {
        return types[getColumnIndex(attribute)];
    }

    public boolean isEmpty() {
        return dataSet[0].length < 1;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        //Header
        int[] columnWidth = new int[attributes.length];
        int totalWidth = 0;

        for (int i = 0; i < attributes.length; i++) {
            String attributeInfo = attributes[i] + "[" + types[i] + "]   ";
            output.append(attributeInfo);
            columnWidth[i] = attributeInfo.length() - 3;
            totalWidth += attributeInfo.length();
        }
        output.append('\n');

        //Sub line
        output.append("-".repeat(Math.max(0, totalWidth)));
        output.append('\n');

        //Table
        for (int row = 0; row < dataSet[0].length; row++) {
            for (int column = 0; column < dataSet.length; column++) {

                String content = "NULL";
                if (dataSet[column][row] != null)
                    content = dataSet[column][row].toString();

                if (content.length() <= columnWidth[column]) {
                    output.append(content);

                    //Fill the gap between this and next column
                    output.append(" ".repeat(Math.max(0, columnWidth[column] - content.length())));
                } else {
                    //If content overflow
                    String subString = content.substring(0, columnWidth[column] - 3);
                    output.append(subString).append("...");
                }
                output.append("   ");
            }
            output.append('\n');
        }

        return output.toString();
    }
}