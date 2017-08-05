package com.example.supun.food;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ConfirmOrderActivity extends AppCompatActivity implements OnMapReadyCallback {


    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private JSONObject dataToSendToKitchen;
    private JSONObject dataToSendToBar;
    private SharedPreferences sharedPref;
    private View mProgressView;
    private Receiver receiver = new Receiver();
    private HashMap<String,OrderedItem> orderList;
    private Button confirmButton;

    protected int publishSuccessCount = 0;
    protected int publishCount = 0;

    private String tableId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_confirm_order);

//        TextView tableIdText = (TextView) ConfirmOrderActivity.this.findViewById(R.id.table_id_text);
//        tableId = getIntent().getStringExtra("TABLE_ID");
//        int tableNo = Integer.parseInt(tableId);
//        if(tableNo>99) {
//            TextView tableText = (TextView) ConfirmOrderActivity.this.findViewById(R.id.tableIdText);
//            tableText.setText("Room No:");
//        }

//        tableIdText.setText(tableId);

        orderList = (HashMap<String, OrderedItem>) getIntent().getSerializableExtra("ORDER");

        dataToSendToBar = new JSONObject();
        dataToSendToKitchen = new JSONObject();
        try {
//            dataToSendToBar.put("TABLE",tableIdText.getText());
//            dataToSendToKitchen.put("TABLE",tableIdText.getText());
            dataToSendToBar.put("WAITER",GlobalState.getCurrentUsername());
            dataToSendToKitchen.put("WAITER",GlobalState.getCurrentUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TableLayout table = (TableLayout)ConfirmOrderActivity.this.findViewById(R.id.order_table);
        DecimalFormat decim = new DecimalFormat("0.00");
        Log.d("confirmorder","item count: "+orderList.size());
        Double total=0.0;
        for(String key : orderList.keySet())
        {
            OrderedItem item = orderList.get(key);
            String itemName = item.getName();
            // Inflate your row "template" and fill out the fields.
            TableRow row = (TableRow) LayoutInflater.from(ConfirmOrderActivity.this).inflate(R.layout.order_row, null);
            ((TextView)row.findViewById(R.id.attrib_name)).setText(itemName);
            ((TextView)row.findViewById(R.id.attrib_value)).setText(decim.format(item.getPrice())+" x "+item.getQty()+" = "+decim.format(item.getPrice()*item.getQty()));
            table.addView(row);

            total+=(item.getPrice()*item.getQty());

            try {
                JSONObject menuItem = new JSONObject();
                menuItem.put(Constants.ITEM_QTY_KEY,orderList.get(key).getQty());
                menuItem.put(Constants.ITEM_ID_KEY,orderList.get(key).itemId);
                if(orderList.get(key).getPreparedIn()==Constants.KITCHEN) {
                    dataToSendToKitchen.put(itemName, menuItem);
                    Log.d("confirmorder","kitchenItem: "+menuItem);
                }
                else if(orderList.get(key).getPreparedIn()==Constants.BAR){
                    dataToSendToBar.put(itemName,menuItem);
                    Log.d("confirmorder","barItem: "+menuItem);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        TableRow row = (TableRow) LayoutInflater.from(ConfirmOrderActivity.this).inflate(R.layout.order_row, null);

        table.addView(row);
        row = (TableRow) LayoutInflater.from(ConfirmOrderActivity.this).inflate(R.layout.order_row, null);
        ((TextView)row.findViewById(R.id.attrib_name)).setText("Total");
        ((TextView)row.findViewById(R.id.attrib_name)).setTextSize(18);
        ((TextView)row.findViewById(R.id.attrib_name)).setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        ((TextView)row.findViewById(R.id.attrib_value)).setText(decim.format(total)+" LKR");
        ((TextView)row.findViewById(R.id.attrib_value)).setTextSize(18);
        ((TextView)row.findViewById(R.id.attrib_value)).setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        table.addView(row);
        table.requestLayout();     // Not sure if this is needed.

        getSupportActionBar().setTitle(Html.fromHtml("<font color=#8B0000>Order Confirmation</font>"));
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//
//        StrictMode.setThreadPolicy(policy);
//        sharedPref = getSharedPreferences("pref",Context.MODE_PRIVATE);





        confirmButton = (Button) findViewById(R.id.confirmButton);
//        try {
//            confirm.setOnClickListener(new View.OnClickListener() {
//
//                Socket s = new Socket("192.168.1.11", 8080);
//
//
//                @Override
//                public void onClick(View v) {
//                   // status.setText("...");
//                    PrintWriter outp = null;
//                    BufferedReader inp = null;
//                   // status.setText("Established connection..");
//                    String serverMsg = null;
//
//                    try {
//                        outp = new PrintWriter(s.getOutputStream(), true);
//                        inp = new BufferedReader(new InputStreamReader(s.getInputStream()));
//                        serverMsg = inp.readLine();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    //convo.append(serverMsg + "\n");
//
//
//                            try {
//
//                              //  convo.append(message + "\n");
//                                outp.println("order");
//                                serverMsg = inp.readLine();
//                                Log.d("communication",serverMsg);
//                              //  convo.append(serverMsg + "\n");
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        mProgressView = findViewById(R.id.login_progress);
// Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//        View view = findViewById(R.id.map_card);
////        view.requestLayout();
//        int visibility = view.getVisibility();
//        view.setVisibility(View.GONE);
////        view.setVisibility(visibility);

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng sydney = new LatLng(-33.85274, 151.21178);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        try {
            this.unregisterReceiver(receiver);
        }
        catch (IllegalArgumentException e){

        }

    }
    public void editOrder(View v){

        finish();
    }
    public void editLocation(View v){

    }
    public void confirmOrder(View v){

        showProgress(true);
        confirmButton.setEnabled(false);
        Log.d("confirmorder","confirm button clicked b "+dataToSendToBar.length()+" k "+dataToSendToBar.length());
        IntentFilter filter = new IntentFilter(Constants.MQTT_PUBLISH_STATE_ACTION);
        IntentFilter filter1 = new IntentFilter(Constants.MQTT_CONNECTION_STATE_ACTION);
        this.registerReceiver(receiver, filter);
        this.registerReceiver(receiver, filter1);

        try {
            //send notification
//            MQTTClient mqttClient = new MQTTClient();
//            if(dataToSendToKitchen.length()>2) {
//
//                Log.d("confirmorder","sending to kitchen");
//                mqttClient.initializeMQTTClient(this.getBaseContext(), "tcp://iot.eclipse.org:1883", "app:waiter"+GlobalState.getCurrentUsername(), false, false, null, null);
//
//                mqttClient.publish("new_order_kitchen", 2, dataToSendToKitchen.toString().getBytes());
//                publishCount++;
//
//            }
//            if(dataToSendToBar.length()>2) {
//                Log.d("confirmorder","sending to Bar");
//                mqttClient = new MQTTClient();
//
//                mqttClient.initializeMQTTClient(this.getBaseContext(), "tcp://iot.eclipse.org:1883", "app:waiter"+GlobalState.getCurrentUsername(), false, false, null, null);
//
//                mqttClient.publish("new_order_bar", 2, dataToSendToBar.toString().getBytes());
//                publishCount++;
//            }
        } catch (Throwable throwable) {
            Log.d("confirm_order","mqtt publish error: "+throwable.toString());
            throwable.printStackTrace();
        }
        Log.d("confirmorder","sent complete");
//        CommunicationService.sendToServer(this,dataToSend.toString(),Constants.ORDER_RECEIVED_ACTION);
//        try {
//
//
//            String  ip = sharedPref.getString(getString(R.string.ip_key), "192.168.8.100");
//
////            Socket s = new Socket("192.168.8.108", 8080);
//            Socket s = new Socket(ip, 8080);
//
//            PrintWriter outp = new PrintWriter(s.getOutputStream(), true);
//            BufferedReader inp = new BufferedReader(new InputStreamReader(s.getInputStream()));
//            String serverMsg;
////            JSONObject dataObj = new JSONObject(dataToSend);
//            Log.d("communication",dataToSend.toString());
//            outp.println(dataToSend.toString());
//            for(int i=0;i<5;i++) {
//                serverMsg = inp.readLine();
//                Log.d("communication", serverMsg);
//            }
//        } catch (IOException e) {
//            Toast toast = Toast.makeText(this,"Communication Error!",Toast.LENGTH_SHORT);
//            toast.show();
//            e.printStackTrace();
//            return;
//        }



    }

    public void orderSucceed(){



        Set<String> itemNames = orderList.keySet();
        ActiveOrderItem activeOrderItem = null;
        OrderedItem item;
        for (String itemName:itemNames){
            item = orderList.get(itemName);
            activeOrderItem = new ActiveOrderItem(itemName,String.valueOf(item.qty),Constants.ITEM_STATE_SENT,Integer.parseInt(tableId),item.orderId,item.itemId,item.price);
//            activeOrderItem.save();
        }
        List<ActiveOrder> existingOrder = ActiveOrder.findWithQuery(ActiveOrder.class, "SELECT * from ACTIVE_ORDER where TABLE_ID=" +tableId);

        if(existingOrder.size()==0) {
            ActiveOrder activeOrder = new ActiveOrder(tableId);
            activeOrder.save();
        }

        final ConfirmOrderActivity obj= this;
        showProgress(false);
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(obj, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(obj);
        }
        builder.setTitle("Order has been placed")
                .setMessage("You will receive a notification after the order has been prepared")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(obj, OrderActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    protected void unregisterReceiver(){
        this.unregisterReceiver(receiver);
    }

    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            showProgress(false);
            confirmButton.setEnabled(true);

            if(arg1.getAction().equals(Constants.MQTT_PUBLISH_STATE_ACTION)) {
//                unregisterReceiver(receiver);
                String response = arg1.getExtras().getString(Constants.RESPONSE_KEY);
                Log.d("mqtt", "Received to confirmOrderActivity: " + response);
                if (response != null && response.equals(Constants.MQTT_DELIVER_SUCCESS)) {
                    publishSuccessCount++;
                    if(publishCount==publishSuccessCount) {
                        UpdateBackendIntentService.startActionSendOrderToBackend(getApplicationContext(), orderList,Integer.parseInt(tableId));
                        orderSucceed();
                    }
//                    orderSucceed();
                }
                else if(response != null && response.equals(Constants.MQTT_PUBLISH_FAILED)){
                    Log.d("mqtt", "Received to confirmOrderActivity: error ");
                    Toast toast = Toast.makeText(getApplicationContext(), "Can't place the order due to connection error!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    Log.d("mqtt", "Received to confirmOrderActivity: error ");
                    Toast toast = Toast.makeText(getApplicationContext(), "Communication Error!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            else if(arg1.getAction().equals(Constants.MQTT_CONNECTION_STATE_ACTION)) {
                String response = arg1.getExtras().getString(Constants.RESPONSE_KEY);
                Log.d("mqtt", "Received to confirmOrderActivity: " + response);
                if (response != null && response.equals(Constants.MQTT_CONNECTION_FAILED)) {
                    Log.d("mqtt", "mqtt connection failed");
                    Toast toast = Toast.makeText(getApplicationContext(), "Can't place the order due to connection error!", Toast.LENGTH_SHORT);
                    toast.show();

                }
            }
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    protected void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
