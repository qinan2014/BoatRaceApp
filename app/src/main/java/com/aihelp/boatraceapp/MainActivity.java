package com.aihelp.boatraceapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.bn.clp.Constant;
import com.bn.st.d2.DBUtil;
import java.util.HashMap;

import static com.bn.st.xc.Constant.SCREEN_HEIGHT;
import static com.bn.st.xc.Constant.SCREEN_WIDTH;
import static com.bn.st.xc.Constant.ratio_height;
import static com.bn.st.xc.Constant.ratio_width;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sp;
    SoundPool shengyinchi;  // 声音池
    HashMap<Integer,Integer> soundIdMap;//声音池中声音ID与自定义声音ID的Map
    int flag;
    //SensorManager对象引用
    SensorManager mySensorManager;
    public static Object boatInitLock=new Object();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取SharePreferences
        sp = this.getSharedPreferences("actm", Context.MODE_PRIVATE);
        // 获得值，默认值为true
        com.bn.clp.Constant.BgSoundFlag = sp.getBoolean("bgSoundFlag", true);
        com.bn.clp.Constant.SoundEffectFlag = sp.getBoolean("soundEffectFlag", true);
        // 初始化声音资源
        chushihuaSound();
        // 设置为全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 设置为横屏模式
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // 游戏过程中只允许调整多媒体音量，而不允许调整通话音量
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        // 创建数据库表
        DBUtil.createTable();

        flag = Settings.System.getInt(this.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
        Settings.System.putInt(this.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 1);

        SCREEN_WIDTH = getWindowManager().getDefaultDisplay().getWidth();
        SCREEN_HEIGHT = getWindowManager().getDefaultDisplay().getHeight();

        float screnHeightTemp = SCREEN_HEIGHT; // 记录系统返回的屏幕分辨率
        float screenWidthTemp = SCREEN_WIDTH;

        if (screnHeightTemp > screenWidthTemp)
        {
            SCREEN_WIDTH = screnHeightTemp;
            SCREEN_HEIGHT = screenWidthTemp;
        }
        Constant.screenRatio = SCREEN_WIDTH / SCREEN_HEIGHT; // 获得屏幕的宽高比
        if(Math.abs(com.bn.clp.Constant.screenRatio-com.bn.clp.Constant.screenRatio854x480)<0.001f)
        {
            com.bn.clp.Constant.screenId=1;
        }
        else if(Math.abs(com.bn.clp.Constant.screenRatio-com.bn.clp.Constant.screenRatio480x320)<0.01f)
        {
            com.bn.clp.Constant.screenId=3;
        }
        else if(Math.abs(com.bn.clp.Constant.screenRatio-com.bn.clp.Constant.screenRatio960x540)<0.001f)
        {
            com.bn.clp.Constant.screenId=2;
        }
        else
        {
            com.bn.clp.Constant.screenId=0;
        }
        ratio_height = SCREEN_HEIGHT / 480;
        ratio_width = SCREEN_WIDTH / 854;
        mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        new Thread()
        {
            public void run()
            {
                synchronized (boatInitLock)
                {
                    // 选船
//                    Cylidertex
                }
            }
        }.start();
    }

    // 创建声音的方法
    public void chushihuaSound()
    {
        shengyinchi = new SoundPool(7, AudioManager.STREAM_MUSIC, 100);
        soundIdMap = new HashMap<Integer, Integer>();
        soundIdMap.put(1, shengyinchi.load(this, R.raw.pengzhuang, 1)); // 碰撞声音
        soundIdMap.put(2, shengyinchi.load(this, R.raw.boatgo, 1));  // 船加速的声音
        soundIdMap.put(3, shengyinchi.load(this, R.raw.eatthings1, 1)); // 吃东西的声音
        soundIdMap.put(4, shengyinchi.load(this, R.raw.zhuangfei, 1));  // 撞飞东西的声音
        soundIdMap.put(5, shengyinchi.load(this, R.raw.daojishi, 1));  // 321倒计时的声音
        soundIdMap.put(6, shengyinchi.load(this, R.raw.start, 1));  // 开始可以走的声音
    }
    // 播放声音
    public void shengyinBoFang(int sound, int loop)
    {
        AudioManager mgr = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent / streamVolumeMax;
        shengyinchi.play(soundIdMap.get(sound), volume, volume, 1, loop, 1f);
    }
}
