package ydb.jimmer.dialect.model.type;

import org.babyfish.jimmer.sql.Column;
import org.babyfish.jimmer.sql.Entity;
import org.babyfish.jimmer.sql.Id;
import org.babyfish.jimmer.sql.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "ydb_decimal")
public interface YdbDecimal {
    @Id
    @Column(name = "id")
    int getId();

    @Column(name = "value")
    BigDecimal value();
}
