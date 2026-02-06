package ydb.jimmer.dialect;

import org.junit.jupiter.api.Assertions;
import ydb.jimmer.dialect.sqlMonitor.QueryLog;

import java.util.List;

public class QueryTestContext {
    private final List<QueryLog> logs;
    private final List<?> rows;

    private int index = 0;

    public QueryTestContext(List<QueryLog> logs, List<?> rows) {
        this.logs = logs;
        this.rows = rows;
    }

    public void nextStatement() {
        index++;
    }

    public void sql(String sql) {
        Assertions.assertEquals(
                sql.replace("--->", ""),
                logs.get(index).getSql(),
                "statements[" + index + "].sql");
    }
}
