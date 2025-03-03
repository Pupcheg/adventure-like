package me.supcheg.adventurelike.processor.step.builder.constructor;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeName;
import me.supcheg.adventurelike.processor.util.MoreTypes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class BuilderImplCollectionParameterInitializer extends BuilderImplParameterInitializer {
    private final MoreTypes moreTypes;
    private final CollectionParameter collectionParameter;

    public BuilderImplCollectionParameterInitializer(MoreTypes moreTypes, ParameterSpec parameter) {
        super(parameter);
        this.moreTypes = moreTypes;
        this.collectionParameter = implementationClass();
    }

    @Override
    public CodeBlock newValueInitializer() {
        return CodeBlock.of("this.$L = new $T<>();\n", parameter.name(), collectionParameter.implClass());
    }

    @Override
    public CodeBlock copyingInitializer(String from) {
        return CodeBlock.of("this.$L = new $T<>($L.$L);\n", parameter.name(), collectionParameter.implClass(), from, parameter.name());
    }

    @Override
    public CodeBlock finalizer() {
        return collectionParameter.finalizer().apply(parameter);
    }

    private @NotNull CollectionParameter implementationClass() {
        TypeName type = parameter.type();

        if (moreTypes.isAccessible(List.class, type)) {
            return new CollectionParameter(
                    ClassName.get(ArrayList.class),
                    param -> CodeBlock.of("$T.copyOf($L)", List.class, param.name())
            );
        }

        if (moreTypes.isAccessible(Set.class, type)) {
            return new CollectionParameter(
                    ClassName.get(LinkedHashSet.class),
                    param -> CodeBlock.of("$T.copyOf($L)", Set.class, param.name())
            );
        }

        if (moreTypes.isAccessible(Collection.class, type)) {
            return new CollectionParameter(
                    ClassName.get(ArrayList.class),
                    param -> CodeBlock.of("$T.copyOf($L)", List.class, param.name())
            );
        }

        throw new IllegalArgumentException("Unsupported type: " + type);
    }

    record CollectionParameter(
            ClassName implClass,
            Function<ParameterSpec, CodeBlock> finalizer
    ) {
    }
}
