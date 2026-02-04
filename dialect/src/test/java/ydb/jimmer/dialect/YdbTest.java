package ydb.jimmer.dialect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import tech.ydb.test.junit5.YdbHelperExtension;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class YdbTest {
    @RegisterExtension
    private static final YdbHelperExtension ydb = new YdbHelperExtension();

    public void initDatabase() {
        try (Connection connection = DriverManager.getConnection(getJdbcURL())) {
            URL dropTablesUrl = YdbTest.class.getClassLoader().getResource("database-drop-tables-ydb.sql");
            if (dropTablesUrl == null) {
                throw new IllegalStateException("Cannot load 'database-drop-tables-ydb.sql'");
            }
            try {
                executeYqlScript(connection, dropTablesUrl);
            } catch (ScriptException _) {
                //
            }

            URL url = YdbTest.class.getClassLoader().getResource("database-ydb.sql");
            if (url == null) {
                throw new IllegalStateException("Cannot load 'database-ydb.sql'");
            }
            executeYqlScript(connection, url);
            try (PreparedStatement select = connection
                    .prepareStatement("select count(1) as cnt from group")) {
                ResultSet rs = select.executeQuery();
                rs.next();
                Assertions.assertEquals(0, rs.getLong("cnt"));
            }
        } catch (SQLException e) {
            Assertions.fail("Database threw an exception: " + e.getMessage());
        }
    }

    private void executeYqlScript(Connection connection, URL url) throws ScriptException {
        ScriptUtils.executeSqlScript(
                connection,
                new EncodedResource(new UrlResource(url)),
                false,
                false,
                "--",
                ";",
                "/*",
                "*/");
    }

    private String getJdbcURL() {
        StringBuilder jdbc = new StringBuilder("jdbc:ydb:")
                .append(ydb.useTls() ? "grpcs://" : "grpc://")
                .append(ydb.endpoint())
                .append("/")
                .append(ydb.database());

        if (ydb.authToken() != null) {
            jdbc.append("?").append("token=").append(ydb.authToken());
        }

        return jdbc.toString();
    }

//    public static <R> List<R> connectAndExecute(boolean rollback, TypedRootQuery<R> query) {
//        try (Connection connection = YDB_DATA_SOURCE.getConnection()) {
//            connection.setAutoCommit(!rollback);
//            try {
//                return query.execute(connection);
//            } finally {
//                if (rollback) {
//                    connection.rollback();
//                }
//            }
//        } catch (SQLException e) {
//            Assertions.fail("Database threw an exception: " + e.getMessage());
//        }
//
//        return null;
//    }
}
