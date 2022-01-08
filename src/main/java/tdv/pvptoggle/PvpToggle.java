package tdv.pvptoggle;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("pvptoggle")
public class PvpToggle {
    public static final String MODID = "pvptoggle";
    public static final Logger LOGGER = LogManager.getLogger();

    public static HashMap<String, Boolean> pvpStatus = new HashMap<>();
    public static HashMap<String, Long> combatTimer = new HashMap<>();

    public PvpToggle() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {

    }
}