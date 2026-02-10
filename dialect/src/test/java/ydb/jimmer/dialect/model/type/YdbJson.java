package ydb.jimmer.dialect.model.type;

import org.babyfish.jimmer.sql.Column;
import org.babyfish.jimmer.sql.Entity;
import org.babyfish.jimmer.sql.Id;
import org.babyfish.jimmer.sql.Table;
import org.json.JSONObject;

@Entity
@Table(name = "ydb_json")
public interface YdbJson {
    @Id
    @Column(name = "id")
    int getId();

    @Column(name = "value")
    JSONObject value();
}
