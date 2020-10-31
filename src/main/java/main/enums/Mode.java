package main.enums;

import java.util.HashMap;
import java.util.Map;

public enum Mode {
    /**
     * сортировать по дате публикации, выводить сначала новые
     */
    RECENT("recent", "time", "DESC"),
    /**
     * сортировать по убыванию количества комментариев
     */
    POPULAR("popular", null, null),
    /**
     * сортировать по убыванию количества лайков
     */
    BEST("best", null, null),
    /**
     * сортировать по дате публикации, выводить сначала старые
     */
    EARLY("early", "time", "ASC");

    /**
     * Параметр типа сортировки с фронта
     */
    private String value;

    /**
     * Значение по которому сортируем
     */
    private String sortBy;

    /**
     * Параметр типа сортировки
     */
    private String sortDirection;

    /**
     * Карта для получения экземпляра Mode Mode#value
     */
    private static final Map<String, Mode> lookup = new HashMap<>();

    static {
        for (Mode mode : Mode.values()) {
            lookup.put(mode.getValue(), mode);
        }
    }

    Mode(String value, String sortBy, String sortDirection) {
        this.value = value;
        this.sortBy = sortBy;
        this.sortDirection = sortDirection;
    }

    public String getValue() {
        return value;
    }

    public String getSortBy() {
        return sortBy;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public static Map<String, Mode> getLookup() {
        return lookup;
    }
}
