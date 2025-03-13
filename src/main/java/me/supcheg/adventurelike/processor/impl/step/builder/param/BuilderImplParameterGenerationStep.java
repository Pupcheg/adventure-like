package me.supcheg.adventurelike.processor.impl.step.builder.param;

import com.palantir.javapoet.AnnotationSpec;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeSpec;
import lombok.RequiredArgsConstructor;
import me.supcheg.adventurelike.processor.impl.step.GenerationStep;
import me.supcheg.adventurelike.processor.impl.util.AnnotationHelper;
import me.supcheg.adventurelike.processor.value.ValueParameter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class BuilderImplParameterGenerationStep implements GenerationStep {
    protected final ValueParameter parameter;
    protected final AnnotationHelper annotationHelper;
    protected final ClassName builderImplClass;

    @Override
    public void generate(TypeSpec.@NotNull Builder target) {
        target.addField(fieldSpec().build());
        target.addMethod(setter().build());
        target.addMethod(getter().build());
    }

    protected FieldSpec.Builder fieldSpec() {
        return FieldSpec.builder(parameter.type().box(), parameter.name())
                .addModifiers(Modifier.PRIVATE);
    }

    protected MethodSpec.Builder setter() {
        return MethodSpec.methodBuilder(parameter.name())
                .addAnnotation(NotNull.class)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(
                        ParameterSpec.builder(parameter.type(), parameter.name())
                                .addAnnotations(setterParameterAnnotations())
                                .build()
                )
                .returns(builderImplClass)
                .addCode(setterCode());
    }

    protected List<AnnotationSpec> setterParameterAnnotations() {
        return annotationHelper.putNullabilityAnnotation(
                parameter.annotations(),
                NotNull.class
        );
    }

    protected CodeBlock setterCode() {
        String name = parameter.name();
        return CodeBlock.builder()
                .add("$T.requireNonNull($L, $S);\n", Objects.class, name, name)
                .add("this.$L = $L;\n", name, name)
                .add("return this;\n")
                .build();
    }

    protected MethodSpec.Builder getter() {
        return MethodSpec.methodBuilder(parameter.name())
                .addAnnotations(getterAnnotations())
                .addModifiers(Modifier.PUBLIC)
                .returns(parameter.type().box())
                .addCode(getterCode());
    }

    protected List<AnnotationSpec> getterAnnotations() {
        return annotationHelper.addIfNotPresent(
                annotationHelper.putNullabilityAnnotation(
                        parameter.annotations(),
                        Nullable.class
                ),
                Override.class
        );
    }

    protected CodeBlock getterCode() {
        return CodeBlock.of("return this.$L;\n", parameter.name());
    }

}
