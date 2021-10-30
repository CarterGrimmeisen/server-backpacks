package olivermakesco.de.servback;

import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;

public class BackpackGui extends SimpleGui {
  public final int slots;
  public ItemStack stack;
  public Inventory inventory;

  public BackpackGui(ServerPlayerEntity player, int slots, ItemStack stack) {
    super(getHandler(slots), player, false);
    this.slots = slots;
    this.stack = stack;
    this.setTitle(stack.getName());

    NbtCompound nbt = stack.getOrCreateNbt();
    DefaultedList<ItemStack> list = DefaultedList.ofSize(slots + 1, ItemStack.EMPTY);
    list.set(slots, stack);
    Inventories.readNbt(nbt, list);
    inventory = new SimpleInventory(list.toArray(ItemStack[]::new));

    for (int i = 0; i < slots; i++)
      setSlotRedirect(i, new BackpackSlot(inventory, i, i, 0));

    open();
    for (int i = 0; i < 9; i++)
      if (player.getInventory().getStack(i).equals(stack))
        screenHandler.setSlot(slots + 27 + i, new NopSlot(inventory, slots, slots, 0));
  }

  public static ScreenHandlerType<?> getHandler(int slots) {
    return switch (slots) {
    case 5 -> ScreenHandlerType.HOPPER;
    case 9 -> ScreenHandlerType.GENERIC_3X3;
    case 18 -> ScreenHandlerType.GENERIC_9X2;
    default -> null;
    };
  }

  @Override
  public void onClose() {
    DefaultedList<ItemStack> inv = DefaultedList.ofSize(slots, ItemStack.EMPTY);
    for (int i = 0; i < slots; i++) {
      ItemStack stack = getSlotRedirect(i).getStack();
      inv.set(i, stack);
    }
    NbtCompound root = stack.getNbt();
    NbtCompound newNbt = Inventories.writeNbt(root, inv);
    stack.setNbt(newNbt);
  }
}
