package gg.bayes.challenge.db.entities;

public class MatchItemEntity {

    private Long matchId;
    private String hero;
    private String item;
    private Long timePurchase;

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

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Long getTimePurchase() {
        return timePurchase;
    }

    public void setTimePurchase(Long timePurchase) {
        this.timePurchase = timePurchase;
    }
}
