package me.supcheg.adventurelike.processor.step.builder.param;

import com.palantir.javapoet.AnnotationSpec;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.ParameterSpec;
import me.supcheg.adventurelike.processor.util.AnnotationHelper;

import java.util.List;

public class BuilderImplPrimitiveParameterGenerationStep extends BuilderImplParameterGenerationStep {
    public BuilderImplPrimitiveParameterGenerationStep(ParameterSpec parameter, AnnotationHelper annotationHelper, ClassName builderImplClass) {
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
