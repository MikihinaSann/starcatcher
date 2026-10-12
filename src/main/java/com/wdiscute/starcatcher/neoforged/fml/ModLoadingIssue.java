package com.wdiscute.starcatcher.neoforged.fml;

public record ModLoadingIssue(String translationKey, Object[] args)
{
    public static ModLoadingIssue error(String translationKey, Object... args)
    {
        return new ModLoadingIssue(translationKey, args);
    }
}
