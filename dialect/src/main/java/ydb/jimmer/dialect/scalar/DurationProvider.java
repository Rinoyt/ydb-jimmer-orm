package ydb.jimmer.dialect.scalar;

import org.babyfish.jimmer.sql.runtime.AbstractScalarProvider;
import org.babyfish.jimmer.sql.runtime.Reader;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;

public class DurationProvider extends AbstractScalarProvider<Duration, Duration> {
    public DurationProvider() {
        super(Duration.class, Duration.class);
    }

    @Override
    public Duration toScalar(@NotNull Duration sqlValue) {
        return sqlValue;
    }

    @Override
    public Duration toSql(@NotNull Duration scalarValue) {
        return scalarValue;
    }

    @Override
    public Reader<Duration> reader() {
        return new DurationReader();
    }

    private static class DurationReader implements Reader<Duration> {
        @Override
        public Duration read(ResultSet rs, Context ctx) throws SQLException {
            return rs.getObject(ctx.col(), Duration.class);
        }

        @Override
        public void skip(Context ctx) {
            ctx.col();
        }
    }
}
