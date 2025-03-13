package me.supcheg.adventurelike.test;

import me.supcheg.adventurelike.AdventureLike;
import net.kyori.adventure.util.Buildable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.SortedSet;

@AdventureLike
public interface MyCoolInterface extends Buildable<MyCoolInterface, MyCoolInterface.Builder> {
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
    @Unmodifiable
    SortedSet<Object> sortedSet();

    @NotNull
    default String lowercaseKey() {
        return key().toLowerCase();
    }

    interface Builder extends Buildable.Builder<MyCoolInterface> {
        @Nullable
        String key();

        Builder key(@NotNull String key);

        @Nullable
        String value();

        Builder value(@NotNull String value);

        @Range(from = 1, to = 2)
        Integer intValue();

        Builder intValue(int intValue);

        @NotNull
        List<String> list();

        Builder list(@NotNull List<String> list);

        @NotNull
        SortedSet<Object> sortedSet();

        Builder sortedSet(@NotNull SortedSet<Object> sortedSet);
    }
}
