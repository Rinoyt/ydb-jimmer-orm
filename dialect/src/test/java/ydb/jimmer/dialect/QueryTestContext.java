package ydb.jimmer.dialect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.babyfish.jimmer.jackson.ImmutableModule;
import org.junit.jupiter.api.Assertions;
import ydb.jimmer.dialect.sqlMonitor.QueryLog;

import java.text.SimpleDateFormat;
import java.util.List;

public class QueryTestContext {
    private static final ObjectMapper MAPPER = new ObjectMapper()
                    .registerModule(new ImmutableModule())
                    .registerModule(new JavaTimeModule());

    static {
        MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    }

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

    public void rows(String json) {
        try {
            Assertions.assertEquals(
                    json.replace("--->", ""),
                    MAPPER.writeValueAsString(rows)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
