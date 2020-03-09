package aware.database;

import aware.database.util.TestcontainersDataSource;
import aware.database.util.settings.DataSourceSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import javax.sql.DataSource;

import aware.database.util.entity.DatabaseType;
import static aware.database.util.settings.DataSourceSettingsBuilder.aDataSourceSettings;

public class TestDatabaseConfiguration {
    @Bean
    public DataSource dataSource() {
        DataSourceSettings settings = aDataSourceSettings()
                .setDatabaseType(DatabaseType.POSTGRES)
                .addLiquibaseChangeLogPath("src/main/resources/liquibase/master.changelog.xml")
                .build();
        return TestcontainersDataSource.getOrBuildDataSource(settings);
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}