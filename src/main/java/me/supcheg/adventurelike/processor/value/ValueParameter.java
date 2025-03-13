package me.supcheg.adventurelike.processor.value;

import com.palantir.javapoet.AnnotationSpec;
import com.palantir.javapoet.TypeName;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ValueParameter(
        @NotNull TypeName type,
        @NotNull String name,
        @NotNull List<AnnotationSpec> annotations
) {
}
