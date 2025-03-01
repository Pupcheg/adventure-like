package me.supcheg.adventurelike.processor.step.builder.param;

import com.palantir.javapoet.AnnotationSpec;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeSpec;
import lombok.RequiredArgsConstructor;
import me.supcheg.adventurelike.processor.util.AnnotationHelper;
import me.supcheg.adventurelike.processor.step.GenerationStep;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.lang.model.element.Modifier;
import java.util.List;

@RequiredArgsConstructor
public class BuilderImplParameterFieldGenerationStep implements GenerationStep {
    private final AnnotationHelper annotationHelper;
    private final ParameterSpec parameter;

    @Override
    public void generate(TypeSpec.@NotNull Builder target) {
        target.addField(
                FieldSpec.builder(parameter.type().box(), parameter.name())
                        .addModifiers(Modifier.PRIVATE)
                        .addAnnotations(annotations())
                        .build()
        );
    }

    private List<AnnotationSpec> annotations() {
        return annotationHelper.putNullabilityAnnotation(
                parameter.annotations(),
                Nullable.class
        );
    }
}
