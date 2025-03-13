package me.supcheg.adventurelike.processor.impl.step.builder.param;

import com.palantir.javapoet.AnnotationSpec;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import me.supcheg.adventurelike.processor.impl.util.AnnotationHelper;
import me.supcheg.adventurelike.processor.value.ValueParameter;

import java.util.List;

public class BuilderImplPrimitiveParameterGenerationStep extends BuilderImplParameterGenerationStep {
    public BuilderImplPrimitiveParameterGenerationStep(ValueParameter parameter, AnnotationHelper annotationHelper, ClassName builderImplClass) {
        super(parameter, annotationHelper, builderImplClass);
    }

    @Override
    protected CodeBlock setterCode() {
        String name = parameter.name();
        return CodeBlock.builder()
                .add("this.$L = $L;\n", name, name)
                .add("return this;")
                .build();
    }

    @Override
    protected List<AnnotationSpec> setterParameterAnnotations() {
        return annotationHelper.removeNullabilityAnnotations(parameter.annotations());
    }
}
