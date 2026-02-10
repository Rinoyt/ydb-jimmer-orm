package ydb.jimmer.dialect.model.type;

import org.babyfish.jimmer.sql.Column;
import org.babyfish.jimmer.sql.Entity;
import org.babyfish.jimmer.sql.Id;
import org.babyfish.jimmer.sql.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "ydb_dateTime64")
public interface YdbDateTime64 {
    @Id
    @Column(name = "id")
    int getId();

    @Column(name = "value")
    LocalDateTime value();
}
