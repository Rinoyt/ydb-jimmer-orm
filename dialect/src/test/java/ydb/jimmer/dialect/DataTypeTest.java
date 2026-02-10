package ydb.jimmer.dialect;

import org.babyfish.jimmer.sql.ast.PropExpression;
import org.babyfish.jimmer.sql.ast.table.spi.AbstractTypedTable;
import org.junit.jupiter.api.Test;
import ydb.jimmer.dialect.model.type.YdbBoolTable;
import ydb.jimmer.dialect.model.type.YdbDate32Table;
import ydb.jimmer.dialect.model.type.YdbDateTable;
import ydb.jimmer.dialect.model.type.YdbDateTime64Table;
import ydb.jimmer.dialect.model.type.YdbDecimalTable;
import ydb.jimmer.dialect.model.type.YdbDoubleTable;
import ydb.jimmer.dialect.model.type.YdbFloatTable;
import ydb.jimmer.dialect.model.type.YdbInt16Table;
import ydb.jimmer.dialect.model.type.YdbInt32Table;
import ydb.jimmer.dialect.model.type.YdbInt64Table;
import ydb.jimmer.dialect.model.type.YdbInt8Table;
import ydb.jimmer.dialect.model.type.YdbStringTable;
import ydb.jimmer.dialect.model.type.YdbTimestamp64Table;
import ydb.jimmer.dialect.model.type.YdbTimestampTable;
import ydb.jimmer.dialect.model.type.YdbUuidTable;

public class DataTypeTest extends YdbTest {
    private void createTable(String tableName, String typeName) {
        executeSql(
                "CREATE TABLE " + tableName + "(" +
                        "id Int8," +
                        "value " + typeName + "," +
                        "PRIMARY KEY (id)" +
                        ")");
    }

    private void insert(String tableName, String... values) {
        if (values.length == 0) {
            return;
        }
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (id, value) VALUES ");
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                sql.append(", ");
            }
            sql.append("(").append(i).append(", ").append(values[i]).append(")");
        }
        executeSql(sql.toString());
    }

    /**
     *
     * @param typeName
     * @param table
     * @param prop
     * @param valuesToInsert
     * @param expectedValues expected and sorted (ASC) return values of the query
     */
    private void typeTest(String tableName,
                          String typeName,
                          AbstractTypedTable<?> table,
                          PropExpression<?> prop,
                          String[] valuesToInsert,
                          String[] expectedValues) {
        createTable(tableName, typeName);

        insert(tableName, valuesToInsert);

        StringBuilder json = new StringBuilder("[");
        for (int i  = 0; i < valuesToInsert.length; i++) {
            if (json.length() != 1) {
                json.append(",");
            }
            json.append("{");
            json.append("\"id\":").append(i).append(",\"value\":").append(expectedValues[i]);
            json.append("}");
        }
        json.append("]");

        executeAndExpect(
                getYqlClient()
                        .createQuery(table)
                        .orderBy(prop)
                        .select(table),
                cxt -> {
                    cxt.sql(
                            "select tb_1_.id, tb_1_.value from " + tableName + " tb_1_ order by tb_1_.value asc");
                    cxt.rows(json.toString());
                }
        );
    }

    @Test
    public void boolTest() {
        String[] valuesToInsert = new String[]{"false", "true"};
        String[] expectedValues = new String[]{"false", "true"};
        typeTest("ydb_test", "Bool",
                YdbBoolTable.$, YdbBoolTable.$.value(),
                valuesToInsert, expectedValues);
    }

    @Test
    public void dateTest() {
        String[] valuesToInsert = new String[]{"Date(\"2000-01-01\")", "DATE(\"2017-11-27\")"};
        String[] expectedValues = new String[]{"\"2000-01-01\"", "\"2017-11-27\""};
        typeTest("ydb_date", "Date",
                YdbDateTable.$, YdbDateTable.$.value(),
                valuesToInsert, expectedValues);
    }

    @Test
    public void date32Test() {
        String[] valuesToInsert = new String[]{"Date32(\"144169-01-01\")"};
        String[] expectedValues = new String[]{"\"+144169-01-01\""};
        typeTest("ydb_date32", "Date32",
                YdbDate32Table.$, YdbDate32Table.$.value(),
                valuesToInsert, expectedValues);
    }

    @Test
    public void dateTime64Test() {
        String[] valuesToInsert = new String[]{"DateTime64(\"2017-11-27T13:24:00Z\")"};
        String[] expectedValues = new String[]{"\"2017-11-27T13:24:00\""};
        typeTest("ydb_dateTime64", "DateTime64",
                YdbDateTime64Table.$, YdbDateTime64Table.$.value(),
                valuesToInsert, expectedValues);
    }

    @Test
    public void decimalTest() {
        String[] valuesToInsert = new String[]{"Decimal(\"1.23\", 5, 2)"};
        String[] expectedValues = new String[]{"1.23"};
        typeTest("ydb_decimal", "Decimal(5, 2)",
                YdbDecimalTable.$, YdbDecimalTable.$.value(),
                valuesToInsert, expectedValues);
    }

    @Test
    public void doubleTest() {
        String[] valuesToInsert = new String[]{"Double(\"1.23\")"};
        String[] expectedValues = new String[]{"1.23"};
        typeTest("ydb_double", "Double",
                YdbDoubleTable.$, YdbDoubleTable.$.value(),
                valuesToInsert, expectedValues);
    }

    @Test
    public void floatTest() {
        String[] valuesToInsert = new String[]{"Float(\"1.23\")"};
        String[] expectedValues = new String[]{"1.23"};
        typeTest("ydb_float", "Float",
                YdbFloatTable.$, YdbFloatTable.$.value(),
                valuesToInsert, expectedValues);
    }

    @Test
    public void int8Test() {
        String[] valuesToInsert = new String[]{"-1", "0", "10"};
        String[] expectedValues = new String[]{"-1", "0", "10"};
        typeTest("ydb_int8", "Int8",
                YdbInt8Table.$, YdbInt8Table.$.value(),
                valuesToInsert, expectedValues);
    }

    @Test
    public void int16Test() {
        String[] valuesToInsert = new String[]{"-1", "0", "10"};
        String[] expectedValues = new String[]{"-1", "0", "10"};
        typeTest("ydb_int16", "Int16",
                YdbInt16Table.$, YdbInt16Table.$.value(),
                valuesToInsert, expectedValues);
    }

    @Test
    public void int32Test() {
        String[] valuesToInsert = new String[]{"-1", "0", "10"};
        String[] expectedValues = new String[]{"-1", "0", "10"};
        typeTest("ydb_int32", "Int32",
                YdbInt32Table.$, YdbInt32Table.$.value(),
                valuesToInsert, expectedValues);
    }

    @Test
    public void int64Test() {
        String[] valuesToInsert = new String[]{"-1", "0", "10"};
        String[] expectedValues = new String[]{"-1", "0", "10"};
        typeTest("ydb_int64", "Int64",
                YdbInt64Table.$, YdbInt64Table.$.value(),
                valuesToInsert, expectedValues);
    }

    @Test
    public void stringTest() {
        String[] valuesToInsert = new String[]{"\"0\"", "\"string\""};
        String[] expectedValues = new String[]{"\"0\"", "\"string\""};
        typeTest("ydb_string", "String",
                YdbStringTable.$, YdbStringTable.$.value(),
                valuesToInsert, expectedValues);
    }

    @Test
    public void timestampTest() {
        String[] valuesToInsert = new String[]{"Timestamp(\"2017-11-27T13:24:00.123456Z\")"};
        String[] expectedValues = new String[]{"\"2017-11-27T13:24:00.123456\""};
        typeTest("ydb_timestamp", "Timestamp",
                YdbTimestampTable.$, YdbTimestampTable.$.value(),
                valuesToInsert, expectedValues);
    }

    @Test
    public void timestamp64Test() {
        String[] valuesToInsert = new String[]{"Timestamp64(\"2017-11-27T13:24:00.123456Z\")"};
        String[] expectedValues = new String[]{"\"2017-11-27T13:24:00.123456\""};
        typeTest("ydb_timestamp64", "Timestamp64",
                YdbTimestamp64Table.$, YdbTimestamp64Table.$.value(),
                valuesToInsert, expectedValues);
    }

    @Test
    public void uuidTest() {
        String[] valuesToInsert = new String[]{
                "Uuid(\"9e197d65-1914-4d57-a65f-77a52a06baa7\")",
                "Uuid(\"8e0f2cf4-4656-4d73-970e-a18be9ead78b\")"};
        String[] expectedValues = new String[]{
                "\"9e197d65-1914-4d57-a65f-77a52a06baa7\"",
                "\"8e0f2cf4-4656-4d73-970e-a18be9ead78b\""};
        typeTest("ydb_uuid", "Uuid",
                YdbUuidTable.$, YdbUuidTable.$.value(),
                valuesToInsert, expectedValues);
    }
}
