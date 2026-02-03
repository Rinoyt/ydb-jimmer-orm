package ydb.jimmer.dialect.model;

import org.babyfish.jimmer.sql.Entity;
import org.babyfish.jimmer.sql.ForeignKeyType;
import org.babyfish.jimmer.sql.GeneratedValue;
import org.babyfish.jimmer.sql.Id;
import org.babyfish.jimmer.sql.JoinColumn;
import org.babyfish.jimmer.sql.ManyToOne;
import org.babyfish.jimmer.sql.meta.UUIDIdGenerator;

import java.util.UUID;

@Entity
public interface Student {
    @Id
    @GeneratedValue(generatorType = UUIDIdGenerator.class)
    UUID id();


    String name();

    @ManyToOne
    @JoinColumn(foreignKeyType = ForeignKeyType.FAKE)
    Group group();
}
