package ch.fge;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DbCopyService {

    private static final int BATCH_SIZE = 1000;

    private final JdbcTemplate sourceJdbcTemplate;
    private final JdbcTemplate targetJdbcTemplate;

    public DbCopyService(JdbcTemplate sourceJdbcTemplate, JdbcTemplate targetJdbcTemplate) {
        this.sourceJdbcTemplate = sourceJdbcTemplate;
        this.targetJdbcTemplate = targetJdbcTemplate;
    }

    public void clear(TableInfo tableInfo) {
        targetJdbcTemplate.execute(tableInfo.deleteSql());
    }

    public void copy(TableInfo tableInfo) {
        var data = new ArrayList<RowData>();
        sourceJdbcTemplate.query(tableInfo.selectSql(), rs -> {
            var columns = new ArrayList<RowData.Column>();
            for (int i = 1; i <= tableInfo.columns().size(); i++) {
                columns.add(new RowData.Column(i, rs.getObject(i)));
            }
            data.add(new RowData(columns));

            if (data.size() >= BATCH_SIZE) {
                write(tableInfo, data);
                data.clear();
            }
        });

        if (!data.isEmpty()) {
            write(tableInfo, data);
            data.clear();
        }
    }

    private void write(TableInfo tableInfo, List<RowData> data) {
        targetJdbcTemplate.batchUpdate(tableInfo.insertSql(), new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                var columns = data.get(i).columns();
                for (var column : columns) {
                    var value = column.value();
                    if (value instanceof Clob clob) {
                        ps.setClob(column.index(), clob.getCharacterStream());
                    } else if (value instanceof Blob blob) {
                        ps.setBlob(column.index(), blob.getBinaryStream());
                    } else {
                        ps.setObject(column.index(), value);
                    }
                }
            }

            @Override
            public int getBatchSize() {
                return data.size();
            }
        });
    }
}
