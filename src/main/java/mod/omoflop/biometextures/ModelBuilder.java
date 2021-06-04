package mod.omoflop.biometextures;

public class ModelBuilder {

    public static String createItemModelJson(String id, String type) {
        if ("generated".equals(type) || "handheld".equals(type)) {
        //The two types of items. "handheld" is used mostly for tools and the like, while "generated" is used for everything else. 
            return "{\n" +
                   "  \"parent\": \"item/" + type + "\",\n" +
                   "  \"textures\": {\n" +
                   "    \"layer0\": \"minecraft:item/" + id + "\"\n" +
                   "  }\n" +
                   "}";
        } else if ("block".equals(type)) {
        //However, if the item is a block-item, it will have a different model json than the previous two.
            return "{\n" +
                   "  \"parent\": \"example_mod:block/" + id + "\"\n" +
                   "}";
        }
        else {
        //If the type is invalid, return an empty json string.
            return "";
        }
    }
    
}
