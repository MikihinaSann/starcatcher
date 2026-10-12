package net.neoforged.fml;

import java.util.List;

public class ModLoadingException extends RuntimeException
{
    public ModLoadingException(String message)
    {
        super(message);
    }

    public ModLoadingException(List<ModLoadingIssue> issues)
    {
        super(issues.isEmpty() ? "mod loading failed" : issues.get(0).translationKey());
    }
}
