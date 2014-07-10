package modtweaker.thaumcraft;

import static modtweaker.util.Helper.toObject;
import static modtweaker.util.Helper.toStack;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker.util.BaseListAddition;
import modtweaker.util.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.CrucibleRecipe;

@ZenClass("mods.thaumcraft.Crucible")
public class Crucible {
    @ZenMethod
    public static void addRecipe(String key, IItemStack result, IIngredient catalyst, String aspects) {
        MineTweakerAPI.tweaker.apply(new Add(new CrucibleRecipe(key, toStack(result), toObject(catalyst), ThaumcraftHelper.parseAspects(aspects))));
    }

    private static class Add extends BaseListAddition {
        public Add(CrucibleRecipe recipe) {
            super("Thaumcraft Crucible", ThaumcraftApi.getCraftingRecipes(), recipe);
        }

        @Override
        public String getRecipeInfo() {
            return ((CrucibleRecipe) recipe).getRecipeOutput().getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        MineTweakerAPI.tweaker.apply(new Remove(toStack(output)));
    }

    private static class Remove extends BaseListRemoval {
        public Remove(ItemStack stack) {
            super("Thaumcraft Crucible", ThaumcraftApi.getCraftingRecipes(), stack);
        }

        @Override
        public void apply() {
            for (Object o : ThaumcraftApi.getCraftingRecipes()) {
                if (o instanceof CrucibleRecipe) {
                    CrucibleRecipe r = (CrucibleRecipe) o;
                    if (r.getRecipeOutput() != null && r.getRecipeOutput().isItemEqual(stack)) {
                        recipe = r;
                        break;
                    }
                }
            }

            ThaumcraftApi.getCraftingRecipes().remove(recipe);
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }
}
