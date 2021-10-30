package olivermakesco.de.servback;

import eu.pb4.polymer.item.VirtualItem;
import eu.pb4.polymer.resourcepack.CMDInfo;
import eu.pb4.polymer.resourcepack.ResourcePackUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class EnderBackpackItem extends Item implements VirtualItem {
  private final CMDInfo model = ResourcePackUtils.requestCustomModelData(Items.CARROT_ON_A_STICK,
      new Identifier("serverbackpacks", "item/ender"));

  public EnderBackpackItem(Settings settings) {
    super(settings.maxCount(1));
  }

  @Override
  public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
    if (!(user instanceof ServerPlayerEntity player))
      return TypedActionResult.pass(ItemStack.EMPTY);

    ItemStack stack = player.getStackInHand(hand);
    var cast = player.raycast(5, 0, false);
    if (cast.getType() == HitResult.Type.BLOCK)
      return TypedActionResult.pass(stack);

    new EnderBackpackGui(player);
    return TypedActionResult.success(stack);
  }

  @Override
  public ActionResult useOnBlock(ItemUsageContext context) {
    if (!(context.getPlayer() instanceof ServerPlayerEntity player))
      return ActionResult.PASS;

    new EnderBackpackGui(player);
    return ActionResult.SUCCESS;
  }

  @Override
  public Item getVirtualItem() {
    return this.model.item();
  }

  @Override
  public int getCustomModelData(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
    return this.model.value();
  }
}