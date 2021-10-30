package olivermakesco.de.servback;

import eu.pb4.polymer.resourcepack.ResourcePackUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Entrypoint implements ModInitializer {
  public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder
      .build(new Identifier("serverbackpacks", "backpacks"), () -> new ItemStack(Entrypoint.NORMAL));

  public static TinyBackpackItem TINY = new TinyBackpackItem(new FabricItemSettings().group(ITEM_GROUP));
  public static BackpackItem NORMAL = new BackpackItem(new FabricItemSettings().group(ITEM_GROUP));
  public static LargeBackpackItem LARGE = new LargeBackpackItem(new FabricItemSettings().group(ITEM_GROUP));
  public static EnderBackpackItem ENDER = new EnderBackpackItem(new FabricItemSettings().group(ITEM_GROUP));

  @Override
  public void onInitialize() {
    ResourcePackUtils.addModAsAssetsSource("serverbackpacks");

    Registry.register(Registry.ITEM, new Identifier("serverbackpacks", "normal"), NORMAL);
    Registry.register(Registry.ITEM, new Identifier("serverbackpacks", "large"), LARGE);
    Registry.register(Registry.ITEM, new Identifier("serverbackpacks", "tiny"), TINY);
    Registry.register(Registry.ITEM, new Identifier("serverbackpacks", "ender"), ENDER);
  }
}
