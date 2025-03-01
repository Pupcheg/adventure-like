package me.supcheg.adventurelike.processor.step.builder;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeSpec;
import lombok.RequiredArgsConstructor;
import me.supcheg.adventurelike.processor.util.AnnotationHelper;
import me.supcheg.adventurelike.processor.step.GenerationStep;
import me.supcheg.adventurelike.processor.step.builder.param.BuilderImplParameterFieldGenerationStep;
import me.supcheg.adventurelike.processor.step.builder.param.BuilderImplParameterFieldGetterGenerationStep;
import me.supcheg.adventurelike.processor.step.builder.param.BuilderImplParameterFieldSetterGenerationStep;
import me.supcheg.adventurelike.processor.step.builder.param.primitive.BuilderImplParameterPrimitiveFieldSetterGenerationStep;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class BuilderImplParameterGenerationStep implements GenerationStep {
    private final AnnotationHelper annotationHelper;
    private final ClassName builderImplClassName;
    private final ParameterSpec parameter;

    @Override
    public void generate(TypeSpec.@NotNull Builder target) {
        new BuilderImplParameterFieldGenerationStep(annotationHelper, parameter).generate(target);

        if (parameter.type().isPrimitive())
            new BuilderImplParameterPrimitiveFieldSetterGenerationStep(annotationHelper, builderImplClassName, parameter).generate(target);
        else
            new BuilderImplParameterFieldSetterGenerationStep(annotationHelper, builderImplClassName, parameter).generate(target);

        new BuilderImplParameterFieldGetterGenerationStep(parameter).generate(target);
    }
}
