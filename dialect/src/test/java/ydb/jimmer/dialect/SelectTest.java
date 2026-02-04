package ydb.jimmer.dialect;

import org.junit.jupiter.api.Test;
import ydb.jimmer.dialect.model.StudentTable;

import java.sql.SQLException;

public class SelectTest extends YdbTest {
    @Test
    public void OneEntityTest() throws SQLException {
        initDatabase();

        StudentTable table = StudentTable.$;


    }
}
