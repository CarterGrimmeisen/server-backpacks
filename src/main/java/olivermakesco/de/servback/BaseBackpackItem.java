package olivermakesco.de.servback;

import eu.pb4.polymer.item.VirtualItem;
import eu.pb4.polymer.resourcepack.CMDInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.minecraft.util.hit.HitResult;

import org.jetbrains.annotations.Nullable;

public abstract class BaseBackpackItem extends BundleItem implements VirtualItem {
  protected abstract CMDInfo getModel();

  protected abstract int getSlots();

  public static final int MAX_STORAGE = 576;

  public BaseBackpackItem(Settings settings) {
    super(settings);
  }

  @Override
  public Item getVirtualItem() {
    return this.getModel().item();
  }

  @Override
  public int getCustomModelData(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
    return this.getModel().value();
  }

  @Override
  public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
    if (!(user instanceof ServerPlayerEntity player))
      return TypedActionResult.pass(ItemStack.EMPTY);

    ItemStack stack = player.getStackInHand(hand);
    var cast = user.raycast(5, 0, false);
    if (cast.getType() == HitResult.Type.BLOCK)
      return TypedActionResult.pass(stack);

    new BackpackGui(player, getSlots(), stack);
    return TypedActionResult.success(stack);
  }

  @Override
  public ActionResult useOnBlock(ItemUsageContext context) {
    if (!(context.getPlayer() instanceof ServerPlayerEntity player))
      return ActionResult.PASS;

    new BackpackGui(player, getSlots(), context.getStack());
    return ActionResult.SUCCESS;
  }

  @Override
  public ItemStack getVirtualItemStack(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
    ItemStack stack = VirtualItem.super.getVirtualItemStack(itemStack, player);
    NbtCompound nbt = itemStack.getNbt();

    if (nbt != null) {
      NbtCompound virtualNbt = stack.getOrCreateNbt();
      DefaultedList<ItemStack> list = DefaultedList.ofSize(getSlots(), ItemStack.EMPTY);
      Inventories.readNbt(nbt, list);
      NbtCompound newNbt = Inventories.writeNbt(virtualNbt, list);
      stack.setNbt(newNbt);
    }

    return stack;
  }
}