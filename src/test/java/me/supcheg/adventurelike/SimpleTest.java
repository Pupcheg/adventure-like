package me.supcheg.adventurelike;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import me.supcheg.adventurelike.processor.AdventureLikeProcessor;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;

class SimpleTest {
    @Test
    void run() {
        JavaFileObject source = JavaFileObjects.forSourceLines(
                "MyCoolInterface",
                """
                        package me.supcheg.adventurelike.test;
                        
                        import me.supcheg.adventurelike.AdventureLike;
                        import net.kyori.adventure.util.Buildable;
                        import org.jetbrains.annotations.NotNull;
                        import org.jetbrains.annotations.Nullable;
                        import org.jetbrains.annotations.Range;
                        import org.jetbrains.annotations.Unmodifiable;
                        
                        import java.util.List;
                        
                        @AdventureLike
                        public interface MyCoolInterface {
                            @NotNull
                            String key();
                        
                            @NotNull
                            String value();
                        
                            @Range(from = 1, to = 2)
                            int intValue();
                        
                            @NotNull
                            @Unmodifiable
                            List<String> list();
                        
                            @NotNull
                            default String lowercaseKey() {
                                return key().toLowerCase();
                            }
                        }
                        """
        );

        Compilation compilation = Compiler.javac()
                .withProcessors(new AdventureLikeProcessor())
                .compile(source);

        assertThat(compilation).succeeded();
        assertThat(compilation)
                .generatedSourceFile("me.supcheg.adventurelike.test.MyCoolInterfaceImpl")
                .hasSourceEquivalentTo(JavaFileObjects.forSourceLines(
                        "MyCoolInterfaceImpl",
                        """
                                package me.supcheg.adventurelike.test;
                                
                                import org.jetbrains.annotations.NotNull;
                                import org.jetbrains.annotations.Range;
                                import org.jetbrains.annotations.Unmodifiable;
                                
                                import java.util.List;
                                
                                record MyCoolInterfaceImpl(
                                        @NotNull String key,
                                        @NotNull String value,
                                        @Range(from = 1, to = 2) int intValue,
                                        @NotNull @Unmodifiable List<String> list
                                ) implements MyCoolInterface {
                                }
                                
                                """));
    }
}
