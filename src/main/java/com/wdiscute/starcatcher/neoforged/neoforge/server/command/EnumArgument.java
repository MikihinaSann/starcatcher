package com.wdiscute.starcatcher.neoforged.neoforge.server.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Fabric port shim of NeoForge's EnumArgument.
 */
public class EnumArgument<T extends Enum<T>> implements ArgumentType<T>
{
    private static final DynamicCommandExceptionType INVALID_ENUM =
            new DynamicCommandExceptionType(o -> Component.literal("Invalid enum value: " + o));

    private final Class<T> enumClass;

    private EnumArgument(Class<T> enumClass)
    {
        this.enumClass = enumClass;
    }

    public static <T extends Enum<T>> EnumArgument<T> enumArgument(Class<T> enumClass)
    {
        return new EnumArgument<>(enumClass);
    }

    public static <T extends Enum<T>> T getEnum(CommandContext<?> context, String name, Class<T> enumClass)
    {
        return context.getArgument(name, enumClass);
    }

    @Override
    public T parse(StringReader reader) throws CommandSyntaxException
    {
        String name = reader.readUnquotedString();
        try
        {
            return Enum.valueOf(enumClass, name);
        }
        catch (IllegalArgumentException e)
        {
            throw INVALID_ENUM.createWithContext(reader, name);
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder)
    {
        return SharedSuggestionProvider.suggest(
                Arrays.stream(enumClass.getEnumConstants()).map(Enum::name), builder);
    }

    @Override
    public Collection<String> getExamples()
    {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name).collect(Collectors.toList());
    }

    /**
     * Network serializer so command trees using this type sync to clients.
     */
    public static class EnumArgumentInfo
            implements net.minecraft.commands.synchronization.ArgumentTypeInfo<EnumArgument<?>, EnumArgumentInfo.TemplateImpl>
    {
        @Override
        public void serializeToNetwork(TemplateImpl template, net.minecraft.network.FriendlyByteBuf buf)
        {
            buf.writeUtf(template.enumClassName);
        }

        @Override
        public TemplateImpl deserializeFromNetwork(net.minecraft.network.FriendlyByteBuf buf)
        {
            return new TemplateImpl(buf.readUtf());
        }

        @Override
        public void serializeToJson(TemplateImpl template, com.google.gson.JsonObject json)
        {
            json.addProperty("enum", template.enumClassName);
        }

        @Override
        public TemplateImpl unpack(EnumArgument<?> argument)
        {
            return new TemplateImpl(argument.enumClass.getName());
        }

        public class TemplateImpl implements net.minecraft.commands.synchronization.ArgumentTypeInfo.Template<EnumArgument<?>>
        {
            private final String enumClassName;

            public TemplateImpl(String enumClassName)
            {
                this.enumClassName = enumClassName;
            }

            @Override
            @SuppressWarnings({"unchecked", "rawtypes"})
            public EnumArgument<?> instantiate(net.minecraft.commands.CommandBuildContext context)
            {
                try
                {
                    return new EnumArgument<>((Class) Class.forName(enumClassName));
                }
                catch (ClassNotFoundException e)
                {
                    throw new IllegalArgumentException("Unknown enum class " + enumClassName, e);
                }
            }

            @Override
            public net.minecraft.commands.synchronization.ArgumentTypeInfo<EnumArgument<?>, ?> type()
            {
                return EnumArgumentInfo.this;
            }
        }
    }
}
