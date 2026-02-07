package ydb.jimmer.dialect;

import org.babyfish.jimmer.sql.ast.impl.Ast;
import org.babyfish.jimmer.sql.ast.impl.Literals;
import org.babyfish.jimmer.sql.ast.impl.query.ForUpdate;
import org.babyfish.jimmer.sql.ast.impl.render.AbstractSqlBuilder;
import org.babyfish.jimmer.sql.dialect.DefaultDialect;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.UUID;

public class YdbDialect extends DefaultDialect {
    @Override
    public String sqlType(Class<?> elementType) {
        if (elementType == String.class) {
            return "String";
        } else if (elementType == UUID.class) {
            return "Uuid";
        } else if (elementType == boolean.class) {
            return "Bool";
        } else if (elementType == byte.class) {
            return "Int8";
        } else if (elementType == short.class) {
            return "Int16";
        } else if (elementType == int.class) {
            return "Int32";
        } else if (elementType == long.class) {
            return "Int64";
        } else if (elementType == float.class) {
            return "Float";
        } else if (elementType == double.class) {
            return "Double";
        } else if (elementType == BigDecimal.class) {
            return "Decimal(35, 35)";
        } else if (elementType == Date.class) {
            return "Date";
        } else if (elementType == LocalDate.class) {
            return "Date32";
        } else if (elementType == Time.class || elementType == OffsetTime.class) {
            return "Timestamp";
        } else if (elementType == LocalTime.class
                || elementType == java.util.Date.class
                || elementType == Timestamp.class) {
            return "Timestamp64";
        } else if (elementType == LocalDateTime.class || elementType == OffsetDateTime.class) {
            return "DateTime64";
        } else if (elementType == ZonedDateTime.class) {
            return "TzDateTime64";
        } else {
            return null;
        }
    }

    @Override
    public boolean isDeleteAliasSupported() {
        return false;
    }

    @Override
    public boolean isUpdateAliasSupported() {
        return false;
    }

    @Override
    public boolean isArraySupported() {
        return true;
    }

    @Override
    public <T> T[] getArray(ResultSet rs, int col, Class<T[]> arrayType) throws SQLException {
        return rs.getObject(col, arrayType);
    }

    @Override
    public boolean isTableOfSubQueryMutable() {
        return false;
    }

    @Override
    public Class<?> getJsonBaseType() {
        return JSONObject.class;
    }

    @Override
    public @Nullable Object jsonToBaseValue(@Nullable String json) {
        return json == null ? new JSONObject() : new JSONObject(json);
    }

    @Override
    public @Nullable String baseValueToJson(@Nullable Object baseValue) {
        return baseValue == null ? null : baseValue.toString();
    }

    @Override
    public boolean isForeignKeySupported() {
        return false;
    }

    @Override
    public boolean isInsertedIdReturningRequired() {
        return true;
    }

    @Override
    public boolean isExplicitBatchRequired() {
        return true;
    }

    @Override
    public boolean isBatchDumb() {
        return true;
    }

    @Override
    public boolean isUpsertSupported() {
        return true;
    }

    @Override
    public boolean isNoIdUpsertSupported() {
        return false;
    }

    @Override
    public boolean isUpsertWithOptimisticLockSupported() {
        return true;
    }

    @Override
    public boolean isUpsertWithNullableKeySupported() {
        return true;
    }

    @Override
    public boolean isTransactionAbortedByError() {
        return true;
    }

    @Override
    public void update(UpdateContext ctx) {
        ctx.sql("UPDATE ")
                .appendTableName()
                .enter(AbstractSqlBuilder.ScopeType.SET)
                .appendAssignments()
                .leave()
                .enter(AbstractSqlBuilder.ScopeType.WHERE)
                .appendPredicates()
                .leave()
                .sql(" RETURNING ")
                .appendId();
    }

    @Override
    public void upsert(UpsertContext ctx) {
        if (ctx.isUpdateIgnored() || !ctx.hasUpdatedColumns()) {
            ctx.sql("INSERT INTO ")
                    .appendTableName()
                    .enter(AbstractSqlBuilder.ScopeType.MULTIPLE_LINE_TUPLE)
                    .appendInsertedColumns("")
                    .leave()
                    .sql("VALUES ")
                    .enter(AbstractSqlBuilder.ScopeType.MULTIPLE_LINE_TUPLE)
                    .appendInsertingValues()
                    .leave();
        } else {
            ctx.sql("UPSERT INTO ")
                    .appendTableName()
                    .enter(AbstractSqlBuilder.ScopeType.MULTIPLE_LINE_TUPLE)
                    .appendInsertedColumns("")
                    .leave()
                    .sql("VALUES ")
                    .enter(AbstractSqlBuilder.ScopeType.MULTIPLE_LINE_TUPLE)
                    .appendInsertingValues()
                    .leave();
        }
    }

    @Override
    public String transCacheOperatorTableDDL() {
        return "CREATE TABLE JIMMER_TRANS_CACHE_OPERATOR(\n" +
                "\tID Uuid NOT NULL,\n" +
                "\tIMMUTABLE_TYPE String,\n" +
                "\tIMMUTABLE_PROP String,\n" +
                "\tCACHE_KEY String NOT NULL,\n" +
                "\tREASON String,\n" +
                "\tPRIMARY KEY(ID)\n" +
                ")";
    }

    @Override
    public void renderLPad(AbstractSqlBuilder<?> builder, int currentPrecedence, Ast expression, Ast length, Ast padString) {
        throw new UnsupportedOperationException(
                "The current dialect \"" + getClass().getName() + "\" does not support LPad."
        );
    }

    @Override
    public void renderRPad(AbstractSqlBuilder<?> builder, int currentPrecedence, Ast expression, Ast length, Ast padString) {
        throw new UnsupportedOperationException(
                "The current dialect \"" + getClass().getName() + "\" does not support RPad."
        );
    }

    @Override
    public void renderPosition(AbstractSqlBuilder<?> builder, int currentPrecedence, Ast subStrAst, Ast expressionAst, @Nullable Ast startAst) {
        builder.sql("FIND(")
                .ast(expressionAst, currentPrecedence)
                .sql(", ")
                .ast(subStrAst, currentPrecedence);
        if (startAst != null) {
            builder.sql(", ")
                    .ast(startAst, currentPrecedence);
        }
        builder.sql(")");
    }

    @Override
    public void renderLeft(AbstractSqlBuilder<?> builder, int currentPrecedence, Ast expressionAst, Ast lengthAst) {
        renderSubString(builder, currentPrecedence, expressionAst, (Ast) Literals.number(0), lengthAst);
    }

    @Override
    public void renderRight(AbstractSqlBuilder<?> builder, int currentPrecedence, Ast expressionAst, Ast lengthAst) {
        builder.sql("substring(")
                .ast(expressionAst, currentPrecedence)
                .sql(", (LENGTH(")
                .ast(expressionAst, currentPrecedence)
                .sql(") - ")
                .ast(lengthAst, currentPrecedence)
                .sql("))");
    }

    @Override
    public void renderForUpdate(AbstractSqlBuilder<?> builder, ForUpdate forUpdate) {
        throw new UnsupportedOperationException(
                "The current dialect \"" + getClass().getName() + "\" does not support 'for update' hint."
        );
    }
}
