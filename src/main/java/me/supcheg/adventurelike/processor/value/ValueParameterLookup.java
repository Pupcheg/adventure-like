package me.supcheg.adventurelike.processor.value;

import com.palantir.javapoet.AnnotationSpec;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.TypeName;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

import static javax.lang.model.util.ElementFilter.methodsIn;

public class ValueParameterLookup {

    @NotNull
    public List<ValueParameter> listValueParameters(@NotNull Element element) {
        List<ValueParameter> parameters = new ArrayList<>();

        for (ExecutableElement method : methodsIn(element.getEnclosedElements())) {
            if (isValueMethod(method)) {
                parameters.add(makeParameter(method));
            }
        }

        return parameters;
    }

    private boolean isValueMethod(@NotNull ExecutableElement method) {
        return !method.isDefault()
               && !method.getModifiers().contains(Modifier.STATIC)
               && method.getReturnType().getKind() != TypeKind.VOID
               && method.getParameters().isEmpty();
    }

    @NotNull
    private ValueParameter makeParameter(@NotNull ExecutableElement method) {
        TypeMirror returnType = method.getReturnType();

        return new ValueParameter(
                TypeName.get(returnType),
                method.getSimpleName().toString(),
                returnType.getAnnotationMirrors().stream()
                        .map(this::convertAnnotationMirrorToAnnotationSpec)
                        .toList()
        );
    }

    @NotNull
    private AnnotationSpec convertAnnotationMirrorToAnnotationSpec(@NotNull AnnotationMirror annotationMirror) {
        DeclaredType annotationType = annotationMirror.getAnnotationType();
        AnnotationSpec.Builder builder = AnnotationSpec.builder((ClassName) ClassName.get(annotationType));

        annotationMirror.getElementValues()
                .forEach((element, value) ->
                        builder.addMember(
                                element.getSimpleName().toString(),
                                "$L", value.getValue()
                        )
                );

        return builder.build();
    }
}
