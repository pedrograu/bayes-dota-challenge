package gg.bayes.challenge.db.entities;

public class MatchDamageEntity {

    private Long matchId;
    private String hero;
    private String target;
    private Integer damage;

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public String getHero() {
        return hero;
    }

    public void setHero(String hero) {
        this.hero = hero;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Integer getDamage() {
        return damage;
    }

    public void setDamage(Integer damage) {
        this.damage = damage;
    }
}
