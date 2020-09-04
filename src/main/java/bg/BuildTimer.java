package bg;

import mindustry.gen.Call;

public class BuildTimer{
    private timerThread updateThread;

    //private boolean active = false;
    Long interval = 1000L;
    Long duration = 0L;
    //
    Long stopTime = 0L;

    public BuildTimer(Thread mt){
    }

    public boolean ticking(){
        if(this.updateThread!=null){
            return this.updateThread.isAlive() || this.updateThread.active;
        }else{
            return false;
        }
    }

    public void stopTimer(){
        this.updateThread.active = false;
        this.updateThread.kill();
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

    public int startTimer(Long time){
        if(ticking()) return 1;
        this.duration = time;
        this.stopTime = System.currentTimeMillis() + duration;
        loop();
        return 0;
    }

    private void loop(){
        this.updateThread = new timerThread();
        this.updateThread.duration = this.duration;
        this.updateThread.stopTime = this.stopTime;
        this.updateThread.setDaemon(true);
        this.updateThread.start();
    }

    class timerThread extends Thread{
        public boolean active = false;
        public Long duration;
        public Long stopTime;

        public void run(){
            this.active = true;
            Long t;
            Call.onInfoToast(String.format("[scarlet]Time remaining[] %02d:%02d", duration/60000L, (duration/1000L)-60L*(int)(duration/60000L)), 1.1f);
            try{
            while(this.active){
                t = (stopTime - System.currentTimeMillis());
                if(t%interval == 0){
                    Call.onInfoToast(String.format("[scarlet]Time remaining[] %02d:%02d", t/60000L, (t/1000L)-60L*(int)(t/60000L)), 1.1f);
                }
                if(t<=0L){
                    break;
                }
            }
            Call.onInfoToast("[scarlet]Time remaining[] 00:00", 5f);
            this.active = false;
            }catch (Exception e){

            }
        }
        public void kill(){
            this.active = false;
            this.duration = 0L;
        }
    }
}
