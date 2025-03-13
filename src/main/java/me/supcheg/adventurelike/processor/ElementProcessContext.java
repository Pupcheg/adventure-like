package me.supcheg.adventurelike.processor;

import me.supcheg.adventurelike.processor.value.ValueParameter;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.TypeElement;
import java.util.List;

public record ElementProcessContext(
        @NotNull TypeElement element,
        @NotNull String packageName,
        @NotNull List<ValueParameter> valueParameters
) {
}
