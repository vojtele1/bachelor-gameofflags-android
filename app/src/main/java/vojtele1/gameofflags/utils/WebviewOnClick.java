package vojtele1.gameofflags.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import vojtele1.gameofflags.R;

/**
 * Created by Leon on 31.05.2016.
 */
public class WebviewOnClick {
    Context context;

    String adresa = "http://gameofflags-vojtele1.rhcloud.com/android/";
    String getFlagInfoUser = adresa + "getflaginfouser";

    TextView tvFractionName, tvPlayerName, tvFlagName, tvFlagWhen, tvWhatToDo;
    RequestQueue requestQueue;
    String cas,responseFrName, responsePName, responseFlName, whatToDo;

    PopupWindow popUp;
    View popUpView;

    int counterError;
    boolean knowAnswer;
    boolean knowResponse;

    ProgressDialog progressDialog;

    /**
     * Umozni nacitat a ukladat hodnoty do pameti
     */
    private SharedPreferences sharedPreferences;

    private String token;

    public WebviewOnClick(Context context) {
        this.context = context;

        requestQueue = Volley.newRequestQueue(context);
        popUp = new PopupWindow(context);

        // Retrieve an instance of the SharedPreferences object.
        sharedPreferences = context.getSharedPreferences(C.SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);

        // Get the value of token from SharedPreferences. Set to "" as a default.
        token = sharedPreferences.getString(C.TOKEN, "");

    }
    @JavascriptInterface
    public void showPopup(String id) {
        popUpView = LayoutInflater.from(context).inflate(R.layout.flag_info, null);
        tvFractionName = (TextView) popUpView.findViewById(R.id.flag_info_textView_fraction);
        tvPlayerName = (TextView) popUpView.findViewById(R.id.flag_info_textView_flag_owner);
        tvFlagName = (TextView) popUpView.findViewById(R.id.flag_info_textView_flag_name);
        tvFlagWhen = (TextView) popUpView.findViewById(R.id.flag_info_textView_flagWhen);
        tvWhatToDo = (TextView) popUpView.findViewById(R.id.flag_info_whatToDo);

        popUp.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popUp.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popUp.setContentView(popUpView);
        // Closes the popup window when touch outside.
        popUp.setOutsideTouchable(true);
        popUp.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
        popUp.setFocusable(true);
        //popUp.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFAB00

        popUp.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_popup_orange_with_dark_orange_border));

        popUp.showAtLocation(popUpView, Gravity.CENTER, 0, 0);
        zjisti(id);
    }

    private void zjisti(final String id) {
        if (progressDialog == null || !progressDialog.isShowing()) {
            showProgressDialogLoading(context);
            showFlag(id);
            System.out.println("zapinam progress v showFlag(WebviewOnClick)");
            counterError = 0;
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (knowResponse) {
                    System.out.println("Pocet chyb: " + counterError);
                    if (counterError >= 200) {
                        System.out.println("Konec, spravna odpoved bohuzel nedosla (same errory).");
                        new AlertDialog.Builder(context)
                                .setMessage("Problém s připojením k databázi, zkuste to prosím později.")
                                .setCancelable(false)
                                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Handler mainHandler = popUpView.getHandler();
                                        Runnable myRunnable = new Runnable() {
                                            @Override
                                            public void run() {
                                                popUp.dismiss();
                                            }
                                        };
                                        mainHandler.post(myRunnable);
                                    }
                                })
                                .show();
                    } else if (!knowAnswer) {
                        showFlag(id);
                        zjisti(id);
                    } else {
                        progressDialog.dismiss();
                        System.out.println("Loading dokoncen.");
                    }
                } else {
                    zjisti(id);
                }
            }
        }, 100);
    }

    private void showFlag(final String idFlag) {
        knowResponse = false;
        knowAnswer = false;

            Map<String, String> params = new HashMap();
            params.put("token", token);
            params.put("ID_flag", idFlag);
            CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, getFlagInfoUser, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            knowResponse = true;
                            System.out.println(response.toString());
                            try {
                                JSONArray flagsJson = response.getJSONArray("flag");
                                JSONObject flagJson = flagsJson.getJSONObject(0);
                                JSONObject time = flagJson.getJSONObject("flagWhen");
                                String flagDate = time.getString("date");
                                responseFlName = flagJson.getString("flagName");
                                responsePName = flagJson.getString("playerName");
                                responseFrName = flagJson.getString("fractionName");
                                String flagMe = flagJson.getString("flagMe");
                                String fractionMe = flagJson.getString("fractionMe");
                                //zmena formatu casu
                                SimpleDateFormat sdfPrijaty = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                SimpleDateFormat sdfVysledny = new SimpleDateFormat("dd. MM. yyyy HH:mm:ss");
                                // nastavi prijaty cas na UTC
                                sdfPrijaty.setTimeZone(TimeZone.getTimeZone("UTC"));

                                try {
                                    Date date = sdfPrijaty.parse(flagDate);
                                    // preformatuje prijaty cas do bezneho ciselneho tvaru
                                    cas = sdfVysledny.format(date.getTime());


                                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                    long dateFlagChange = date.getTime();
                                    // ziskani aktualniho casu
                                    Long dateNow = new Date().getTime();
                                    if (flagMe.equals("true") && !fractionMe.equals("true")) {
                                        whatToDo = "zabrána tebou za druhou frakci.";
                                    } else if (flagMe.equals("true")) {
                                        whatToDo = "zabrána tebou.";
                                    } else if (fractionMe.equals("true")) {
                                        whatToDo = "patří tvé frakci.";
                                    } else if (dateNow < dateFlagChange + C.FLAG_IMMUNE_TIME) {
                                        whatToDo = "čerstvě zabrána, počkej do " + sdf.format(date.getTime()) + ".";
                                    } else {
                                        whatToDo = "zaber ji!";
                                    }


                                    setText();
                                    knowAnswer = true;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.append(error.getMessage());
                    knowResponse = true;
                    counterError++;
                }
            });
            requestQueue.add(jsObjRequest);
    }

    private void setText() {
        Handler mainHandler = popUpView.getHandler();
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                tvFlagName.setText(responseFlName);
                tvPlayerName.setText(responsePName);
                tvFractionName.setText(responseFrName);
                tvFlagWhen.setText(cas);
                tvWhatToDo.setText(whatToDo);

                progressDialog.dismiss();
            }
        };
        mainHandler.post(myRunnable);
    }
    public ProgressDialog showProgressDialogLoading(Context context) {

        progressDialog = new ProgressDialog(context);

        progressDialog.show();

        progressDialog.setCancelable(false);

        progressDialog.setCanceledOnTouchOutside(false);

        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        progressDialog.setContentView(R.layout.progress_dialog_loading);

        return progressDialog;
    }
}