package com.aihelp.boatraceapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bn.clp.Constant;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sp;
    SoundPool shengyinchi;  // 声音池
    HashMap<Integer,Integer> soundIdMap;//声音池中声音ID与自定义声音ID的Map
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取SharePreferences
        sp = this.getSharedPreferences("actm", Context.MODE_PRIVATE);
        // 获得值，默认值为true
        Constant.BgSoundFlag = sp.getBoolean("bgSoundFlag", true);
        Constant.SoundEffectFlag = sp.getBoolean("soundEffectFlag", true);
        // 初始化声音资源
        chushihuaSound();
        // 设置为全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 设置为横屏模式
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // 游戏过程中只允许调整多媒体音量，而不允许调整通话音量
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
