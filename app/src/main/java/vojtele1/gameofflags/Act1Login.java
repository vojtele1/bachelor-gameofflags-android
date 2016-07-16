package vojtele1.gameofflags;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.identitytoolkit.GitkitClient;
import com.google.identitytoolkit.GitkitUser;
import com.google.identitytoolkit.IdToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import vojtele1.gameofflags.utils.BaseActivity;
import vojtele1.gameofflags.utils.CustomRequest;
import vojtele1.gameofflags.utils.RetryingSender;

/**
 * Created by Leon on 25.10.2015.
 */
public class Act1Login extends BaseActivity {

    private String token;

    private GitkitClient client;


    private String nickname;
    private String newNickname;

    String adresa = "http://gameofflags-vojtele1.rhcloud.com/android/";

    String loginPlayer = adresa + "loginplayer";
    String changePlayerName = adresa + "changeplayername";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_welcome);


        // Step 1: Create a GitkitClient.
        // The configurations are set in the AndroidManifest.xml. You can also set or overwrite them
        // by calling the corresponding setters on the GitkitClient builder.
        //

        client = GitkitClient.newBuilder(this, new GitkitClient.SignInCallbacks() {
            // Implement the onSignIn method of GitkitClient.SignInCallbacks interface.
            // This method is called when the sign-in process succeeds. A Gitkit IdToken and the signed
            // in account information are passed to the callback.
            @Override
            public void onSignIn(IdToken idToken, GitkitUser user) {
                //showProfilePage(idToken, user);

                // Now use the idToken to create a session for your user.
                // To do so, you should exchange the idToken for either a Session Token or Cookie
                // from your server.
                // Finally, save the Session Token or Cookie to maintain your user's session.

                token = idToken.getTokenString();
                System.out.println(token);

                loginHrac();


            }

            // Implement the onSignInFailed method of GitkitClient.SignInCallbacks interface.
            // This method is called when the sign-in process fails.
            @Override
            public void onSignInFailed() {
                Toast.makeText(Act1Login.this, "Přihlášení se nezdařilo.", Toast.LENGTH_LONG).show();
            }
        }).build();

    }




    // Step 3: Override the onActivityResult method.
    // When a result is returned to this activity, it is maybe intended for GitkitClient. Call
    // GitkitClient.handleActivityResult to check the result. If the result is for GitkitClient,
    // the method returns true to indicate the result has been consumed.
    //

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (!client.handleActivityResult(requestCode, resultCode, intent)) {
            super.onActivityResult(requestCode, resultCode, intent);
        }

    }



    // Step 4: Override the onNewIntent method.
    // When the app is invoked with an intent, it is possible that the intent is for GitkitClient.
    // Call GitkitClient.handleIntent to check it. If the intent is for GitkitClient, the method
    // returns true to indicate the intent has been consumed.

    @Override
    protected void onNewIntent(Intent intent) {
        if (!client.handleIntent(intent)) {
            super.onNewIntent(intent);
        }
    }






    // Step 5: Respond to user actions.
    // If the user clicks sign in, call GitkitClient.startSignIn() to trigger the sign in flow.

    public void logIn(View view) {

            client.startSignIn();
    }

    private void loginHrac() {
        RetryingSender r = new RetryingSender(this) {
            public CustomRequest send() {
                knowResponse = false;
                knowAnswer = false;
                Map<String, String> params = new HashMap<>();
                params.put("token", token);

               return new CustomRequest(Request.Method.POST, loginPlayer, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println(response.toString());
                                knowResponse = true;

                                try {
                                    JSONArray players = response.getJSONArray("player");
                                    JSONObject player = players.getJSONObject(0);
                                    nickname = player.getString("nickname");
                                    String fraction_name;
                                    int fraction = player.optInt("player_fraction");
                                    if (fraction == 1) {
                                        fraction_name = "Red";
                                    } else {
                                        fraction_name = "Blue";
                                    }


                                    if (nickname.equals("user")) {
                                        zmenaJmena(fraction_name);
                                    } else {
                                        continueToWebview();
                                    }
                                    knowAnswer = true;
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
            }
        };
        r.startSender();
    }

    private void continueToWebview() {
        Intent intent = new Intent(this, Act2WebView.class);
        intent.putExtra("token", token);
        startActivity(intent);
    }

    private void zmenaJmena(final String fraction_name) {
        final EditText editText = new EditText(Act1Login.this);
        // filtr pro zadavani pouze cisel a pismen
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetterOrDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        editText.setFilters(new InputFilter[] { filter });

        new AlertDialog.Builder(Act1Login.this)
                .setTitle("Zadejte svůj nick:")
                .setView(editText)
                .setCancelable(false)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        newNickname = editText.getText().toString();
                        if (("user").equals(newNickname)) {
                            zmenaJmena(fraction_name);
                            Toast.makeText(Act1Login.this, "Nickname nesmí být slovo user.", Toast.LENGTH_LONG).show();
                        } else if (newNickname.length() >= 4) {
                            zmenaJmenaRequest(newNickname, fraction_name);
                        } else {
                            zmenaJmena(fraction_name);
                            Toast.makeText(Act1Login.this, "Nickname musí být alespoň 4 znaky.", Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }
                })
                .show();

    }
    private void zmenaJmenaRequest(final String nickname, final String fraction_name) {
        RetryingSender r = new RetryingSender(this) {
            public CustomRequest send() {
                knowResponse = false;
                knowAnswer = false;
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("nickname", nickname);

                return new CustomRequest(Request.Method.POST,  changePlayerName, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println(response.toString());
                            knowResponse = true;
                            try {
                                JSONArray players = response.getJSONArray("player");
                                JSONObject player = players.getJSONObject(0);
                                String nickname = player.getString("nickname");
                                if (nickname != null) {
                                   showInfoDialog("Vítej ve hře " + nickname + ", tvoje frakce je: "
                                           + fraction_name + "!", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialogInterface, int i) {
                                           dialogInterface.dismiss();
                                           continueToWebview();
                                       }
                                   });
                                }
                                knowAnswer = true;
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
            }
        };
        r.startSender();
    }

    @Override
    public void onBackPressed() {
        // zakomentovani zabrani reakci na stisk hw back
        //super.onBackPressed();
    }
}
