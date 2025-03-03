package me.supcheg.adventurelike.processor.step.builder.constructor;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeName;
import me.supcheg.adventurelike.processor.util.MoreTypes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class BuilderImplCollectionParameterInitializer extends BuilderImplParameterInitializer {
    private final MoreTypes moreTypes;
    private final ClassName implClass;

    public BuilderImplCollectionParameterInitializer(MoreTypes moreTypes, ParameterSpec parameter) {
        super(parameter);
        this.moreTypes = moreTypes;
        this.implClass = implementationClass();
    }

    @Override
    public CodeBlock newValueInitializer() {
        return CodeBlock.of("this.$L = new $T<>();\n", parameter.name(), implClass);
    }

    @Override
    public CodeBlock copyingInitializer(String from) {
        return CodeBlock.of("this.$L = new $T<>($L.$L);\n", parameter.name(), implClass, from, parameter.name());
    }

    private @NotNull ClassName implementationClass() {
        TypeName type = parameter.type();

        if(moreTypes.isAccessible(Deque.class, type)) {
            return ClassName.get(LinkedList.class);
        }

        if (moreTypes.isAccessible(List.class, type)) {
            return ClassName.get(ArrayList.class);
        }

        if (moreTypes.isAccessible(Set.class, type)) {
            return ClassName.get(LinkedHashSet.class);
        }

        if(moreTypes.isAccessible(Collection.class, type)) {
            return ClassName.get(ArrayList.class);
        }

        throw new IllegalArgumentException("Unsupported type: " + type);
    }
}
