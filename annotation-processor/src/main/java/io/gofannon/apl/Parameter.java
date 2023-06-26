package io.gofannon.apl;

public record Parameter(
        String conf,
        String parameter,
        String defaultValue,
        String description
) {
}
