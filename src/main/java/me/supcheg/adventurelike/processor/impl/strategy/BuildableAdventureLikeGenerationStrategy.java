package me.supcheg.adventurelike.processor.impl.strategy;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.TypeSpec;
import lombok.RequiredArgsConstructor;
import me.supcheg.adventurelike.processor.ElementProcessContext;
import me.supcheg.adventurelike.processor.impl.step.ConstructorGenerationStep;
import me.supcheg.adventurelike.processor.impl.step.GeneratedAnnotationGenerationStep;
import me.supcheg.adventurelike.processor.impl.step.builder.BuilderImplGenerationStep;
import me.supcheg.adventurelike.processor.impl.util.AnnotationHelper;
import me.supcheg.adventurelike.processor.impl.util.MoreTypes;
import me.supcheg.adventurelike.processor.value.ValueParameter;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.TypeElement;
import java.util.List;

@RequiredArgsConstructor
public class BuildableAdventureLikeGenerationStrategy implements AdventureLikeGenerationStrategy {
    public static final String BUILDER_NAME = "Builder";
    public static final String IMPL_SUFFIX = "Impl";

    private final AnnotationHelper annotationHelper;
    private final MoreTypes moreTypes;

    @NotNull
    @Override
    public TypeSpec generate(@NotNull ElementProcessContext ctx) {
        TypeElement element = ctx.element();
        String packageName = ctx.packageName();
        List<ValueParameter> valueParameters = ctx.valueParameters();

        ClassName targetName = ClassName.get(
                packageName,
                element.getSimpleName() + IMPL_SUFFIX
        );

        TypeSpec.Builder target = TypeSpec.recordBuilder(targetName)
                .addSuperinterface(element.asType());

        new ConstructorGenerationStep(valueParameters).generate(target);
        new BuilderImplGenerationStep(
                annotationHelper,
                moreTypes,
                targetName,
                targetName.nestedClass(BUILDER_NAME + IMPL_SUFFIX),
                element,
                valueParameters
        ).generate(target);

        new GeneratedAnnotationGenerationStep(valueParameters).generate(target);

        return target.build();
    }
}
