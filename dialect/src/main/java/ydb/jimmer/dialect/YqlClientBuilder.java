package ydb.jimmer.dialect;

import org.babyfish.jimmer.sql.JSqlClient;
import ydb.jimmer.dialect.scalar.DurationProvider;

public final class YqlClientBuilder {
    private YqlClientBuilder() {}

    public static JSqlClient.Builder getBuilder() {
        return JSqlClient.newBuilder()
                .setDialect(new YdbDialect())
                .addScalarProvider(new DurationProvider());
    }

    public static JSqlClient getYqlClient() {
        return getBuilder().build();
    }
}
