package me.supcheg.adventurelike.processor.impl.step.builder.constructor;

import com.palantir.javapoet.CodeBlock;
import lombok.RequiredArgsConstructor;
import me.supcheg.adventurelike.processor.value.ValueParameter;

import java.util.Objects;

@RequiredArgsConstructor
public class BuilderImplParameterInitializer {
    protected final ValueParameter parameter;

    public CodeBlock newValueInitializer() {
        return CodeBlock.of("");
    }

    public CodeBlock copyingInitializer(String from) {
        return CodeBlock.of("this.$L = $L.$L;\n", parameter.name(), from, parameter.name());
    }

    public CodeBlock finalizer() {
        return CodeBlock.of("$T.requireNonNull($L, $S)", Objects.class, parameter.name(), parameter.name());
    }
}
