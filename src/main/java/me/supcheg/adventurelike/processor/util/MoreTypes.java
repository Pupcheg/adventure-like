package me.supcheg.adventurelike.processor.util;

import com.palantir.javapoet.TypeName;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.List;

@SuppressWarnings("SameParameterValue")
@RequiredArgsConstructor
public class MoreTypes {
    private final ProcessingEnvironment env;

    public boolean isAccessible(@NotNull Class<?> superType, @NotNull TypeName type) {
        if (type.isPrimitive()) {
            return false;
        }

        return isAccessible(superType, getTypeMirror(type));
    }

    public boolean isAccessible(@NotNull Class<?> superType, @NotNull TypeMirror type) {
        return containsSubtype(type, superType.getName());
    }

    private boolean containsSubtype(@NotNull TypeMirror type, @NotNull String looking) {

        if (toNoGenericString(type.toString()).equals(looking)) {
            return true;
        }

        if (toNoGenericString(type.toString()).equals("java.lang.Object")) {
            return false;
        }

        List<? extends TypeMirror> subtypes = env.getTypeUtils().directSupertypes(type);

        for (TypeMirror mirror : subtypes) {
            if (containsSubtype(mirror, looking)) {
                return true;
            }
        }
        return false;
    }

    private boolean isReferenceType(@NotNull TypeMirror type) {
        return !type.getKind().isPrimitive() && type.getKind() != TypeKind.VOID;
    }

    @NotNull
    private String toNoGenericString(@NotNull String type) {
        int genericInfoStartIndex = type.indexOf("<");

        return genericInfoStartIndex == -1 ?
                type :
                type.substring(0, genericInfoStartIndex);
    }

    @NotNull
    private TypeMirror getTypeMirror(@NotNull TypeName name) {
        TypeElement typeElement = env.getElementUtils().getTypeElement(toNoGenericString(name.toString()));
        if (typeElement == null) {
            throw new NullPointerException("Cannot get type mirror for name " + name);
        }
        return typeElement.asType();
    }
}
