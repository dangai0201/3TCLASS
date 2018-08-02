package com.tttlive.education.ui.room;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tttlive.education.adapter.ChatAdapter;
import com.tttlive.education.adapter.QQFaceAdapter;
import com.tttlive.education.constant.Constant;
import com.tttlive.basic.education.R;
import com.tttlive.education.ui.SharePopu;
import com.tttlive.education.ui.room.bean.SendTextMessageBean;
import com.tttlive.education.ui.room.liveLand.landadapter.ChatAdapterLand;
import com.tttlive.education.util.PicturePreviewPopWindowUtil;

import java.util.ArrayList;

/**
 * Created by mr.li on 2018/4/16.
 */

public class ChatPopupWindow {

    private Context mContext;
    private String mRoomId;
    private String mUserId;
    private String mUserName;
    private String mUserImage;
    private View cView;
    private PopupWindow popupWindow;
    private PicturePreviewPopWindowUtil picturePreviewUtil = new PicturePreviewPopWindowUtil();
    private ImageButton chat_close;
    private RecyclerView chat_recycler;
    private int popupHeight;
    private int popupWidth;
    private boolean mBanned = false;
    private ChatAdapter adapter;
    private QQFaceAdapter faceAdapter;
    private ArrayList<SendTextMessageBean> smglist;

    private GridView recyview_em;
    private ImageView bt_em;
    private Button bt_send;
    private EditText room_edt_chat;
    private ImageView bt_picture;
    private ChatOnItemClickListener chatOnItemClickListener = new ChatOnItemClickListener();
    private FaceOnItemClickListener faceOnItemClickListener = new FaceOnItemClickListener();
    private EmOnclickListener emOnclickListener = new EmOnclickListener();
    private SendOnClickListener sendOnClickListener = new SendOnClickListener();
    private PictureOnClickListener pictureOnClickListener = new PictureOnClickListener();
    private ChatEditorActionListener chatEditorActionListener = new ChatEditorActionListener();
    private ChatOnClickListener chatOnClickListener = new ChatOnClickListener();
    private RelativeLayout chat_relative_layout;


    public ChatPopupWindow(Context context, String roomId, String userId, String userName, String userImage) {
        this.mContext = context;
        this.mRoomId = roomId;
        this.mUserId = userId;
        this.mUserName = userName;
        this.mUserImage = userImage;
    }

    public void showChatPop(View view, Context context, ArrayList<SendTextMessageBean> msglist , boolean isBanned) {
        this.mContext = context;
        this.cView = view;
        this.mBanned = isBanned;
        //自定义PopupWindow的布局
        View contentView = LayoutInflater.from(context).inflate(R.layout.chat_popupwindow, null);
        //获取自身的长宽高
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupHeight = contentView.getMeasuredHeight();
        popupWidth = contentView.getMeasuredWidth();
        contentView.setFocusable(true);

        //初始化PopupWindow,并为其设置布局文件
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        chat_close = contentView.findViewById(R.id.chat_close);
        chat_recycler = contentView.findViewById(R.id.chat_recycler);
        recyview_em = contentView.findViewById(R.id.recyview_em);
        bt_em = contentView.findViewById(R.id.bt_em);
        bt_send = contentView.findViewById(R.id.bt_send);
        bt_picture = contentView.findViewById(R.id.bt_picture);
        room_edt_chat = contentView.findViewById(R.id.room_edt_chat);
        chat_relative_layout = contentView.findViewById(R.id.chat_relative_layout);
        chat_recycler.setLayoutManager(new LinearLayoutManager(context));
        recyview_em.setSelector(new ColorDrawable(Color.TRANSPARENT));
        recyview_em.setVisibility(View.GONE);
        faceAdapter = new QQFaceAdapter(mContext);
        recyview_em.setAdapter(faceAdapter);
        recyview_em.setOnItemClickListener(faceOnItemClickListener);
        bt_em.setOnClickListener(emOnclickListener);
        bt_send.setOnClickListener(sendOnClickListener);
        bt_picture.setOnClickListener(pictureOnClickListener);
        room_edt_chat.setOnEditorActionListener(chatEditorActionListener);
        room_edt_chat.setOnClickListener(chatOnClickListener);
//

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) chat_relative_layout.getLayoutParams();
        linearParams.setMargins(0, height / 3, 0, 0);
        chat_relative_layout.setLayoutParams(linearParams);

        showChatList(msglist);

        int[] location = new int[2];
        view.getLocationOnScreen(location);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.AnimBottomIn);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setInputMethodMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //在控件上方显示
        //        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, (location[0]) - popupWidth / 2, location[1] - popupHeight);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        chat_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                popupWindow = null;
            }
        });

    }

    public void closePop() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    /**
     * 是否显示
     *
     * @return
     */
    public boolean isShow() {
        if (popupWindow != null) {
            return popupWindow.isShowing();
        }
        return false;
    }

    /**
     * 初始化聊天信息框
     * @param msglist
     */
    public void showChatList(ArrayList<SendTextMessageBean> msglist) {
        this.smglist = msglist;
        adapter = new ChatAdapter(mContext, smglist);
        chat_recycler.setAdapter(adapter);
        chat_recycler.scrollToPosition(adapter.getItemCount() - 1);
        adapter.setOnItemClickListener(chatOnItemClickListener);

    }

    /**
     * 聊天消息点击监听
     */
    class ChatOnItemClickListener implements ChatAdapter.PersonneViewItemClickListener {


        @Override
        public void onPersonneItemClick(View view, int position) {

            if (smglist != null && smglist.size() > 0) {
                if (smglist.get(position).getData().getType() == Constant.SEND_MESSAGE_TYPE_IMAGE) {
                    picturePreviewUtil.showPicturePreviewUtil(mContext, cView, smglist.get(position).getData().getMessage());
                    Log.e("==================", position + " Url === " + smglist.get(position).getData().getMessage());
                }
            }

        }
    }


    /**
     * 表情选择
     */
    class FaceOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SpannableString spannableString = new SpannableString(view
                    .getTag().toString());
            Drawable drawable = mContext.getResources().getDrawable(
                    (int) faceAdapter.getItemId(position));
            drawable.setBounds(0, 0, 70, 70);

            ImageSpan imageSpan = new ImageSpan(drawable,
                    ImageSpan.ALIGN_BOTTOM);
            spannableString.setSpan(imageSpan, 0, view.getTag().toString()
                    .length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            room_edt_chat.getText().insert(room_edt_chat.getSelectionStart(), spannableString);
        }
    }

    /**
     * 表情按钮点击事件
     */
    class EmOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (mBanned){
                Toast.makeText(mContext , mContext.getResources().getString(R.string.live_banned) , Toast.LENGTH_SHORT).show();
                return;
            }

            if (recyview_em.getVisibility() == View.VISIBLE) {
                bt_em.setBackground(mContext.getResources().getDrawable(R.drawable.chat_icon_emoji_normal));
                ((BaseLiveActivity) mContext).showKeyboard();
                recyview_em.setVisibility(View.GONE);
            } else {
                bt_em.setBackground(mContext.getResources().getDrawable(R.drawable.chat_icon_keyboard_normal));
                ((BaseLiveActivity) mContext).hideKeyboard(v);
                recyview_em.setVisibility(View.VISIBLE);

            }
        }
    }

    /**
     * 发送按钮
     */
    class SendOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (mBanned){
                Toast.makeText(mContext , mContext.getResources().getString(R.string.live_banned) , Toast.LENGTH_SHORT).show();
                return;
            }
            String diaEdaitTex = room_edt_chat.getText().toString().trim();
            if (TextUtils.isEmpty(diaEdaitTex)) {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.input_out_empty), Toast.LENGTH_SHORT).show();
                return;
            }

            if (recyview_em.getVisibility() == View.GONE) {
                ((BaseLiveActivity) mContext).hideKeyboard(v);
            } else if (recyview_em.getVisibility() == View.VISIBLE) {
                recyview_em.setVisibility(View.GONE);
            }
            sendTextMessage(diaEdaitTex, Constant.SEND_MESSAGE_TYPE_TEXT);
            room_edt_chat.setText("");
            bt_em.setBackground(mContext.getResources().getDrawable(R.drawable.chat_icon_emoji_normal));

        }
    }

    /**
     * 软键盘发送按钮监听
     */
    class ChatEditorActionListener implements TextView.OnEditorActionListener {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (mBanned){
                return false;
            }
            if (actionId == EditorInfo.IME_ACTION_SEND) {

                String diaMessage = room_edt_chat.getText().toString().trim();

                if (TextUtils.isEmpty(diaMessage)) {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.input_out_empty), Toast.LENGTH_SHORT).show();
                    return false;
                }

                sendTextMessage(diaMessage, Constant.SEND_MESSAGE_TYPE_TEXT);
                room_edt_chat.setText("");
                return true;
            }
            return false;
        }
    }

    /**
     * 输入框点击事件
     */
    class ChatOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (mBanned){
                Toast.makeText(mContext , mContext.getResources().getString(R.string.live_banned) , Toast.LENGTH_SHORT).show();
                room_edt_chat.setFocusable(false);
                return;
            }else {
                room_edt_chat.setFocusable(true);
            }
            if (recyview_em.getVisibility() == View.VISIBLE) {
                bt_em.setBackground(mContext.getResources().getDrawable(R.drawable.chat_icon_emoji_normal));
                recyview_em.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 发送图片
     */
    class PictureOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (mBanned){
                Toast.makeText(mContext , mContext.getResources().getString(R.string.live_banned) , Toast.LENGTH_SHORT).show();
                return;
            }

            //第二个参数是需要申请的权限
            if (ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //权限还没有授予，需要在这里写申请权限的代码
                ActivityCompat.requestPermissions((Activity) mContext,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        7);
            } else {
                //权限已经被授予，在这里直接写要执行的相应方法即可
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                ((Activity) mContext).startActivityForResult(intent, Constant.PHOTO_REQUEST_GALLERY);
                if (recyview_em.getVisibility() == View.VISIBLE) {
                    bt_em.setBackground(mContext.getResources().getDrawable(R.drawable.chat_icon_emoji_normal));
                    recyview_em.setVisibility(View.GONE);
                }
            }

        }
    }


    /**
     * 发送聊天消息
     *
     * @param message
     */
    public void sendTextMessage(String message, int messageType) {
        Long ct = System.currentTimeMillis();
        String seqid = "binding_" + String.valueOf(ct);
        Gson gson = new Gson();
        SendTextMessageBean stMessAge = new SendTextMessageBean();
        SendTextMessageBean.DataBean stDb = new SendTextMessageBean.DataBean();
        stMessAge.setMessageType(Constant.BARRAGE_REQ);
        stDb.setMessage(message);
        stDb.setNickName(mUserName);
        stDb.setAvatar(mUserImage);
        stDb.setUserId(mUserId);
        stDb.setRoomId(mRoomId);
        stDb.setType(messageType);
        stMessAge.setData(stDb);
        String ss = gson.toJson(stMessAge);
        smglist.add(stMessAge);
        showChatList(smglist);
        //   mRoomLiveHelp.sendMessage(0, 1, seqid, ss);

        if (Constant.wsService != null) {
            Constant.wsService.sendRequest(BaseLiveActivity.wsSendMsg(Integer.parseInt(mRoomId), Integer.parseInt(mUserId), 1, ss, seqid, 0, true));
        }

    }

    /**
     * 是否禁言
     * @param banned
     */
    public void banned(boolean banned){
        this.mBanned = banned;
    }


}
