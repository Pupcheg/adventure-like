package me.supcheg.adventurelike.processor.step;

import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeSpec;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class ConstructorGenerationStep implements GenerationStep {
    private final Iterable<ParameterSpec> parameters;

    @Override
    public void generate(TypeSpec.@NotNull Builder target) {
        target.recordConstructor(
                MethodSpec.constructorBuilder()
                        .addParameters(parameters)
                        .build()
        );
    }
}
