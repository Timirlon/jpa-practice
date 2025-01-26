package model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderStatus {
    CREATED("Создан"),
    EN_ROUTE("В пути"),
    DELIVERED("Доставлен");

    private final String displayName;
}
