package ch.fge;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class ApplicationConfig {

    @Bean
    public JdbcTemplate sourceJdbcTemplate(DataSource sourceDataSource) {
        return new JdbcTemplate(sourceDataSource);
    }

    @Bean
    public JdbcTemplate targetJdbcTemplate(DataSource targetDataSource) {
        return new JdbcTemplate(targetDataSource);
    }

    @Bean
    public DataSource sourceDataSource() {
        return sourceDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean
    public DataSource targetDataSource() {
        return targetDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.source")
    protected DataSourceProperties sourceDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.target")
    protected DataSourceProperties targetDataSourceProperties() {
        return new DataSourceProperties();
    }
}
