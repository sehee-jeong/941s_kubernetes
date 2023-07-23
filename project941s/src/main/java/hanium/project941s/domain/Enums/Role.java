package hanium.project941s.domain.Enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ADMIN("ROLE_ADMIN", "관리자"),
    USER("ROLE_USER", "고객");

    private final String key;
    private final String title;
}
