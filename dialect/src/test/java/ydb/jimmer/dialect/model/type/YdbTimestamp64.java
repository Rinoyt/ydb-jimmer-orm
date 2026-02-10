package ydb.jimmer.dialect.model.type;

import org.babyfish.jimmer.sql.Column;
import org.babyfish.jimmer.sql.Entity;
import org.babyfish.jimmer.sql.Id;
import org.babyfish.jimmer.sql.Table;

import java.time.LocalTime;

@Entity
@Table(name = "ydb_timestamp64")
public interface YdbTimestamp64 {
    @Id
    @Column(name = "id")
    int getId();

    @Column(name = "value")
    LocalTime value();
}
