package me.supcheg.adventurelike.processor;

import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.TypeSpec;
import me.supcheg.adventurelike.processor.parameter.ParameterSpecLookup;
import me.supcheg.adventurelike.processor.strategy.AdventureLikeGenerationStrategy;
import me.supcheg.adventurelike.processor.strategy.BuildableAdventureLikeGenerationStrategy;
import me.supcheg.adventurelike.processor.strategy.SimpleAdventureLikeGenerationStrategy;
import me.supcheg.adventurelike.processor.util.AnnotationHelper;
import me.supcheg.adventurelike.processor.util.MoreTypes;
import net.kyori.adventure.util.Buildable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.Set;

@SupportedAnnotationTypes("me.supcheg.adventurelike.AdventureLike")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class AdventureLikeProcessor extends AbstractProcessor {
    private final AdventureLikeGenerationStrategy defaultStrategy = new SimpleAdventureLikeGenerationStrategy(
            new ParameterSpecLookup()
    );
    private final AdventureLikeGenerationStrategy buildableStrategy = new BuildableAdventureLikeGenerationStrategy(
            new AnnotationHelper(),
            new ParameterSpecLookup()
    );
    private MoreTypes moreTypes;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.moreTypes = new MoreTypes(processingEnv);
    }

    @Override
    public boolean process(@NotNull Set<? extends TypeElement> annotations, @NotNull RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                try {
                    processElement(element);
                } catch (Exception e) {
                    processingEnv.getMessager().printError(e.toString(), element);
                }
            }
        }
        return true;
    }

    private void processElement(@NotNull Element element) throws IOException {
        if (element.getKind() != ElementKind.INTERFACE) {
            throw new RuntimeException("Element is not an interface");
        }

        String interfaceClassPackage = ((PackageElement) element.getEnclosingElement()).getQualifiedName().toString();

        TypeSpec typeSpec = strategyFor(element).generate(element);

        JavaFile javaFile = JavaFile.builder(interfaceClassPackage, typeSpec)
                .skipJavaLangImports(true)
                .indent("    ")
                .build();

        javaFile.writeTo(processingEnv.getFiler());
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
