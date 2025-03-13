package me.supcheg.adventurelike.processor.impl;

import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.TypeSpec;
import lombok.RequiredArgsConstructor;
import me.supcheg.adventurelike.processor.ElementProcessContext;
import me.supcheg.adventurelike.processor.impl.strategy.AdventureLikeGenerationStrategy;
import me.supcheg.adventurelike.processor.impl.util.MoreTypes;
import net.kyori.adventure.util.Buildable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.IOException;

@RequiredArgsConstructor
public class ImplementationGenerator {
    private final AdventureLikeGenerationStrategy defaultStrategy;
    private final AdventureLikeGenerationStrategy buildableStrategy;
    private final MoreTypes moreTypes;

    public void processElement(@NotNull ElementProcessContext ctx, @NotNull Filer target) throws IOException {
        TypeElement element = ctx.element();
        String packageName = ctx.packageName();

        TypeSpec typeSpec = strategyFor(element).generate(ctx);

        JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                .skipJavaLangImports(true)
                .indent("    ")
                .build();

        javaFile.writeTo(target);
    }

    private AdventureLikeGenerationStrategy strategyFor(Element element) {
        if (isBuildable(element)) {
            return buildableStrategy;
        }

        return defaultStrategy;
    }

    private boolean isBuildable(@NotNull Element element) {
        return moreTypes.isAccessible(Buildable.class, element.asType());
    }
}
