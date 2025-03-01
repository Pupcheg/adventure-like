package me.supcheg.adventurelike.processor.parameter;

import com.palantir.javapoet.AnnotationSpec;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeName;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

import static javax.lang.model.util.ElementFilter.methodsIn;

public class ParameterSpecLookup {

    @NotNull
    public List<ParameterSpec> listRecordParametersForInterface(@NotNull Element element) {
        List<ParameterSpec> parameters = new ArrayList<>();

        for (ExecutableElement method : methodsIn(element.getEnclosedElements())) {
            if (!method.isDefault() && method.getReturnType().getKind() != TypeKind.VOID) {
                parameters.add(makeParameter(method));
            }
        }

        return parameters;
    }

    @NotNull
    private ParameterSpec makeParameter(@NotNull ExecutableElement method) {
        TypeMirror returnType = method.getReturnType();

        return ParameterSpec.builder(
                        TypeName.get(returnType),
                        method.getSimpleName().toString()
                )
                .addAnnotations(
                        returnType.getAnnotationMirrors().stream()
                                .map(this::convertAnnotationMirrorToAnnotationSpec)
                                ::iterator
                )
                .build();
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
