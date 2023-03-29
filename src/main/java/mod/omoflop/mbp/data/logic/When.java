package mod.omoflop.mbp.data.logic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mod.omoflop.mbp.data.BlockModelPredicate;
import mod.omoflop.mbp.data.WorldViewCondition;
import net.minecraft.block.BlockState;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class When implements WorldViewCondition {

    final And conditions;
    private final List<Identifier> applyModelList;

    public When(And conditions, List<Identifier> applyModelList) {
        this.conditions = conditions;
        this.applyModelList = Collections.unmodifiableList(applyModelList);
    }

    public Identifier getModel(long seed) {
        return applyModelList.get((int) (Math.abs(seed) % applyModelList.size()));
    }

    public List<Identifier> getModels() {
        return applyModelList;
    }

    public static When parse(JsonElement arg) {
        JsonObject object = arg.getAsJsonObject();
        List<BlockModelPredicate> conditions = BlockModelPredicate.parseFromJson(object.get("when"));

        List<Identifier> applyModelList;
        JsonElement apply = object.get("apply");
        if (apply.isJsonArray()) {
            applyModelList = new ArrayList<>();
            for (JsonElement entry : apply.getAsJsonArray()) {
                String applyId;
                int weight = 1;
                if (entry.isJsonObject()) {
                    JsonObject obj = entry.getAsJsonObject();
                    applyId = obj.get("model").getAsString();
                    if (obj.has("weight")) weight = obj.get("weight").getAsInt();
                } else {
                    applyId = entry.getAsString();
                }

                String[] id = applyId.split(":");
                Identifier currentModelID = new Identifier(id[0], "block/" + id[1]);
                for (int i = 0; i < weight; i++) {
                    applyModelList.add(currentModelID);
                }
            }
        } else {
            String[] id = apply.getAsString().split(":");
            applyModelList = List.of(new Identifier(id[0], "block/" + id[1]));
        }

        return new When(new And(conditions), applyModelList);
    }

    @Override
    public boolean meetsCondition(BlockView world, BlockPos pos, BlockState state, Identifier renderContext) {
        return conditions.meetsCondition(world, pos, state, renderContext);
    }

    public static class Template {
        enum ArgumentType implements StringIdentifiable {
            STRING,
            NUMBER,
            OBJECT,
            BOOLEAN;

            @Override
            public String asString() {
                return name().toLowerCase();
            }
        }

        public JsonElement jsonElement;

        public Template(JsonElement jsonElement) {
            this.jsonElement = jsonElement;
        }

        public When Build(JsonObject arguments) {
            var map = new HashMap<String, Function<JsonElement, Void>>();

            var entries = arguments.entrySet();
            for (var entry : entries) {
                String curArgumentName = entry.getKey();
                String curArgType = entry.getValue().getAsString();

                ArgumentType argType = ArgumentType.valueOf(curArgType);
                switch (argType) {
                    case STRING -> {
                        map.put("$"+curArgumentName+"$", el -> el.se )
                    }
                    case NUMBER -> {
                    }
                    case OBJECT -> {
                    }
                    case BOOLEAN -> {
                    }
                }
            }
        }
    }
}
