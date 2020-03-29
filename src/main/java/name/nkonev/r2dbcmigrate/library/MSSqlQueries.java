package name.nkonev.r2dbcmigrate.library;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.Statement;

import java.util.Arrays;
import java.util.List;

public class MSSqlQueries implements SqlQueries {

    @Override
    public List<String> createInternalTables() {
        return Arrays.asList("if not exists (select * from sysobjects where name='migrations' and xtype='U') create table migrations (id int primary key, description text)");
    }

    @Override
    public String getMaxMigration() {
        return "select max(id) as max from migrations";
    }

    public String insertMigration() {
        return "insert into migrations(id, description) values (@id, @descr)";
    }

    @Override
    public Statement createInsertMigrationStatement(Connection connection, FilenameParser.MigrationInfo migrationInfo) {
        return connection
                .createStatement(insertMigration())
                .bind("@id", migrationInfo.getVersion())
                .bind("@descr", migrationInfo.getDescription());
    }
}
