package olivermakesco.de.servback;

import eu.pb4.polymer.resourcepack.CMDInfo;
import eu.pb4.polymer.resourcepack.ResourcePackUtils;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class LargeBackpackItem extends BaseBackpackItem {
  private final CMDInfo model = ResourcePackUtils.requestCustomModelData(Items.BUNDLE,
      new Identifier("serverbackpacks", "item/large"));

  public LargeBackpackItem(Settings settings) {
    super(settings);
  }

  @Override
  protected CMDInfo getModel() {
    return this.model;
  }

  @Override
  protected int getSlots() {
    return 18;
  }
}
