package com.tttlive.education.ui.room;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tttlive.basic.education.R;
import com.tttlive.education.adapter.ChatAdapter;
import com.tttlive.education.base.BaseFragment;
import com.tttlive.education.constant.Constant;
import com.tttlive.education.ui.room.bean.SendTextMessageBean;
import com.tttlive.education.util.LocalSharedPreferencesStorage;
import com.wushuangtech.bean.LocalAudioStats;
import com.wushuangtech.bean.LocalVideoStats;
import com.wushuangtech.bean.RemoteAudioStats;
import com.wushuangtech.bean.RemoteVideoStats;
import com.wushuangtech.room.core.EnterUserInfo;
import com.wushuangtech.room.core.RoomLiveHelp;
import com.wushuangtech.room.core.RoomLiveInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by mr.li on 2018/3/6.
 * 互动聊天界面
 */
public class ChatFragment extends BaseFragment implements RoomLiveInterface, View.OnClickListener {
    @BindView(R.id.bt_chat_message)
    Button mBtChatMessage;
    @BindView(R.id.rl_course_room)
    RelativeLayout mRlCourseRoom;
    @BindView(R.id.room_im_recycler_view)
    RecyclerView mImRecyclerView;
    private boolean isSetEnabled;

    private RoomLiveHelp mRoomLiveHelp;
    private MBtMessageOnClickListener mBtMessageOnClickListener = new MBtMessageOnClickListener();
    private EtOnEditorActionListener etOnEditorActionListener = new EtOnEditorActionListener();

    private EditText dialogEditText;
    private LinearLayout dialog_ll_chat;
    private Button dialog_bt_send;
    private Dialog dialog;

    private ArrayList<SendTextMessageBean> msgList=new ArrayList<>();
    private Gson chatGson = new Gson();
    private ChatAdapter adapter;

    public static ChatFragment newInstance() {
        Bundle args = new Bundle();
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_courseim;
    }

    @Override
    protected void initViews(View view) {
        EventBus.getDefault().register(this);
        mRoomLiveHelp = new RoomLiveHelp(this, mContext);
        mBtChatMessage.setOnClickListener(mBtMessageOnClickListener);
        mImRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        LocalSharedPreferencesStorage.getConfigStrValue(mContext , "teacher_user_id");
    }

    @Subscribe
    public void onEvent(MessageEvent messageEvent) {
        if (messageEvent.getType().equals(Constant.SEND_EVENT_TEXT)) {
            receiveTextMessage(messageEvent.getMessage());
        }else if (messageEvent.getType().equals(Constant.SEND_EVENT_GAG_REG)){
            //禁言
            mBtChatMessage.setBackground(getResources().getDrawable(R.drawable.icon_chat_disable));
            isSetEnabled = true;

        }else if (messageEvent.getType().equals(Constant.SEND_EVENT_REMOVE_GAG_RER)){
            //解除禁言
            mBtChatMessage.setBackground(getResources().getDrawable(R.drawable.icon_chat_normal));
            isSetEnabled = false;

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mRoomLiveHelp.exitHelp();
    }

    private Dialog inputBoxDialog() {

        dialog = new Dialog(mContext, R.style.style_dialog);
        dialog.setContentView(R.layout.room_live_chat_utile);

        dialogEditText = dialog.findViewById(R.id.room_edt_chat);
        dialog_ll_chat = dialog.findViewById(R.id.ll_dialog_chat);
        dialog_bt_send = dialog.findViewById(R.id.bt_send);
        dialog_bt_send.setOnClickListener(this);

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.BOTTOM);
        lp.width = width; // 宽度
        dialogWindow.setAttributes(lp);

        return dialog;


    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_send:

                String diaEdaitTex = dialogEditText.getText().toString().trim();
                if (TextUtils.isEmpty(diaEdaitTex)) {
                    Toast.makeText(mContext, getResources().getString(R.string.input_out_empty), Toast.LENGTH_SHORT).show();
                    return;
                }

//                sendTextMessage(diaEdaitTex);
                dialogEditText.setText("");
                dialog_ll_chat.setVisibility(View.GONE);
                dialog.dismiss();
                break;
        }
    }

    /**
     * 发送消息
     * @param message
     */
    private void sendTextMessage(String message , int messageType) {
        Long ct = System.currentTimeMillis();
        String seqid = "binding_"+String.valueOf(ct);

        String user_name = LocalSharedPreferencesStorage.getConfigStrValue(mContext , "teacher_user_name");
        String user_avatar = LocalSharedPreferencesStorage.getConfigStrValue(mContext , "teacher_user_arar");
        String room_id = LocalSharedPreferencesStorage.getConfigStrValue(mContext , "teacher_room_id");
        String user_id = LocalSharedPreferencesStorage.getConfigStrValue(mContext , "teacher_user_id");

        String teacher_id = LocalSharedPreferencesStorage.getConfigStrValue(mContext,"teacher_user_id_teacher");


        Gson gson = new Gson();
        SendTextMessageBean stMessAge = new SendTextMessageBean();
        SendTextMessageBean.DataBean stDb = new SendTextMessageBean.DataBean();
        stMessAge.setMessageType(Constant.BARRAGE_REQ);
        stDb.setMessage(message);
        stDb.setNickName(user_name);
        stDb.setAvatar(user_avatar);
        stDb.setUserId(user_id);
        stDb.setRoomId(room_id);
        stDb.setType(messageType);
        stMessAge.setData(stDb);
        String ss = gson.toJson(stMessAge);
        msgList.add(stMessAge);
        adapter = new ChatAdapter(getContext(),msgList);
        mImRecyclerView.setAdapter(adapter);

        //   mRoomLiveHelp.sendMessage(0, 1, seqid, ss);
        Constant. wsService.sendRequest(BaseLiveActivity.wsSendMsg(Integer.parseInt(room_id), Integer.parseInt(user_id),1,ss,seqid,0, true));

    }


    public void receiveTextMessage(String data){
        Gson gson=new Gson();
        SendTextMessageBean sendmsg= gson.fromJson(data,SendTextMessageBean.class);
        msgList.add(sendmsg);

        adapter = new ChatAdapter(getContext(),msgList);
        mImRecyclerView.setAdapter(adapter);
        mImRecyclerView.scrollToPosition(adapter.getItemCount()-1);


    }



    /**
     * 弹出输入框按钮监听
     */
    private class MBtMessageOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (isSetEnabled) {
                toastShort("您已被禁止发言");
                return;
            }
            Dialog inputDialog = inputBoxDialog();
            inputDialog.show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showSoftInputFromWindow(mContext, dialogEditText);

                }
            }, 100);

            dialogEditText.setOnEditorActionListener(etOnEditorActionListener);
        }
    }

    /**
     * 软键盘发送按钮监听
     */
    private class EtOnEditorActionListener implements TextView.OnEditorActionListener {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                ((BaseLiveActivity) getActivity()).showKeyboard();
                dialog_ll_chat.setVisibility(View.GONE);
                dialog.dismiss();
                String diaMessage = dialogEditText.getText().toString().trim();

                if (TextUtils.isEmpty(diaMessage)) {
                    Toast.makeText(mContext, getResources().getString(R.string.input_out_empty), Toast.LENGTH_SHORT).show();
                    return false;
                }

//                sendTextMessage(diaMessage);
                dialogEditText.setText("");
                return true;
            }
            return false;
        }
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        ((BaseLiveActivity) activity).showKeyboard();
    }


    @Override
    public void enterRoomSuccess() {

    }

    @Override
    public void enterRoomFailue(int error) {

    }

    @Override
    public void onDisconnected(int errorCode) {

    }

    @Override
    public void onMemberExit(long userId) {

    }

    @Override
    public void onMemberEnter(long userId, EnterUserInfo userInfo , String scum) {

    }

    @Override
    public void onHostEnter(long userId, EnterUserInfo userInfo , String scum) {

    }

    @Override
    public void onUpdateLiveView(List<EnterUserInfo> userInfos) {

    }

    @Override
    public void dispatchMessage(long srcUserID, int type, String sSeqID, String data) {

    }

    @Override
    public void sendMessageResult(int resultType, String data) {

    }

    @Override
    public void localVideoStatus(LocalVideoStats localVideoStats) {
        //本地上行速率
    }

    @Override
    public void remoteVideoStatus(RemoteVideoStats mRemoteVideoStats) {
        //下行速率
    }

    @Override
    public void LocalAudioStatus(LocalAudioStats localAudioStats) {

    }

    @Override
    public void remoteAudioStatus(RemoteAudioStats mRemoteAudioStats) {

    }

    @Override
    public void OnupdateUserBaseInfo(Long roomId, long uid, String sCustom) {

    }

    @Override
    public void OnConnectSuccess(String ip, int port) {

    }


}
