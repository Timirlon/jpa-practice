package model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {
    ADMIN("Администратор"),
    MODERATOR("Модератор"),
    NO_NAME("Гость");

    private final String displayName;
}
