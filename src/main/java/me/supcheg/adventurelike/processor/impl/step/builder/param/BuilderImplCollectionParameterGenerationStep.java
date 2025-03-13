package me.supcheg.adventurelike.processor.impl.step.builder.param;

import com.palantir.javapoet.AnnotationSpec;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import me.supcheg.adventurelike.processor.impl.util.AnnotationHelper;
import me.supcheg.adventurelike.processor.value.ValueParameter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Objects;

public class BuilderImplCollectionParameterGenerationStep extends BuilderImplParameterGenerationStep {
    public BuilderImplCollectionParameterGenerationStep(ValueParameter parameter, AnnotationHelper annotationHelper, ClassName builderImplClass) {
        super(parameter, annotationHelper, builderImplClass);
    }

    @Override
    protected FieldSpec.Builder fieldSpec() {
        return super.fieldSpec()
                .addModifiers(Modifier.FINAL);
    }

    @Override
    protected List<AnnotationSpec> setterParameterAnnotations() {
        return annotationHelper.removeIfPresent(
                super.setterParameterAnnotations(),
                Unmodifiable.class
        );
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
