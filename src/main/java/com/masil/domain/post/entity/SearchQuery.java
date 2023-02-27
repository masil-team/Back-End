package com.masil.domain.post.entity;

import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

public class SearchQuery {

    private static final Pattern SPECIAL_CHARS = Pattern.compile("\\[‘”-#@;=*/+]");
    private static final Set<String> STRING_SET = Set.of("update", "select", "delete", "insert");

    private final String value;

    public SearchQuery(String value) {
        this.value = toSafeQuery(value);
    }

    private String toSafeQuery(String query) {
        if (query != null) {
            query = changeNoSqlInjection(query.toLowerCase(Locale.ROOT));
        }
        if (query == null) {
            query = "";
        }
        return query;
    }

    /**
     * 댓글에서 DB Injection 방어하기 위하여 구현
     */
    private String changeNoSqlInjection(String query) {
        query = SPECIAL_CHARS.matcher(query).replaceAll("");
        for (String s : STRING_SET) {
            if (query.contains(s)) {
                return "";
            }
        }
        return query;
    }

    public String getValue() {
        return value;
    }
}
