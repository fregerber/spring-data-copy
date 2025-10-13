package ch.fge;

import java.util.List;

public record RowData(List<Column> columns) {
    public record Column(int index, Object value) {
    }
}
