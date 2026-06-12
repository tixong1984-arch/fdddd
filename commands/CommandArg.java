package dev.enco.greatcombat.core.commands;

import dev.enco.greatcombat.core.commands.Subcommand;
import dev.enco.greatcombat.core.commands.impl.CombatSubcommand;
import dev.enco.greatcombat.core.commands.impl.CopySubcommand;
import dev.enco.greatcombat.core.commands.impl.ReloadSubcommand;
import dev.enco.greatcombat.core.commands.impl.StopAllSubcommand;
import dev.enco.greatcombat.core.commands.impl.StopSubcommand;
import dev.enco.greatcombat.core.commands.impl.UpdateSubcommand;
import lombok.Generated;

public enum CommandArg {
    RELOAD(ReloadSubcommand.class),
    STOP(StopSubcommand.class),
    STOPALL(StopAllSubcommand.class),
    GIVE(CombatSubcommand.class),
    UPDATE(UpdateSubcommand.class),
    COPY(CopySubcommand.class);

    private final Class<? extends Subcommand> clazz;

    private CommandArg(Class<? extends Subcommand> clazz) {
        this.clazz = clazz;
    }

    @Generated
    public Class<? extends Subcommand> getClazz() {
        return this.clazz;
    }
}
