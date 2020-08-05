package gg.bayes.challenge.rest.controller;

import gg.bayes.challenge.business.model.HeroItemsLogic;
import gg.bayes.challenge.business.model.HeroKillsLogic;
import gg.bayes.challenge.business.model.HeroSpellsLogic;
import gg.bayes.challenge.db.service.MatchService;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/match")
public class MatchController {

    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping(consumes = "text/plain")
    public ResponseEntity<Long> ingestMatch(@RequestBody @NotNull @NotBlank String payload) {
        try {
            final Long matchId = matchService.ingestMatch(payload);
            return ResponseEntity.ok(matchId);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("{matchId}")
    public ResponseEntity<List<HeroKills>> getMatch(@PathVariable("matchId") Long matchId) {
        try {
            List<HeroKillsLogic> heroKillsLogicList = matchService.getMatchDaoGivenMatchId(matchId);
            if (heroKillsLogicList.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            List<HeroKills> heroKills = heroKillsLogicList.stream().map(this::transformHeroKills).collect(Collectors.toList());
            return ResponseEntity.ok(heroKills);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("{matchId}/{heroName}/items")
    public ResponseEntity<List<HeroItems>> getItems(@PathVariable("matchId") Long matchId,
                                                    @PathVariable("heroName") String heroName) {
        try {
            List<HeroItemsLogic> heroItemsLogics = matchService.getHeroItemsDaoGivenMatchIdAndHero(matchId, heroName);
            if (heroItemsLogics.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            List<HeroItems> heroItems = heroItemsLogics.stream().map(this::transformHeroItems).collect(Collectors.toList());

            return ResponseEntity.ok(heroItems);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("{matchId}/{heroName}/spells")
    public ResponseEntity<List<HeroSpells>> getSpells(@PathVariable("matchId") Long matchId,
                                                      @PathVariable("heroName") String heroName) {
        try {
            List<HeroSpellsLogic> heroSpellsLogics = matchService.getHeroSpellsDaoGivenMatchIdAndHero(matchId, heroName);
            if (heroSpellsLogics.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            List<HeroSpells> heroSpells = heroSpellsLogics.stream().map(this::transformHeroSpells).collect(Collectors.toList());

            return ResponseEntity.ok(heroSpells);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("{matchId}/{heroName}/damage")
    public ResponseEntity<List<HeroDamage>> getDamage(@PathVariable("matchId") Long matchId,
                                                      @PathVariable("heroName") String heroName) {
        // TODO use match service to retrieve stats
        throw new NotImplementedException("should be implemented by the applicant");
    }

    private HeroKills transformHeroKills(HeroKillsLogic heroKillsLogic) {
        HeroKills heroKills = new HeroKills();
        heroKills.setHero(heroKillsLogic.getHero());
        heroKills.setKills(heroKillsLogic.getKills());
        return heroKills;
    }

    private HeroItems transformHeroItems(HeroItemsLogic heroItemsLogic) {
        HeroItems heroItems = new HeroItems();
        heroItems.setItem(heroItemsLogic.getItem());
        heroItems.setTimestamp(heroItemsLogic.getTimePurchase());
        return heroItems;
    }

    private HeroSpells transformHeroSpells(HeroSpellsLogic heroSpellsLogic) {
        HeroSpells heroSpells = new HeroSpells();
        heroSpells.setSpell(heroSpellsLogic.getSpell());
        heroSpells.setCasts(heroSpellsLogic.getCasts());
        return heroSpells;
    }
}
