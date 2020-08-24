package bg;


import arc.struct.Array;
import arc.struct.ObjectIntMap;
import mindustry.entities.type.Player;
import mindustry.gen.Call;


public class Scores {
    ObjectIntMap<Player> standings = new ObjectIntMap<>();

    public Scores(){
    }

    public Array<Player> getLeaderboard(){
        Array<Player> players = standings.keys().toArray();
        players.sort(p -> -getPlayerScore(p));
        return players;
    };

    public int getPlayerScore(Player p){
        return standings.get(p, 0);
    }

    public void setPlayers(Array<Player> players){
        standings.clear();
        players.forEach(p -> standings.put(p,0));
    }

    public void updateScore(Player p, int score){
        standings.put(p, standings.get(p, 0) + score);
    }

    public void showLeaderboardInfo(){
        Call.onInfoMessage(LeaderboardInfo());
    }

    public void showLeaderboardEnd(){
        StringBuilder sb = new StringBuilder("[sky]Final standings[]\n\n");
        Array<Player> _players = getLeaderboard();
        Player p;
        for(int i=0; i<standings.keys().toArray().size; i++){
            p = getLeaderboard().get(i);
            sb.append(i+1).append(" : ").append(p.name).append("[] : ").append(getPlayerScore(p)).append('\n');
        }
        Call.onInfoMessage(sb.toString());
    }

    public String LeaderboardInfo(){
        StringBuilder sb = new StringBuilder("[sky]Current standings[]\n\n");
        this.getLeaderboard().forEach(p -> sb.append(p.name).append("[] : ").append(getPlayerScore(p)).append('\n'));
        return sb.toString();
    }

    public void reset(){
        standings.clear();
    }
}
