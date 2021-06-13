package mod.omoflop.vbm.data;

/**
 * !WIP
 * Used to get data driven condition types when loading resource packs
 * Will replace ConditionFactory's awful logic
 */
public @interface ConditionType {
    public String identifier();
    public String[] arguments();
}
