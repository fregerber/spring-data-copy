package ch.fge;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public record TableInfo(String name, List<Column> columns) {

    public String deleteSql() {
        return "delete from " + name;
    }

    public String countSql() {
        return "select count(*) from " + name;
    }

    public String selectSql() {
        return "select " + columns.stream().map(Column::name).collect(Collectors.joining(",")) + " from " + name;
    }

    public String insertSql() {
        return "insert into " + name + " ("
                + columns.stream().map(Column::name).collect(Collectors.joining(","))
                + " ) values (" + String.join(",", Collections.nCopies(columns.size(), "?")) + ")";
    }

    public record Column(String name, String type, Integer chars, Integer precision, Integer scale, String nullable) {
    }
}
