package dev.enco.greatcombat.core.actions;

import com.google.common.collect.ImmutableMap;
import dev.enco.greatcombat.core.actions.ActionMap;
import dev.enco.greatcombat.core.actions.ActionType;
import dev.enco.greatcombat.core.actions.context.Context;
import dev.enco.greatcombat.core.actions.context.MaterialContext;
import dev.enco.greatcombat.core.actions.context.SoundContext;
import dev.enco.greatcombat.core.actions.context.StringContext;
import dev.enco.greatcombat.core.actions.context.TitleContext;
import dev.enco.greatcombat.core.config.settings.Locale;
import dev.enco.greatcombat.core.utils.logger.Logger;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Generated;

public final class ActionFactory {
    private static final Pattern ACTION_PATTERN = Pattern.compile("\\[(\\S+)] ?(.*)");
    private static final ImmutableMap<Class<? extends Context>, BiFunction<String, Locale, ? extends Context>> VALIDATORS = ImmutableMap.of(StringContext.class, StringContext::validate, TitleContext.class, TitleContext::validate, SoundContext.class, SoundContext::validate, MaterialContext.class, MaterialContext::validate);

    public static ActionMap from(Locale locale, List<String> settings) {
        if (settings == null || settings.isEmpty()) {
            return ActionMap.EMPTY;
        }
        HashMap<ActionType, List> actions = new HashMap<ActionType, List>();
        for (String s : settings) {
            ActionType type;
            Matcher matcher = ACTION_PATTERN.matcher(s);
            if (!matcher.matches()) {
                Logger.warn(locale.illegalActionPattern() + s);
                continue;
            }
            String typeName = matcher.group(1);
            try {
                type = ActionType.valueOf(typeName);
            }
            catch (IllegalArgumentException e) {
                Logger.warn(MessageFormat.format(locale.actionDoesNotExist(), typeName));
                continue;
            }
            String contextStr = matcher.group(2).trim();
            List contexts = actions.computeIfAbsent(type, k -> new ArrayList());
            BiFunction<String, Locale, ? extends Context> validator = VALIDATORS.get(type.getContextType());
            Context context = validator.apply(contextStr, locale);
            if (context == null) continue;
            contexts.add(context);
        }
        ActionType[] types = new ActionType[actions.size()];
        Context[][] contexts = new Context[actions.size()][];
        int i = 0;
        for (Map.Entry entry : actions.entrySet()) {
            types[i] = (ActionType)((Object)entry.getKey());
            List contextList = (List)entry.getValue();
            contexts[i] = contextList.toArray(new Context[0]);
            ++i;
        }
        return new ActionMap(types, contexts);
    }

    @Generated
    private ActionFactory() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
