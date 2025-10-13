package ch.fge;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DbInfoService {

    private final JdbcTemplate sourceJdbcTemplate;
    private final JdbcTemplate targetJdbcTemplate;

    public DbInfoService(JdbcTemplate sourceJdbcTemplate, JdbcTemplate targetJdbcTemplate) {
        this.sourceJdbcTemplate = sourceJdbcTemplate;
        this.targetJdbcTemplate = targetJdbcTemplate;
    }

    public void connect() {
        ping(sourceJdbcTemplate);
        ping(targetJdbcTemplate);
    }

    public List<String> getTableNames() {
        return queryTableNames(targetJdbcTemplate);
    }

    public TableInfo getTableInfo(String tableName) {
        return queryTableInfo(targetJdbcTemplate, tableName);
    }

    public Long getRowCount(String tableName) {
        return queryRowCount(sourceJdbcTemplate, tableName);
    }

    private void ping(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.queryForObject("select 1", Boolean.class);
    }

    private List<String> queryTableNames(JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.queryForList("select table_name from information_schema.tables", String.class);
    }

    private TableInfo queryTableInfo(JdbcTemplate jdbcTemplate, String tableName) {
        return new TableInfo(tableName, jdbcTemplate.query("""
                select column_name, data_type, character_maximum_length, numeric_precision, numeric_scale, is_nullable
                from information_schema.columns
                where table_name = ?
                order by ordinal_position;
                """, (rs, rowNum) -> new TableInfo.Column(
                rs.getString(1),
                rs.getString(2),
                rs.getObject(3, Integer.class),
                rs.getObject(4, Integer.class),
                rs.getObject(5, Integer.class),
                rs.getString(6)), tableName));
    }

    private Long queryRowCount(JdbcTemplate jdbcTemplate, String tableName) {
        return jdbcTemplate.queryForObject("select count(*) from " + tableName, Long.class);
    }
}
