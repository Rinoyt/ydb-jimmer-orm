package ydb.jimmer.dialect.model.type;

import org.babyfish.jimmer.sql.Column;
import org.babyfish.jimmer.sql.Entity;
import org.babyfish.jimmer.sql.Id;
import org.babyfish.jimmer.sql.Table;

import java.time.Duration;

@Entity
@Table(name = "ydb_interval64")
public interface YdbInterval64 {
    @Id
    @Column(name = "id")
    int getId();

    @Column(name = "value")
    Duration value();
}
