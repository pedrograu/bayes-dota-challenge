package gg.bayes.challenge.db.entities;

public class MatchKillEntity {

    private Long matchId;
    private String Hero;
    private String target;

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public String getHero() {
        return Hero;
    }

    public void setHero(String hero) {
        Hero = hero;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
