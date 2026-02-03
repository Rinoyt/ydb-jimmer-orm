package ydb.jimmer.dialect;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import tech.ydb.test.junit5.YdbHelperExtension;

import static ydb.jimmer.dialect.YdbUtil.initDatabase;

public class QueryTest {
    @RegisterExtension
    private static final YdbHelperExtension ydb = new YdbHelperExtension();

    @Test
    public void OneEntityTest() {
        initDatabase();
    }
}
