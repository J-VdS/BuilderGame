package bg;

import mindustry.gen.Call;

public class BuildTimer2 {
    //private boolean active = false;
    Long interval = 1000L;
    Long duration = 0L;
    //when to stop timer
    Long stopTime = 0L;

    public BuildTimer2(){
    }

    public void update(){
        Long t = (stopTime - System.currentTimeMillis());
        if(t%interval == 0 && t > 0L){
            Call.onInfoToast(String.format("[scarlet]Time remaining[] %02d:%02d", t/60000L, (t/1000L)-60L*(int)(t/60000L)), 1.05f);
        }else if(t%interval == 0 && t <= 0L){
            Call.onInfoToast("[scarlet]Time remaining[] 00:00", 1.05f);
        }
    }

    public boolean ticking(){
        return stopTime > System.currentTimeMillis();
    }

    public void stop(){
        stopTime = 0L;
    }

    public void setTime(Long time){
        duration = time;
    }

    public void start(Long time){
        duration = time;
        stopTime = System.currentTimeMillis() + time;
    }
}
