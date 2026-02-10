package ydb.jimmer.dialect.model.type;

import org.babyfish.jimmer.sql.Column;
import org.babyfish.jimmer.sql.Entity;
import org.babyfish.jimmer.sql.Id;
import org.babyfish.jimmer.sql.Table;

@Entity
@Table(name = "ydb_int8")
public interface YdbInt8 {
    @Id
    @Column(name = "id")
    int getId();

    @Column(name = "value")
    byte value();
}
