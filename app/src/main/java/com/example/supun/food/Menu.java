package com.example.supun.food;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//import org.lorecraft.phparser.SerializedPhpParser;
//import org.lorecraft.phparser.SerializedPhpParserException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by asela on 5/17/17.
 */
public class Menu {

    private HashMap<Integer,String> menuOPtionsStringMap;

    public Menu(Context ctx){
//        WebServerCommunicationService.sendGetRequest(ctx,Constants.API_BASE_URL+Constants.API_CATEGORIES,Constants.CATEGORIES_UPDATE_ACTION);

    }
    String[] imageNames={"egg","fish","chicken","Vegetable"};
    String[] imageUrls = {"http://icons.iconarchive.com/icons/graphicloads/food-drink/256/egg-2-icon.png","http://www.mobilebaynep.com/assets/landing/Fish_Icon_trans.png","http://www.free-icons-download.net/images/grilled-chicken-icon-65997.png","http://urbanorganicsonline.com/wp-content/uploads/2015/03/UO-veggie-icon.png"};
    String[] itemNames = {"Egg","Fish","Chicken","Vegetable"};
    Double[] prices = {110.0,120.0,140.0,100.0};
    public void updateMenuItems(Context ctx) {

        List<MenuItem> items = new ArrayList<>();

        for(int i=0;i<itemNames.length;i++) {
            String imageName = imageNames[i];
            if (ImageStorage.checkifImageExists(imageName, ctx)) {
                File file = ImageStorage.getImage("/" + imageName, ctx);
                String path = file.getAbsolutePath();
                if (path != null) {
                    Log.d("image found", path);

                }
            } else {
                String imgurl = imageUrls[i];
                new GetImages(imgurl, imageName, ctx).execute();
            }
            items.add(new MenuItem(0, 0, itemNames[i], prices[i], imageName, 0, null));


        }

        MenuItem.deleteAll(MenuItem.class);
        MenuItem.saveInTx(items);


    }

    public void updateCategories(Context ctx) {
//        JSONArray menuArray = categoriesObject.getJSONObject(Constants.API_CATEGORIES).getJSONArray(Constants.RECORDS_KEY);
//
        List<Category> categoryArrayList = new ArrayList<>();
//
//        for(int i=0;i<menuArray.length();i++) {
//            JSONArray item = menuArray.getJSONArray(i);
//
//            if (item.getInt(6) == 1) {
//                String imageName = item.getString(5);
//                Log.d("Category", "imageName: " + imageName);
//                if (imageName != null && imageName != "") {
//                    try {
//                        imageName = imageName.split("/")[1].split("\\.")[0] + "-120x120." + imageName.split("/")[1].split("\\.")[1];
//                    } catch (ArrayIndexOutOfBoundsException ex) {
//                        imageName = "no_photo-120x120.png";
//                    }
//
//
//                    String imageName = "egg.png";
//                    if (ImageStorage.checkifImageExists(imageName, ctx)) {
//                        File file = ImageStorage.getImage("/" + imageName, ctx);
//                        String path = file.getAbsolutePath();
//                        if (path != null) {
//                            Log.d("image found", path);
//                            // b = BitmapFactory.decodeFile(path);
//                            //imageView.setImageBitmap(b);
//                        }
//                    } else {
//                        String imgurl = Constants.IMAGE_THUMBS_URL + imageName;
//                        new GetImages(imgurl, imageName, ctx).execute();
//                    }
//                }
                categoryArrayList.add(new Category(0,"Lunch","lunch", 0));
//                Log.d("adding category", item.getInt(0) + ":" + item.getString(1));
//            }
//        }
            Category.deleteAll(Category.class);
            Category.saveInTx(categoryArrayList);

        updateMenuItems(ctx);
//update menu options
//        WebServerCommunicationService.sendGetRequest(ctx,Constants.API_BASE_URL+Constants.API_OPTION_VALUES+"?include="+Constants.API_MENU_OPTIONS,Constants.OPTIONS_UPDATE_ACTION);


//        }
    }
    public void updateMenuOptions(Context ctx,JSONObject responseObject) throws JSONException {
        menuOPtionsStringMap =  new HashMap<>();

        JSONArray optionValuesArray = responseObject.getJSONObject(Constants.API_OPTION_VALUES).getJSONArray(Constants.RECORDS_KEY);
        HashMap<Integer,String> optionNames = new HashMap<>();
        //store option names
        for(int i=0;i<optionValuesArray.length();i++){
            optionNames.put(optionValuesArray.getJSONArray(i).getInt(0),optionValuesArray.getJSONArray(i).getString(2));
        }
        //get vales for options and add them to menu item
        JSONArray menuOptionsArray = responseObject.getJSONObject(Constants.API_MENU_OPTIONS).getJSONArray(Constants.RECORDS_KEY);
        for(int i=0; i<menuOptionsArray.length();i++){
            JSONArray menuOption = menuOptionsArray.getJSONArray(i);

            int menuId = menuOption.getInt(2);

            String data = menuOption.getString(5);//"a:2:{i:1;a:5:{s:15:\"option_value_id\";s:2:\"17\";s:5:\"price\";s:4:\"1900\";s:8:\"quantity\";s:0:\"\";s:14:\"subtract_stock\";s:1:\"0\";s:20:\"menu_option_value_id\";s:0:\"\";}i:2;a:5:{s:15:\"option_value_id\";s:2:\"18\";s:5:\"price\";s:3:\"950\";s:8:\"quantity\";s:0:\"\";s:14:\"subtract_stock\";s:1:\"0\";s:20:\"menu_option_value_id\";s:0:\"\";}}";
//            SerializedPhpParser phparser = new SerializedPhpParser(data);
            LinkedHashMap result = null;
//            try {
//                result = (LinkedHashMap)phparser.parse();
//            System.out.println("Price: "+((SerializedPhpParser.PhpObject)result.get(1)).attributes.get("price"));
//                System.out.println("size: "+result.size());
//                String options = "";
//                for(Object key:result.keySet()){
//                    //"option_name:value,option_name:value.."
//                    options += optionNames.get(Integer.parseInt(((LinkedHashMap)result.get(key)).get("option_value_id").toString()))+":"
//                            +((LinkedHashMap)result.get(key)).get("price")+",";
//
//                    System.out.println("option: "+options);
//
//                }
                //update menu option in DB
//                MenuItem item = MenuItem.findById(MenuItem.class,(long)menuId);
////                Log.d("update_options","menu item: "+item.name);
//                MenuItem.executeQuery("UPDATE MENU_ITEM" +
//                        " SET OPTIONS='"+options+"' " +
//                        "WHERE MENU_ID=" + menuId);

//                menuOPtionsStringMap.put(menuId,options);


//            } catch (SerializedPhpParserException e) {
//                System.out.println("SerializedPhpParserException:"+e);
//                e.printStackTrace();
//            }

        }

        Log.d("menu","Updating menu options");
        WebServerCommunicationService.sendGetRequest(ctx,Constants.API_BASE_URL+Constants.API_MENU,Constants.MENU_UPDATE_ACTION);

    }

}
