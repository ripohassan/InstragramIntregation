package com.mcc.instagramintegration.activity;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mcc.instagramintegration.Api.HttpParams;
import com.mcc.instagramintegration.Api.RectrofitClient;
import com.mcc.instagramintegration.R;
import com.mcc.instagramintegration.adapter.PostAdapter;
import com.mcc.instagramintegration.model.Datum;
import com.mcc.instagramintegration.model.InstaModel;
import com.mcc.instagramintegration.utilities.AppConfig;
import com.mcc.instagramintegration.utilities.InstagramApp;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private InstagramApp mApp;
    private Button btnConnect, btnView;
    private LinearLayout llAfterLoginView;
    private ArrayList<Datum> arrayList;
    private PostAdapter postAdapter;
    private HashMap<String, String> userInfoHashmap = new HashMap<String, String>();

    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == InstagramApp.WHAT_FINALIZE) {
                userInfoHashmap = mApp.getUserInfo();
                Log.d("TAG", mApp.getUserName() + "");
            } else if (msg.what == InstagramApp.WHAT_FINALIZE) {
                Toast.makeText(MainActivity.this, "Check your network.",
                        Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initVars();
        initListener();

        getUserPics();
    }


    private void initVars() {
        arrayList = new ArrayList<>();


    }

    private void initView() {

        setContentView(R.layout.activity_main);

        btnConnect = findViewById(R.id.btnConnect);
        btnView = findViewById(R.id.btnView);
        llAfterLoginView = findViewById(R.id.llafterloginView);
        mApp = new InstagramApp(this, AppConfig.CLIENT_ID,
                AppConfig.CLIENT_SECRET, AppConfig.CALLBACK_URL);
        mApp.setListener(new InstagramApp.OAuthAuthenticationListener() {

            @Override
            public void onSuccess() {
                btnConnect.setText("Disconnect");
                llAfterLoginView.setVisibility(View.VISIBLE);
                mApp.fetchUserName(handler);
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT)
                        .show();
            }
        });


        if (mApp.hasAccessToken()) {
            // tvSummary.setText("Connected as " + mApp.getUserName());
            btnConnect.setText("Disconnect");
            llAfterLoginView.setVisibility(View.VISIBLE);
            mApp.fetchUserName(handler);

        }
    }

    private void initListener() {

        btnConnect.setOnClickListener(this);
        btnView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConnect:
                loginUser();
                break;
            case R.id.btnView:
                displayPersonInfo();
                break;

            default:
                break;

        }
    }

    private void displayPersonInfo() {

     /*   AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Profile Info");
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.profile_view, null);
        builder.setView(view);*/

        ImageView imvProfilePic = findViewById(R.id.imvProfilePic);
        TextView tvName = findViewById(R.id.tvNofUserName);
        TextView tvBio = findViewById(R.id.tvNoofBio);
        TextView tvNoOfFollowers = findViewById(R.id.tvNoofFollower);
        TextView tvNoOfFollowing = findViewById(R.id.tvNoofFollowing);


        RecyclerView recyclerView = findViewById(R.id.rv_view);
        postAdapter = new PostAdapter(this, arrayList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(postAdapter);
        Glide.with(getApplicationContext()).load(userInfoHashmap.get(InstagramApp.TAG_PROFILE_PICTURE)).into(imvProfilePic);
        tvBio.setText(userInfoHashmap.get(InstagramApp.TAG_BIO));
        tvName.setText(userInfoHashmap.get(InstagramApp.TAG_USERNAME));
        tvNoOfFollowing.setText(userInfoHashmap.get(InstagramApp.TAG_FOLLOWS));
        tvNoOfFollowers.setText(userInfoHashmap.get(InstagramApp.TAG_FOLLOWED_BY));
        /* builder.create().show();*/


    }

    private void loginUser() {

        if (mApp.hasAccessToken()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    MainActivity.this);
            builder.setMessage("Disconnect from Instagram?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    mApp.resetAccessToken();
                                    // btnConnect.setVisibility(View.VISIBLE);
                                    llAfterLoginView.setVisibility(View.GONE);
                                    btnConnect.setText("Connect");
                                    // tvSummary.setText("Not connected");
                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
            final AlertDialog alert = builder.create();
            alert.show();
        } else {
            mApp.authorize();
        }
    }


    private void getUserPics() {
        RectrofitClient.getClient().getALLImages(HttpParams.ACCESS_TOKEN).enqueue(new Callback<InstaModel>() {
            @Override
            public void onResponse(Call<InstaModel> call, Response<InstaModel> response) {
                arrayList.clear();
                arrayList.addAll(response.body().getData());
            }

            @Override
            public void onFailure(Call<InstaModel> call, Throwable t) {
                Log.d("TAG", t.toString());
            }
        });
    }

}
