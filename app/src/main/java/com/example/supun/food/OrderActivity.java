package com.example.supun.food;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OrderActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

        private Toolbar toolbar;
        private TabLayout tabLayout;
        private ViewPager viewPager;

        private Order order;

        private ArrayList<Drawable> icons;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_order);

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        getActionBar().setTitle("Silver Ring Village Hotel");
            getSupportActionBar().setTitle(Html.fromHtml("<font color=#8B0000>Order</font>"));
            viewPager = (ViewPager) findViewById(R.id.viewpager);
           new Menu(getBaseContext()).updateCategories(getBaseContext());



            icons = new ArrayList<>();

                setupViewPager(viewPager);

            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
            setupTabIcons();

            order = new Order();
            order.setTableId("0");
        }

    private void setupTabIcons() {
//        File path = ImageStorage.getImage("ic_salad-120x120.png", getApplicationContext());
//        BitmapFactory.Options options = new BitmapFactory.Options();
////        options.inDither = true;
////        options.inPreferredConfig = Bitmap.Config.ALPHA_8;
//
//        Bitmap b = BitmapFactory.decodeFile(path.getAbsolutePath());
//        Drawable d = new BitmapDrawable(getApplicationContext().getResources(),b);
        for(int i=0;i<icons.size();i++)
            tabLayout.getTabAt(i).setIcon(icons.get(i));

//        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
//        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
//        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        try {


            Iterator<Category> categoryIterator = Category.findAll(Category.class);
            Category category;
//        Log.d("add menu item",String.valueOf(MenuItem.findById(MenuItem.class,(long)0).price));
            int i=0;
            while (categoryIterator.hasNext()) {
                category = categoryIterator.next();
                try {

                    Log.d("menuActivity", "get category: "+category.categoryId+":"+category.name);

                    TabFragment fragment = new TabFragment();
                    Bundle args = new Bundle();
                    args.putInt("index", category.categoryId);
                    fragment.setArguments(args);
                    adapter.addFrag(fragment, category.name);

                    File path = ImageStorage.getImage(category.imageName, getApplicationContext());
                    Bitmap b = BitmapFactory.decodeFile(path.getAbsolutePath());
                    Drawable d =new BitmapDrawable(b);
                    icons.add(d);
//                    tabLayout.getTabAt(i++).setIcon(d);


                } catch (NullPointerException ex) {
                    Log.d("add menu item error", ex.toString());
                }
            }


        }
        catch (android.database.sqlite.SQLiteException ex){
            Toast.makeText(this,"No Data",Toast.LENGTH_SHORT).show();
        }
//        adapter.addFrag(new OneFragment(), "APPETIZERS");
//        adapter.addFrag(new TabFragment(), "SALAD");
//        adapter.addFrag(new SoupFragment(), "SOUP");
//        adapter.addFrag(new SandwichFragment(), "SANDWICH");
//        adapter.addFrag(new TwoFragment(), "TWO");
//        adapter.addFrag(new ThreeFragment(), "THREE");
//        adapter.addFrag(new FourFragment(), "FOUR");
//        adapter.addFrag(new FiveFragment(), "FIVE");
//        adapter.addFrag(new SixFragment(), "SIX");
//        adapter.addFrag(new SevenFragment(), "SEVEN");
//        adapter.addFrag(new EightFragment(), "EIGHT");
//        adapter.addFrag(new NineFragment(), "NINE");
//        adapter.addFrag(new TenFragment(), "TEN");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }


    }
    public void orderButtonClickHandler(View v){


//        String message = editText.getText().toString();
        if(order.getOrder().size()>0) {
            Intent intent = new Intent(this, ConfirmOrderActivity.class);
            intent.putExtra("ORDER", order.getOrder());
            intent.putExtra("TABLE_ID", order.getTableId());
            startActivity(intent);
        }
        else{
            Toast toast = Toast.makeText(this,"Nothing to order",Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    public void addButtonClickHandler(View v){
        String item =((TextView)((LinearLayout)((ViewGroup)v.getParent().getParent().getParent()).getChildAt(1)).getChildAt(0)).getText().toString();
        Log.d("menu",item);

        // ((TextView)((LinearLayout)((ViewGroup)v.getParent().getParent().getParent()).getChildAt(1)).getChildAt(0)).getText().toString();


        ((TextView) ((ViewGroup) ((ViewGroup) v.getParent().getParent().getParent()).getChildAt(3)).getChildAt(0)).setText(String.valueOf(order.addItem(item)));
//        ((TextView) ((ViewGroup) ((ViewGroup) v.getParent().getParent().getParent()).getChildAt(3)).getChildAt(0)).setText(String.valueOf(4));
//        Spinner optionSpinner = ((Spinner)((LinearLayout)((ViewGroup)v.getParent().getParent().getParent())).getChildAt(2));
//
//        if(optionSpinner.getSelectedItem()!=null) {
//            String optionName = optionSpinner.getSelectedItem().toString();
//            String optionValue = (String) ((StringWithTag)optionSpinner.getSelectedItem()).tag;
//            order.setOption(item,optionName,Double.parseDouble(optionValue));
//            Log.d("menu", "optionsName: " + optionName+" optionValue:"+optionValue);
//        }

    }
    public void removeButtonClickHandler(View v){
        String item =((TextView)((LinearLayout)((ViewGroup)v.getParent().getParent().getParent()).getChildAt(1)).getChildAt(0)).getText().toString();
        Log.d("menu",item);
        int newCount = order.removeItem(item);
        if(newCount>0) {
            ((TextView) ((ViewGroup) ((ViewGroup) v.getParent().getParent().getParent()).getChildAt(3)).getChildAt(0)).setText(String.valueOf(newCount));
        }
        else
            ((TextView)((ViewGroup)((ViewGroup)v.getParent().getParent().getParent()).getChildAt(3)).getChildAt(0)).setText("");
    }

    int prevPos = -1;
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

        if(prevPos!=pos){
            prevPos = pos;

        }
        StringWithTag s = (StringWithTag) parent.getItemAtPosition(pos);
        Double price = Double.parseDouble(s.tag.toString());
        DecimalFormat decim = new DecimalFormat("0.00");

        String item =((TextView)((LinearLayout)((ViewGroup)parent.getParent()).getChildAt(1)).getChildAt(0)).getText().toString();

        //  priceText.setText(decim.format(Double.parseDouble(optionPriceMap.get(pos))) + " LKR");
        ((TextView) ((ViewGroup) ((ViewGroup) parent.getParent()).getChildAt(1)).getChildAt(1)).setText( decim.format(price)+" LKR");



        Log.d("menu", "item selected: "+ parent.getSelectedItem()+ " "+ pos+" id: "+id);

        Log.d("menu",item);
        // order.setOption(item,s.toString(),price);
        //int newCount = order.removeItem(item);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
