package me.supcheg.adventurelike.processor.step.builder.constructor;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.ParameterSpec;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public class BuilderImplParameterInitializer {
    protected final ParameterSpec parameter;

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
