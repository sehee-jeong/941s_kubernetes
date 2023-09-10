package hanium.project941s.domain.Enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ServiceStatus {

    SUCCESS("SUCCESS", "성공"),
    FAILURE("FAILURE", "실패");

    private final String key;
    private final String title;
}
