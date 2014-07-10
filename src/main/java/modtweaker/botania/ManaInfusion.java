package modtweaker.botania;

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
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;

@ZenClass("mods.botania.ManaInfusion")
public class ManaInfusion {
    @ZenMethod
    public static void addInfusion(IItemStack output, IIngredient input, int mana) {
        MineTweakerAPI.tweaker.apply(new Add(new RecipeManaInfusion(toStack(output), toObject(input), mana)));
    }
    
    @ZenMethod
    public static void addAlchemy(IItemStack output, IIngredient input, int mana) {
        RecipeManaInfusion recipe = new RecipeManaInfusion(toStack(output), toObject(input), mana);
        recipe.setAlchemy(true);
        MineTweakerAPI.tweaker.apply(new Add(recipe));
    }
    
    @ZenMethod
    public static void addConjuration(IItemStack output, IIngredient input, int mana) {
        RecipeManaInfusion recipe = new RecipeManaInfusion(toStack(output), toObject(input), mana);
        recipe.setConjuration(true);
        MineTweakerAPI.tweaker.apply(new Add(recipe));
    }

    private static class Add extends BaseListAddition {
        public Add(RecipeManaInfusion recipe) {
            super("Botania Mana Infusion", BotaniaAPI.manaInfusionRecipes, recipe);
        }

        @Override
        public String getRecipeInfo() {
            return ((RecipeManaInfusion) recipe).getOutput().getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        MineTweakerAPI.tweaker.apply(new Remove(toStack(output)));
    }

    private static class Remove extends BaseListRemoval {
        public Remove(ItemStack stack) {
            super("Botania Mana Infusion", BotaniaAPI.manaInfusionRecipes, stack);
        }

        @Override
        public void apply() {
            for (RecipeManaInfusion r : BotaniaAPI.manaInfusionRecipes) {
                if (r.getOutput() != null && r.getOutput().isItemEqual(stack)) {
                    recipe = r;
                    break;
                }
            }

            BotaniaAPI.manaInfusionRecipes.remove(recipe);
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }
}
