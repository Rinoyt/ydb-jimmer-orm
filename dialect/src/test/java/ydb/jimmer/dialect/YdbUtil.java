package ydb.jimmer.dialect;

import org.babyfish.jimmer.sql.ast.query.TypedRootQuery;
import org.junit.jupiter.api.Assertions;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import tech.ydb.jdbc.YdbDriver;

import javax.sql.DataSource;
import java.net.URL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.List;

public final class YdbUtil {
    private static final Driver DRIVER = new YdbDriver();
    private static final String URL = "jdbc:ydb:grpc://localhost:2136/local";

    public static final DataSource YDB_DATA_SOURCE;

    static {
        YDB_DATA_SOURCE = new SimpleDriverDataSource(
                DRIVER,
                URL
        );
    }

    private YdbUtil() {
    }

    public static void initDatabase() {
        try (Connection connection = YDB_DATA_SOURCE.getConnection()) {
            URL url = YdbUtil.class.getClassLoader().getResource("database-ydb.sql");
            if (url == null) {
                throw new IllegalStateException("Cannot load 'database-ydb.sql'");
            }
            ScriptUtils.executeSqlScript(
                    connection,
                    new EncodedResource(new UrlResource(url)),
                    false,
                    false,
                    "--",
                    ";",
                    "/*",
                    "*/");
        } catch (SQLException e) {
            Assertions.fail("Database threw an exception: " + e.getMessage());
        }
    }

    public static <R> List<R> connectAndExecute(boolean rollback, TypedRootQuery<R> query) {
        try (Connection connection = YDB_DATA_SOURCE.getConnection()) {
            connection.setAutoCommit(!rollback);
            try {
                return query.execute(connection);
            } finally {
                if (rollback) {
                    connection.rollback();
                }
            }
        } catch (SQLException e) {
            Assertions.fail("Database threw an exception: " + e.getMessage());
        }

        return null;
    }
}
