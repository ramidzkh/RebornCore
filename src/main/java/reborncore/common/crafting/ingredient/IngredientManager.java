package reborncore.common.crafting.ingredient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.function.Function;

public class IngredientManager {

	private static final Identifier DEFAULT_RECIPE_TYPE = new Identifier("reborncore", "stack");
	private static final HashMap<Identifier, Function<JsonObject, RebornIngredient>> recipeTypes = new HashMap<>();

	public static void setup(){
		recipeTypes.put(DEFAULT_RECIPE_TYPE, StackIngredient::deserialize);
	}

	public static RebornIngredient deserialize(@Nullable JsonElement jsonElement) {
		if(jsonElement == null || !jsonElement.isJsonObject()){
			throw new JsonParseException("ingredient must be a json object");
		}

		JsonObject json = jsonElement.getAsJsonObject();

		Identifier recipeTypeIdent = DEFAULT_RECIPE_TYPE;
		if(json.has("type")){
			recipeTypeIdent = new Identifier(JsonHelper.getString(json, "type"));
		}

		Function<JsonObject, RebornIngredient> recipeTypeFunction = recipeTypes.get(recipeTypeIdent);
		if(recipeTypeFunction == null){
			throw new JsonParseException("No recipe type found for " + recipeTypeIdent.toString());
		}
		return recipeTypeFunction.apply(json);
	}

}