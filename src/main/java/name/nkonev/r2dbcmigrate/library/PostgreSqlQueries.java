package name.nkonev.r2dbcmigrate.library;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.Statement;

import java.util.Arrays;
import java.util.List;

public class PostgreSqlQueries implements SqlQueries {

    @Override
    public List<String> createInternalTables() {
        return Arrays.asList("create table if not exists migrations (id int primary key, description text)");
    }

    @Override
    public String getMaxMigration() {
        return "select max(id) from migrations";
    }

    public String insertMigration() {
        return "insert into migrations(id, description) values ($1, $2)";
    }

    @Override
    public Statement createInsertMigrationStatement(Connection connection, FilenameParser.MigrationInfo migrationInfo) {
        return connection
                .createStatement(insertMigration())
                .bind("$1", migrationInfo.getVersion())
                .bind("$2", migrationInfo.getDescription());
    }
}
