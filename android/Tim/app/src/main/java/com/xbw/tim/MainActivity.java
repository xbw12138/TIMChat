package com.xbw.tim;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMValueCallBack;
import java.io.IOException;
import java.util.List;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public String tag="xbw12138";
    private EditText etimport;
    private Button btnimport;
    private EditText etlogin;
    private Button btnlogin;
    private EditText etsend;
    private Button btnsend;
    private EditText etmessgae;
    private EditText etreceive;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;
        etimport=(EditText)findViewById(R.id.et_import);
        etlogin=(EditText)findViewById(R.id.et_login);
        etsend=(EditText)findViewById(R.id.et_send);
        btnimport=(Button)findViewById(R.id.btn_import);
        btnlogin=(Button)findViewById(R.id.btn_login);
        btnsend=(Button)findViewById(R.id.btn_send);
        etreceive=(EditText)findViewById(R.id.et_receive);
        etmessgae=(EditText)findViewById(R.id.et_message);
        btnimport.setOnClickListener(this);
        btnlogin.setOnClickListener(this);
        btnsend.setOnClickListener(this);
        receiveMessage(etmessgae);
    }

    public void receiveMessage(final EditText editText) {
        TIMManager.getInstance().addMessageListener(new TIMMessageListener() {
            @Override
            public boolean onNewMessages(List<TIMMessage> list) {
                Log.i(tag, "new message");
                for (TIMMessage msg : list) {
                    for (int i = 0; i < msg.getElementCount(); ++i) {
                        TIMElem elem = msg.getElement(i);
                        TIMElemType elemType = elem.getType();
                        Log.d(tag, "elem type: " + elemType.name());
                        if (elemType == TIMElemType.Text) {
                            TIMTextElem textElem = (TIMTextElem) elem;
                            editText.getText().append("\n" + textElem.getText());
                            Log.i(tag, textElem.getText());
                        } else if (elemType == TIMElemType.Image) {
                        }
                    }
                }
                return false;
            }
        });
    }
    //导入独立账号到腾讯云通信
    public void importUserid(String userid){
        OkHttpClient okHttpClient = new OkHttpClient();
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(Config.API_IMPORT_USERID+"?userid="+userid)
                .build();
        okhttp3.Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        T.showShort(mContext,getString(R.string.importfail));
                        Log.d(tag,"import userid fail");
                    }
                });
            }
            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        T.showShort(mContext,getString(R.string.importsucc));
                        Log.d(tag,"import userid success");
                    }
                });
            }
        });
    }
    //从服务器获取usersig，成功后登录
    public void getUserSig(final String userid){
        OkHttpClient okHttpClient = new OkHttpClient();
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(Config.API_GET_USERSIG+"?userid="+userid)
                .build();
        okhttp3.Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {
                TimLogin(userid,response.body().string());
            }
        });
    }
    //登录腾讯云通信
    public void TimLogin(String userid,String usersig){
        TIMManager.getInstance().login(userid, usersig, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                T.showShort(mContext,getString(R.string.loginfail)+code+desc);
                Log.d(tag, "login failed. code: " + code + " errmsg: " + desc);
            }

            @Override
            public void onSuccess() {
                T.showShort(mContext,getString(R.string.loginsucc));
                Log.d(tag, "login succ");
            }
        });
    }
    //注销腾讯云通信
    public void Timlogout(){
        TIMManager.getInstance().logout(new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {

                Log.d(tag, "logout failed. code: " + code + " errmsg: " + desc);
            }
            @Override
            public void onSuccess() {
            }
        });
    }

    public void sendMessage(String userid,String content) {
        TIMConversation conversation = TIMManager.getInstance().getConversation(
                TIMConversationType.C2C,
                userid);
        TIMMessage msg = new TIMMessage();
        TIMTextElem elem = new TIMTextElem();
        elem.setText(content);
        if(msg.addElement(elem) != 0) {
            Log.d(tag, "addElement failed");
            return;
        }
        conversation.sendMessage(msg,new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int code, String desc) {
                T.showShort(mContext,getString(R.string.sendfail)+code+desc);
                Log.d(tag, "send message failed. code: " + code + " errmsg: " + desc);
            }

            @Override
            public void onSuccess(TIMMessage msg) {
                T.showShort(mContext,getString(R.string.sendsucc));
                Log.e(tag, "SendMsg ok");
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_import:
                if(!etimport.getText().toString().equals("")) {
                    importUserid(etimport.getText().toString());
                }
                break;
            case R.id.btn_login:
                if(!etlogin.getText().toString().equals("")) {
                    getUserSig(etlogin.getText().toString());
                }
                break;
            case R.id.btn_send:
                if(!etreceive.getText().toString().equals("")&&!etsend.getText().toString().equals("")) {
                    sendMessage(etreceive.getText().toString(),etsend.getText().toString());
                }
                break;
        }
    }
}

