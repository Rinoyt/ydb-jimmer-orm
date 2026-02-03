package ydb.jimmer.dialect.sqlMonitor;

import org.babyfish.jimmer.sql.runtime.ExecutionPurpose;

import java.util.Collections;
import java.util.List;

public class ExecutorLog {
    private final String sql;
    private final ExecutionPurpose purpose;
    private final List<List<Object>> variablesList;

    public ExecutorLog(String sql, ExecutionPurpose purpose, List<List<Object>> variablesList) {
        this.sql = sql;
        this.purpose = purpose;
        this.variablesList = variablesList;
    }

    public static ExecutorLog simple(String sql, ExecutionPurpose purpose, List<Object> variables) {
        return new ExecutorLog(sql, purpose, Collections.singletonList(variables));
    }
}
