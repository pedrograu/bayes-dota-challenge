package gg.bayes.challenge.db.service;

import gg.bayes.challenge.business.model.HeroDamageLogic;
import gg.bayes.challenge.business.model.HeroItemsLogic;
import gg.bayes.challenge.business.model.HeroKillsLogic;
import gg.bayes.challenge.business.model.HeroSpellsLogic;

import java.util.List;

public interface MatchService {
    Long ingestMatch(String payload);

    List<HeroKillsLogic> getMatchDaoGivenMatchId(Long matchId);

    List<HeroItemsLogic> getHeroItemsDaoGivenMatchIdAndHero(Long matchId, String heroName);

    List<HeroSpellsLogic> getHeroSpellsDaoGivenMatchIdAndHero(Long matchId, String heroName);

    List<HeroDamageLogic> getHeroDamageDaoGivenMatchIdAndHero(Long matchId, String heroName);
}
