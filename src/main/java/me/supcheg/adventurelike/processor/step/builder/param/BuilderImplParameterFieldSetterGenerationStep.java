package me.supcheg.adventurelike.processor.step.builder.param;

import com.palantir.javapoet.AnnotationSpec;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeSpec;
import lombok.RequiredArgsConstructor;
import me.supcheg.adventurelike.processor.util.AnnotationHelper;
import me.supcheg.adventurelike.processor.step.GenerationStep;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class BuilderImplParameterFieldSetterGenerationStep implements GenerationStep {
    private final AnnotationHelper annotationHelper;
    private final ClassName builderImplClassName;
    private final ParameterSpec parameter;

    @Override
    public void generate(@NotNull TypeSpec.Builder target) {
        target.addMethod(
                MethodSpec.methodBuilder(parameter.name())
                        .addAnnotation(NotNull.class)
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(
                                ParameterSpec.builder(parameter.type(), parameter.name())
                                        .addAnnotations(parameterAnnotations())
                                        .build()
                        )
                        .returns(builderImplClassName)
                        .addCode(code())
                        .build()
        );
    }

    @NotNull
    private CodeBlock code() {
        String name = parameter.name();
        return CodeBlock.builder()
                .add("$T.requireNonNull($L, $S);\n", Objects.class, name, name)
                .add("this.$L = $L;\n", name, name)
                .add("return this;")
                .build();
    }

    private List<AnnotationSpec> parameterAnnotations() {
        return annotationHelper.putNullabilityAnnotation(
                parameter.annotations(),
                NotNull.class
        );
    }
}
