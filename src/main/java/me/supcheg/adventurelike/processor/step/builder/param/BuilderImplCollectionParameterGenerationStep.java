package me.supcheg.adventurelike.processor.step.builder.param;

import com.palantir.javapoet.AnnotationSpec;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.ParameterSpec;
import me.supcheg.adventurelike.processor.util.AnnotationHelper;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Objects;

public class BuilderImplCollectionParameterGenerationStep extends BuilderImplParameterGenerationStep {
    public BuilderImplCollectionParameterGenerationStep(ParameterSpec parameter, AnnotationHelper annotationHelper, ClassName builderImplClass) {
        super(parameter, annotationHelper, builderImplClass);
    }

    @Override
    protected FieldSpec.Builder fieldSpec() {
        return super.fieldSpec()
                .addModifiers(Modifier.FINAL);
    }

    @Override
    protected CodeBlock setterCode() {
        String name = parameter.name();
        return CodeBlock.builder()
                .add("$T.requireNonNull($L, $S);\n", Objects.class, name, name)
                .add("this.$L.clear();\n", name)
                .add("this.$L.addAll($L);\n", name, name)
                .add("return this;\n")
                .build();
    }

    @Override
    protected List<AnnotationSpec> getterAnnotations() {
        return annotationHelper.addIfNotPresent(
                annotationHelper.putNullabilityAnnotation(
                        parameter.annotations(),
                        NotNull.class
                ),
                Override.class
        );
    }
}
