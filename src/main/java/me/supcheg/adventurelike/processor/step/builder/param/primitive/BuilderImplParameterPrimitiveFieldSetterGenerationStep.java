package me.supcheg.adventurelike.processor.step.builder.param.primitive;

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

@RequiredArgsConstructor
public class BuilderImplParameterPrimitiveFieldSetterGenerationStep implements GenerationStep {
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
                .add("this.$L = $L;\n", name, name)
                .add("return this;")
                .build();
    }

    private List<AnnotationSpec> parameterAnnotations() {
        return annotationHelper.removeNullabilityAnnotations(parameter.annotations());
    }
}
