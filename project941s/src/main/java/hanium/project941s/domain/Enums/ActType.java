package hanium.project941s.domain.Enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActType {

    CREATE("CREATE", "생성"),
    UPDATE("UPDATE", "갱신"),
    DELETE("DELETE", "삭제");

    private final String key;
    private final String title;
}
