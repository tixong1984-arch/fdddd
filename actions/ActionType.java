package dev.enco.greatcombat.core.actions;

import dev.enco.greatcombat.core.actions.Action;
import dev.enco.greatcombat.core.actions.context.Context;
import dev.enco.greatcombat.core.actions.context.MaterialContext;
import dev.enco.greatcombat.core.actions.context.SoundContext;
import dev.enco.greatcombat.core.actions.context.StringContext;
import dev.enco.greatcombat.core.actions.context.TitleContext;
import dev.enco.greatcombat.core.actions.impl.ActionBarAction;
import dev.enco.greatcombat.core.actions.impl.BackItemsAction;
import dev.enco.greatcombat.core.actions.impl.BroadcastActionBarAction;
import dev.enco.greatcombat.core.actions.impl.BroadcastMessageAction;
import dev.enco.greatcombat.core.actions.impl.BroadcastSoundAction;
import dev.enco.greatcombat.core.actions.impl.BroadcastTitleAction;
import dev.enco.greatcombat.core.actions.impl.ConsoleAction;
import dev.enco.greatcombat.core.actions.impl.MessageAction;
import dev.enco.greatcombat.core.actions.impl.PlayerAction;
import dev.enco.greatcombat.core.actions.impl.RemoveItemAction;
import dev.enco.greatcombat.core.actions.impl.SoundAction;
import dev.enco.greatcombat.core.actions.impl.TitleAction;
import lombok.Generated;

public enum ActionType {
    ACTIONBAR(new ActionBarAction(), StringContext.class),
    BROADCASTACTIONBAR(new BroadcastActionBarAction(), StringContext.class),
    BROADCASTMESSAGE(new BroadcastMessageAction(), StringContext.class),
    BROADCASTSOUND(new BroadcastSoundAction(), SoundContext.class),
    BROADCASTTITLE(new BroadcastTitleAction(), TitleContext.class),
    CONSOLE(new ConsoleAction(), StringContext.class),
    MESSAGE(new MessageAction(), StringContext.class),
    PLAYER(new PlayerAction(), StringContext.class),
    SOUND(new SoundAction(), SoundContext.class),
    TITLE(new TitleAction(), TitleContext.class),
    REMOVE_ITEMS(new RemoveItemAction(), MaterialContext.class),
    BACK_ITEMS(new BackItemsAction(), StringContext.class);

    private final Action<?> action;
    private final Class<? extends Context> contextType;

    private ActionType(Action<?> action, Class<? extends Context> contextType) {
        this.action = action;
        this.contextType = contextType;
    }

    public <C extends Context> Action<C> getAction() {
        return this.action;
    }

    @Generated
    public Class<? extends Context> getContextType() {
        return this.contextType;
    }
}
