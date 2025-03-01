package me.supcheg.adventurelike.processor.util;

import com.palantir.javapoet.AnnotationSpec;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.TypeName;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.function.Predicate.isEqual;

public class AnnotationHelper {
    private final Set<TypeName> nullabilityAnnotations = Set.of(
            TypeName.get(Nullable.class),
            TypeName.get(NotNull.class)
    );

    @NotNull
    public List<AnnotationSpec> putNullabilityAnnotation(
            @NotNull List<AnnotationSpec> annotations,
            @NotNull Class<? extends Annotation> type
    ) {
        if (!isNullabilityAnnotation(TypeName.get(type))) {
            throw new IllegalArgumentException("Unsupported nullability annotation: " + type);
        }

        List<AnnotationSpec> copy = new ArrayList<>(annotations);

        int i = indexOfNullabilityAnnotation(annotations);
        if (i != -1) {
            copy.set(i, annotationSpec(type));
        } else {
            copy.addFirst(annotationSpec(type));
        }

        return copy;
    }

    @NotNull
    public List<AnnotationSpec> removeNullabilityAnnotations(
            @NotNull List<AnnotationSpec> annotations
    ) {
        List<AnnotationSpec> copy = new ArrayList<>(annotations);
        copy.removeIf(this::isNullabilityAnnotation);
        return copy;
    }

    @NotNull
    public List<AnnotationSpec> addIfNotPresent(
            @NotNull List<AnnotationSpec> annotations,
            @NotNull Class<? extends Annotation> type
    ) {
        if (annotations.stream().map(AnnotationSpec::type).anyMatch(isEqual(ClassName.get(type)))) {
            return annotations;
        }

        List<AnnotationSpec> copy = new ArrayList<>(annotations);
        copy.add(annotationSpec(type));
        return copy;
    }

    @NotNull
    @Contract("_ -> new")
    private static AnnotationSpec annotationSpec(Class<? extends Annotation> type) {
        return AnnotationSpec.builder(type).build();
    }

    private int indexOfNullabilityAnnotation(@NotNull List<AnnotationSpec> annotations) {
        for (int i = 0; i < annotations.size(); i++) {
            if (isNullabilityAnnotation(annotations.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public boolean isNullabilityAnnotation(@NotNull AnnotationSpec spec) {
        return isNullabilityAnnotation(spec.type());
    }

    public boolean isNullabilityAnnotation(@NotNull TypeName type) {
        return nullabilityAnnotations.contains(type);
    }
}
