package bg;

import mindustry.Vars;
import mindustry.gen.Call;

public class BuildTimer{
    private Thread updateThread;

    private boolean active = false;
    Long interval = 1000L;
    Long duration = 0L;
    //
    Long stopTime = 0L;

    public BuildTimer(Thread mt){
    }

    public boolean ticking(){
        if(this.updateThread!=null){
            return this.updateThread.isAlive() || active;
        }else{
            return active;
        }

    }

    public void stopTimer(){
        this.active = false;
    }

    public void setTime(Long time){
        this.duration = time;
    }

    public int startTimer(){
        if(ticking()) return 1;
        this.stopTime = System.currentTimeMillis() + duration;
        loop();
        return 0;
    }

    private void loop(){
        active = true;
        this.updateThread = new Thread(){
            public void run(){
                Long t;
                Call.onInfoToast(String.format("[scarlet]Time remaining[] %02d:%02d", duration/60000L, duration/1000L), 1.1f);
                while(true && active){
                    t = (stopTime - System.currentTimeMillis());
                    if(t%interval == 0){
                        System.out.println("update");
                        System.out.println(t/1000L);
                        Call.onInfoToast(String.format("[scarlet]Time remaining[] %02d:%02d", t/60000L, t/1000L), 1.1f);
                    }
                    if(t<=0L){
                        break;
                    }
                }
                Call.onInfoToast("[scarlet]Time remaining[] 00:00", 5f);
            }
        };
        this.updateThread.setDaemon(true);
        this.updateThread.start();
    }

}
