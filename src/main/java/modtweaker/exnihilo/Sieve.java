package modtweaker.exnihilo;

import static modtweaker.util.Helper.ItemStack;
import static modtweaker.util.Helper.isABlock;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker.util.BaseListAddition;
import modtweaker.util.BaseListRemoval;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import exnihilo.registries.SieveRegistry;
import exnihilo.registries.helpers.SiftReward;

@ZenClass("mods.exnihilo.Sieve")
public class Sieve {
    //Adding a Ex Nihilo Sieve recipe
    @ZenMethod
    public static void addRecipe(@NotNull IItemStack input, @NotNull IItemStack output, @NotNull int rarity) {
        if (isABlock(input)) {
            Block theBlock = Block.getBlockFromItem(ItemStack(input).getItem());
            int theMeta = ItemStack(input).getItemDamage();
            MineTweakerAPI.tweaker.apply(new Add(new SiftReward(theBlock, theMeta, ItemStack(output).getItem(), ItemStack(output).getItemDamage(), rarity)));
        }
    }

    //Passes the list to the base list implementation, and adds the recipe
    private static class Add extends BaseListAddition {
        public Add(SiftReward recipe) {
            super("ExNihilo Sieve", SieveRegistry.rewards, recipe);
        }

        @Override
        public String getRecipeInfo() {
            return new ItemStack(((SiftReward) recipe).source, 1, ((SiftReward) recipe).sourceMeta).getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Removing a Ex Nihilo Sieve recipe
    @ZenMethod
    public static void removeRecipe(@NotNull IItemStack output) {
        MineTweakerAPI.tweaker.apply(new Remove(ItemStack(output)));
    }

    //Removes a recipe, apply is never the same for anything, so will always need to override it
    private static class Remove extends BaseListRemoval {
        public Remove(ItemStack stack) {
            super("ExNihilo Sieve", SieveRegistry.rewards, stack);
        }

        //Loops through the registry, to find the item that matches, saves that recipe then removes it
        @Override
        public void apply() {
            for (SiftReward r : SieveRegistry.rewards) {
                ItemStack stack = new ItemStack(r.item, 1, r.meta);
                if (stack != null && stack.isItemEqual(stack)) {
                    recipe = r;
                    break;
                }
            }

            SieveRegistry.rewards.remove(recipe);
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }
}