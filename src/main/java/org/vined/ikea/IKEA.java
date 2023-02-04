package org.vined.ikea;

import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;
import org.vined.ikea.modules.dupes.IKEADupe;
import org.vined.ikea.modules.utility.AutoItemMove;
import org.vined.ikea.modules.utility.DubCounter;
import org.vined.ikea.modules.utility.NoChestRender;

public final class IKEA extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category UTILITY = new Category("IKEA Utility");
    public static final Category DUPES = new Category("IKEA Dupes");

    @Override
    public final void onInitialize() {
        LOG.info("Initializing IKEA Addon");
        Modules.get().add(new IKEADupe());
        Modules.get().add(new NoChestRender());
        Modules.get().add(new DubCounter());
        Modules.get().add(new AutoItemMove());
    }

    @Override
    public final void onRegisterCategories() {
        Modules.registerCategory(UTILITY);
        Modules.registerCategory(DUPES);
    }

    @Override
    public final String getPackage() {
        return "org.vined.ikea";
    }
}
