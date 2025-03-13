package me.supcheg.adventurelike.processor.impl.step;

import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeSpec;
import lombok.RequiredArgsConstructor;
import me.supcheg.adventurelike.processor.value.ValueParameter;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@RequiredArgsConstructor
public class ConstructorGenerationStep implements GenerationStep {
    private final Collection<ValueParameter> parameters;

    @Override
    public void generate(TypeSpec.@NotNull Builder target) {
        target.recordConstructor(
                MethodSpec.constructorBuilder()
                        .addParameters(
                                parameters.stream()
                                        .map(parameter ->
                                                ParameterSpec.builder(parameter.type(), parameter.name())
                                                        .addAnnotations(parameter.annotations())
                                                        .build()
                                        )
                                        ::iterator
                        )
                        .build()
        );
    }
}
