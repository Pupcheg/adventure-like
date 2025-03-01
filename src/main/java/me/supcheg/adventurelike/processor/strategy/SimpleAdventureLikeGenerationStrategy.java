package me.supcheg.adventurelike.processor.strategy;

import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeSpec;
import lombok.RequiredArgsConstructor;
import me.supcheg.adventurelike.processor.parameter.ParameterSpecLookup;
import me.supcheg.adventurelike.processor.step.ConstructorGenerationStep;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.Element;
import java.util.List;

@RequiredArgsConstructor
public class SimpleAdventureLikeGenerationStrategy implements AdventureLikeGenerationStrategy {
    private final ParameterSpecLookup parameterSpecLookup;

    @NotNull
    @Override
    public TypeSpec generate(@NotNull Element element) {
        TypeSpec.Builder builder = TypeSpec.recordBuilder(element.getSimpleName() + "Impl")
                .addSuperinterface(element.asType());

        List<ParameterSpec> parameters = parameterSpecLookup.listRecordParametersForInterface(element);

        new ConstructorGenerationStep(parameters).generate(builder);

        return builder.build();
    }
}
