package com.nekiplay.hypixelcry.DataInterpretation;

import com.google.common.base.Predicate;
import com.nekiplay.hypixelcry.EventIDs;
import com.nekiplay.hypixelcry.utils.ApecUtils;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Tuple;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataExtractor {

    private Minecraft mc = Minecraft.getMinecraft();
    //public PotionFetcher potionFetcher = new PotionFetcher(this);

    private final char HpSymbol = '❤';
    private final char MnSymbol = '✎';
    private final char[] healDurationSymbols = new char[] { '▆', '▅', '▄', '▃', '▂', '▁' };

    private final String endRaceSymbol = "THE END RACE";
    private final String woodRacingSymbol = "WOODS RACING";
    private final String dpsSymbol = "DPS";
    private final String secSymbol = "second";
    private final String secretSymbol = "Secrets";
    private final String chickenRaceSymbol = "CHICKEN RACING";
    private final String jumpSymbol = "JUMP";
    private final String crystalRaceSymbol = "CRYSTAL CORE RACE";
    private final String giantMushroomSymbol = "GIANT MUSHROOM RACE";
    private final String precursorRuinsSymbol = "PRECURSOR RUINS RACE";
    private final String armadilloName = "Armadillo";

    private boolean alreadyShowedTabError = false;
    private boolean alreadyShowedScrErr = false;

    private int lastHp = 1, lastBaseHp = 1;
    private int lastMn = 1, lastBaseMn = 1;
    private int lastDefence = 0;
    private int baseAp = 0;
    private int lastAp = 1 , lastBaseAp = 1;
    private int baseOp = 1;

    private String lastHour = "",lastDate = "",lastZone = "";

    private String lastSkillXp = "";
    private String lastPurse = "";

    private boolean usesPiggyBank = false;

    private boolean hasSentATradeRequest = false;
    private boolean hasRecievedATradeRequest = false;

    public boolean isInTheCatacombs = false;
    private boolean wasInTheCatacombs = false;

    public boolean IsDeadInTheCatacombs = false;

    private int tradeTicks = 0;

    String actionBarData  = ""; // Holds the data that is in the action bar
    String footerTabData = ""; // Holds the data that is in the

    private PlayerStats playerStats;
    private List<String> scoreBoardLines;
    private final ScoreBoardData scoreBoardData = new ScoreBoardData();
    private OtherData otherData;

    public boolean isInSkyblock = false; // This flag is true if the player is in skyblock

    // The priority is set on highest so the data is getting parsed before it's modified by sba
    /** Gets the action bar data and looks in the chat for trades */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChatMsg(ClientChatReceivedEvent event) {
        String reviveSymbol = "Revive";
        if (
                (event.message.getUnformattedText().contains(String.valueOf(HpSymbol))
                        || event.message.getUnformattedText().contains(String.valueOf(MnSymbol))
                        || event.message.getUnformattedText().contains(reviveSymbol)
                        || event.message.getUnformattedText().contains(chickenRaceSymbol)) // For some reason this race doesn't show the mana
                        || event.message.getUnformattedText().contains(armadilloName)
        ) {
            IsDeadInTheCatacombs = event.message.getUnformattedText().contains(reviveSymbol);
            actionBarData = event.message.getUnformattedText();
            /** used for sampling data when developing
             try {
             File f = new File("config/Apec/debug");
             f.createNewFile();
             FileWriter fw = new FileWriter(f,true);
             char[] c = actionBarData.toCharArray();
             fw.write('\n' + actionBarData + ' ');
             for (char c_ : c) {
             fw.write(Integer.toString((int)c_));
             fw.write(' ');
             }
             fw.write('\n');
             fw.close();
             } catch (IOException exception) {
             exception.printStackTrace();
             }*/
        } else if (!event.message.getUnformattedText().contains("<") && !isFromChat(event.message.getFormattedText())){

            String msg = ApecUtils.removeAllCodes(event.message.getUnformattedText());
            String sendTradeRequestMsg = "You have sent a trade request to";
            if (msg.contains(sendTradeRequestMsg)) hasSentATradeRequest = true;
            String expireSentTradeRequest = "Your /trade request to";
            if (msg.contains(expireSentTradeRequest)) hasSentATradeRequest = false;

            String recieveTradeRequestMsg = "has sent you a trade request";
            if (msg.contains(recieveTradeRequestMsg)) hasRecievedATradeRequest = true;
            String expireRecieveTradeRequest = "The /trade request from";
            if (msg.contains(expireRecieveTradeRequest)) hasRecievedATradeRequest = false;

            String tradeCompleted = "Trade completed";
            String tradeCancelled = "You cancelled the trade!";
            if (msg.contains(tradeCancelled) || msg.contains(tradeCompleted)) {
                if (hasSentATradeRequest) hasSentATradeRequest = false;
                if (hasRecievedATradeRequest) hasRecievedATradeRequest = false;
            }
        }
    }

    // The priority is on lowest since the gui swapping that happens if sidebar mod swiched it happens on priority low
    boolean UpdateThisTick = false;
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onTick(TickEvent.ClientTickEvent event) {
        UpdateThisTick = !UpdateThisTick;
        if (UpdateThisTick) {
            try {
                String s = getScoreBoardTitle();
                if (!s.isEmpty()) {
                    this.isInSkyblock = ApecUtils.removeAllCodes(s).toLowerCase().contains("skyblock");
                    if (wasInTheCatacombs ^ isInTheCatacombs) {
                        wasInTheCatacombs = isInTheCatacombs;
                        //potionFetcher.ClearAll();
                    }
                }
            } catch (Exception ignored) {
            }
            try {
                IChatComponent icc = (IChatComponent) FieldUtils.readField(mc.ingameGUI.getTabList(), ApecUtils.unObfedFieldNames.get("footer"), true);
                if (icc == null) {
                    this.footerTabData = "";
                } else {
                    this.footerTabData = icc.getFormattedText();
                    alreadyShowedTabError = false;
                }
            } catch (Exception e) {
                if (!alreadyShowedTabError) {
                    alreadyShowedTabError = true;
                }
            }

            try {
                this.scoreBoardLines = getSidebarLines();
                this.scoreBoardLines = addDataFixesScoreboard(this.scoreBoardLines);
            } catch (Exception ignored) {

            }

            ProcessScoreBoardData();

            this.playerStats = this.ProcessPlayerStats();

            this.otherData = this.ProcessOtherData(this.scoreBoardData);

            if (hasRecievedATradeRequest || hasSentATradeRequest) {
                tradeTicks++;
                if (tradeTicks == 600) {
                    hasSentATradeRequest = false;
                    hasRecievedATradeRequest = false;
                }
            } else {
                tradeTicks = 0;
            }

        }
    }

    /**
     * This only includes adding missing brackets in dungeons
     */

    public List<String> addDataFixesScoreboard(List<String> s) {
        for (String _s: s) {
            if (_s.contains("[") && !_s.contains("]")) _s.concat("§]");
        }
        return s;
    }

    /**
     * @param s = Input string
     * @return Return true if the specific text should have an empty line before in the left side display
     */

    public boolean ShouldHaveSpaceBefore(String s) {
        return ApecUtils.containedByCharSequence(s,"Objective") || //Objectives
                ApecUtils.containedByCharSequence(s,"Contest") || // Jacob's contest
                ApecUtils.containedByCharSequence(s,"Year") || // New year and the vote thing
                ApecUtils.containedByCharSequence(s,"Zoo") || // Traveling Zoo
                ApecUtils.containedByCharSequence(s,"Festival") || // Spooky Festival
                ApecUtils.containedByCharSequence(s,"Season") || // Jerry season
                ApecUtils.containedByCharSequence(s,"Election") || // Idk at this point im just putting some things from the wiki
                ApecUtils.containedByCharSequence(s,"Slayer") || // Slayer quest
                ApecUtils.containedByCharSequence(s,"Keys") || // Keys in dungeons
                ApecUtils.containedByCharSequence(s,"Time Elapsed") || // time elapsed dungeons
                ApecUtils.containedByCharSequence(s,"Starting in:") ||
                ApecUtils.containedByCharSequence(s,"Wave") ||
                ApecUtils.containedByCharSequence(s,"Festival");
    }

    /**
     * @param s = Input string
     * @return Return true if the specific text should have an empty line after in the left side display
     */

    public boolean ShouldHaveSpaceAfter(String s) {
        return ApecUtils.containedByCharSequence(s,"Dungeon Cleared"); // Dungeon cleared in dungeons
    }

    /**
     * @param s = Input string
     * @return Return true if the input string represents the real life date
     */

    public boolean RepresentsIRLDate(String s) {
        return ApecUtils.containedByCharSequence(s,"//");
    }

    /**
     * @param s = Input string
     * @return Return true if the input string represents the date
     */

    public boolean RepresentsDate(String s) {
        return (ApecUtils.containedByCharSequence(s,"Autumn") ||
                ApecUtils.containedByCharSequence(s,"Winter") ||
                ApecUtils.containedByCharSequence(s,"Spring") ||
                ApecUtils.containedByCharSequence(s,"Summer")) &&
                (ApecUtils.containedByCharSequence(s,"st") ||
                        ApecUtils.containedByCharSequence(s,"nd") ||
                        ApecUtils.containedByCharSequence(s,"rd") ||
                        ApecUtils.containedByCharSequence(s,"th"));
    }

    /**
     * @param s = Input string
     * @return Return true if the input string represents the time
     */

    public boolean RepresentsTime(String s) {
        return (ApecUtils.containedByCharSequence(s,"am") ||
                ApecUtils.containedByCharSequence(s,"pm")) &&
                ApecUtils.containedByCharSequence(s,":") &&
                (ApecUtils.containedByCharSequence(s,"☽") || // The moon
                        ApecUtils.containedByCharSequence(s,"☀")); // The sun
    }

    /**
     * @param s = Input string
     * @return Return true if the input string represents the zone
     */

    public boolean RepresentsZone(String s) {
        return ApecUtils.containedByCharSequence(s,"⏣");
    }

    /**
     * @param s = Input string
     * @return Return true if the input string represents the purse
     */

    public boolean RepresentsPurse(String s) {
        return ApecUtils.containedByCharSequence(s,"Purse: ");
    }

    /**
     * @param s = Input string
     * @return Return true if the input string represents the purse with a piggy equipped
     */

    public boolean RepresentsPiggy(String s){
        return ApecUtils.containedByCharSequence(s,"Piggy: ");
    }

    /**
     * @param s = Input string
     * @return Return true if the input string represents the bits counter
     */
    public boolean RepresentsBits(String s) {
        return ApecUtils.containedByCharSequence(s,"Bits: ");
    }

    /**
     * ScoreBoardData - Contains the things that used to be displayed in the scoreboard
     * -server
     * -in game date and time
     * -purse
     * NOTE! Anything else that is not constant on the scoreboard and appears or disappears due to events or context are in ScoreBoardData.ExtraInfo saved as a list
     */

    private void ProcessScoreBoardData() {
        try {
            scoreBoardData.ExtraInfo.clear();

            int size = scoreBoardLines.size() - 1;

            isInTheCatacombs = false;

            for (int i = 0;i < scoreBoardLines.size();i++){
                String clearedName = "dungeon cleared";
                if (ApecUtils.containedByCharSequence(scoreBoardLines.get(size-i).toLowerCase(), clearedName)) {
                    isInTheCatacombs = true;
                }
                String combatZoneName = "the catacombs";
                if (ApecUtils.containedByCharSequence(scoreBoardLines.get(size-i).toLowerCase(), combatZoneName) && !scoreBoardLines.get(size-i).toLowerCase().contains("to")) {
                    isInTheCatacombs = true;
                }
            }

            ScoreboardParser(scoreBoardData, scoreBoardLines);

            scoreBoardData.ExtraInfo.add(" ");

            alreadyShowedScrErr = false;
            lastDate = scoreBoardData.Date;
            lastZone = scoreBoardData.Zone;
            lastHour = scoreBoardData.Hour;
        } catch (Exception err) {
            if (!alreadyShowedScrErr) {
                alreadyShowedScrErr = true;
            }
        }
    }

    /** Thanks for this very cool
     * From https://gist.github.com/aaron1998ish/33c4e1836bd5cf79501d163a1b5c8304
     *
     * Fetching lines are based on how they're visually seen on your sidebar
     * and not based on the actual value score.
     *
     * Written around Minecraft 1.8 Scoreboards, modify to work with your
     * current version of Minecraft.
     *
     * <3 aaron1998ish
     *
     * @return a list of lines for a given scoreboard or empty
     *         if the worlds not loaded, scoreboard isnt present
     *         or if the sidebar objective isnt created.
     */
    public List<String> getSidebarLines() {

        List<String> lines = new ArrayList<String>();
        Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();
        if (scoreboard == null) {
            return lines;
        }

        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);

        if (objective == null) {
            return lines;
        }

        Collection<Score> scores = scoreboard.getSortedScores(objective);
        List<Score> list = Lists.newArrayList(Iterables.filter(scores,new Predicate<Score>()  {
            public boolean apply(Score s) {
                return s.getPlayerName() != null;
            }
        }));

        if (list.size() > 15) {
            scores = Lists.newArrayList(Iterables.skip(list, scores.size() - 15));
        } else {
            scores = list;
        }

        for (Score score : scores) {
            ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
            lines.add(ScorePlayerTeam.formatPlayerName(team, score.getPlayerName()));
        }

        return lines;
    }

    /**
     * @return The title string of the scoreboard
     */

    public String getScoreBoardTitle() {
        try {
            Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();
            ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
            return objective.getDisplayName();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @param sd = Current ScoreBoardData class
     * @param l = All the lines of the scoreboard
     */

    private void ScoreboardParser(ScoreBoardData sd,List<String> l) {

        boolean BitsHaveBeenSet = false;
        List<String> rl = Lists.reverse(l);
        for (String line : rl) {
            if (RepresentsIRLDate(line)) {
                sd.IRL_Date = ApecUtils.removeFirstSpaces(line);
                if (line.contains("§8")) {
                    sd.ExtraInfo.add("Currently in: " + ApecUtils.segmentString(scoreBoardLines.get(scoreBoardLines.size() - 1), "§8", '§', '~', 1, 1, ApecUtils.SegmentationOptions.TOTALLY_INCLUSIVE));
                }
            }
            else if (RepresentsDate(line)) sd.Date = ApecUtils.removeFirstSpaces(line);
            else if (RepresentsTime(line)) sd.Hour = ApecUtils.removeFirstSpaces(line);
            else if (RepresentsZone(line)) sd.Zone = ApecUtils.removeFirstSpaces(line);
            else if (RepresentsPurse(line)) {
                sd.Purse = ApecUtils.removeFirstSpaces(line);
                usesPiggyBank = false;
            }
            else if (RepresentsPiggy(line)) {
                sd.Purse = ApecUtils.removeFirstSpaces(line);
                usesPiggyBank = true;
            }
            else if (RepresentsBits(line)) {
                sd.Bits = ApecUtils.removeFirstSpaces(line);
                BitsHaveBeenSet = true;
            }
            else if (!line.contains("www")) {
                if (!line.replaceAll("[^a-zA-Z0-9]", "").isEmpty()) {
                    if (ShouldHaveSpaceBefore(line)) sd.ExtraInfo.add(" ");
                    sd.ExtraInfo.add(ApecUtils.removeFirstSpaces(line));
                    if (ShouldHaveSpaceAfter(line)) sd.ExtraInfo.add(" ");
                }
            }
        }
        if (!BitsHaveBeenSet && !isInTheCatacombs) {
            sd.Bits = "Bits: §b0";
        }

    }

    public ScoreBoardData getScoreBoardData () {
        return this.scoreBoardData;
    }

    /**
     * Player Stats
     * -health
     * -mana
     * -defence
     * -absorption
     * -skill xp
     * -air
     */

    private PlayerStats ProcessPlayerStats () {
        PlayerStats playerStats = new PlayerStats();

        try {
            //HP
            {
                String segmentString = ApecUtils.segmentString(actionBarData, String.valueOf(HpSymbol), '§', HpSymbol, 1, 1);
                if (segmentString != null) {
                    Tuple<Integer, Integer> t = formatStringFractI(ApecUtils.removeAllCodes(segmentString));
                    playerStats.Hp = t.getFirst();
                    playerStats.BaseHp = t.getSecond();

                    if (playerStats.Hp > playerStats.BaseHp) {
                        playerStats.Ap = playerStats.Hp - playerStats.BaseHp;
                        playerStats.Hp = playerStats.BaseHp;
                    } else {
                        playerStats.Ap = 0;
                        playerStats.BaseAp = 0;
                    }
                    if (playerStats.Ap > baseAp) {
                        baseAp = playerStats.Ap;
                    }
                    playerStats.BaseAp = baseAp;

                    lastAp = playerStats.Ap;
                    lastBaseAp = playerStats.BaseAp;

                    lastHp = playerStats.Hp;
                    lastBaseHp = playerStats.BaseHp;
                } else {
                    playerStats.Hp = lastHp;
                    playerStats.BaseHp = lastBaseHp;
                    playerStats.Ap = lastAp;
                    playerStats.BaseAp = lastBaseAp;
                }
            }
        } catch (Exception err) {
            playerStats.Hp = lastHp;
            playerStats.BaseHp = lastBaseHp;
            playerStats.Ap = lastAp;
            playerStats.BaseAp = lastBaseAp;
        }

        try {
            // MP
            {
                String segmentedString = ApecUtils.segmentString(actionBarData, String.valueOf(MnSymbol), '§', MnSymbol, 1, 1);
                if (segmentedString != null) {
                    Tuple<Integer, Integer> t = formatStringFractI(ApecUtils.removeAllCodes(segmentedString));
                    playerStats.Mp = t.getFirst();
                    playerStats.BaseMp = t.getSecond();
                    lastMn = playerStats.Mp;
                    lastBaseMn = playerStats.BaseMp;
                } else {
                    playerStats.Mp = lastMn;
                    playerStats.BaseMp = lastBaseMn;
                }
            }
        } catch (Exception err) {
            playerStats.Mp = lastMn;
            playerStats.BaseMp = lastBaseMn;
        }

        try {
            // Overflow mana
            {
                char overflowSymbol = 'ʬ';
                String segmentedString = ApecUtils.segmentString(actionBarData,String.valueOf(overflowSymbol),'§', overflowSymbol,1,1);
                if (segmentedString != null) {
                    int value = Integer.parseInt(ApecUtils.removeAllCodes(segmentedString.replace(",","")));
                    playerStats.Op = value;
                    if (baseOp < value) {
                        baseOp = value;
                    }
                    playerStats.BaseOp = baseOp;
                } else {
                    playerStats.Op = 0;
                    playerStats.BaseOp = 0;
                    baseOp = 0;
                }
            }
        } catch (Exception ignored) {

        }

        try {
            // Heal Duration
            {
                String segmentedSrtring = null;
                char ticker = '\0';
                for (char _c : healDurationSymbols) {
                    ticker = _c;
                    segmentedSrtring = ApecUtils.segmentString(actionBarData,String.valueOf(_c),'+',_c,1,1);
                    if (segmentedSrtring != null) break;
                }
                if (segmentedSrtring != null) {
                    playerStats.HealDuration = Integer.parseInt(ApecUtils.removeAllCodes(segmentedSrtring).replace("+","")); //remove all codes for safety
                    playerStats.HealDurationTicker = ticker;
                } else {
                    playerStats.HealDuration = 0;
                }
            }
        } catch (Exception ignored) {

        }

        try {
            // Skill
            String segmentString = ApecUtils.segmentString(actionBarData, ")", '+', ' ', 1, 1, ApecUtils.SegmentationOptions.ALL_INSTANCES_LEFT);

            String inBetweenBrackets = null;
            float percentage = -1f;

            if (segmentString != null) {
                inBetweenBrackets = ApecUtils.segmentString(segmentString, "(", '(', ')', 1, 1, ApecUtils.SegmentationOptions.TOTALLY_EXCLUSIVE);
            }

            if (inBetweenBrackets != null) {
                percentage = praseSkillPercentage(inBetweenBrackets);
            }

            if (percentage != -1f) {
                lastSkillXp = segmentString;
                String wholeString = ApecUtils.removeAllCodes(segmentString);

                playerStats.SkillIsShown = true;
                playerStats.SkillInfo = wholeString;
                playerStats.SkillExpPercentage = percentage;

            } else {
                if (!lastSkillXp.isEmpty()) {
                    String wholeString = ApecUtils.removeAllCodes(lastSkillXp);
                    percentage = praseSkillPercentage(ApecUtils.segmentString(lastSkillXp, "(", '(', ')', 1, 1, ApecUtils.SegmentationOptions.TOTALLY_EXCLUSIVE));

                    playerStats.SkillIsShown = true;
                    playerStats.SkillInfo = wholeString;
                    playerStats.SkillExpPercentage = percentage;
                } else {
                    playerStats.SkillIsShown = false;
                }
            }

        } catch (Exception ignored) {

        }

        try {
            // ABILITY
            String segmentedString = ApecUtils.segmentString(actionBarData, ")",'§',' ',3,1);
            if (segmentedString != null) {
                if (segmentedString.contains("-") && actionBarData.contains(String.valueOf(MnSymbol)) /* This is to make sure that the data is indeed from the action bar */ ) {
                    playerStats.IsAbilityShown = true;
                    playerStats.AbilityText = segmentedString;
                }
            }
        } catch (Exception err) {
            playerStats.IsAbilityShown = false;
        }

        try {
            // DEF
            {
                char dfSymbol = '❈';
                String segmentedString = ApecUtils.segmentString(actionBarData, String.valueOf(dfSymbol), '§', dfSymbol, 2, 1);
                if (segmentedString != null) {
                    playerStats.Defence = Integer.parseInt(ApecUtils.removeAllCodes(segmentedString.replace(",","")));
                    lastDefence = playerStats.Defence;
                } else if (!actionBarData.contains(endRaceSymbol) &&
                        !actionBarData.contains(woodRacingSymbol) &&
                        !actionBarData.contains(dpsSymbol) &&
                        !actionBarData.contains(secSymbol) &&
                        !actionBarData.contains(secretSymbol) &&
                        !actionBarData.contains(chickenRaceSymbol) &&
                        !actionBarData.contains(jumpSymbol) &&
                        !actionBarData.contains(crystalRaceSymbol) &&
                        !actionBarData.contains(giantMushroomSymbol) &&
                        !actionBarData.contains(precursorRuinsSymbol) &&
                        !playerStats.IsAbilityShown &&
                        !playerStats.SkillIsShown)
                // Makes sure that the defence is not replace by something else in the auction bar and it is really 0
                {
                    playerStats.Defence = 0;
                    lastDefence = playerStats.Defence;
                } else {
                    playerStats.Defence = lastDefence;
                }
            }
        } catch (Exception err) {
            playerStats.Defence = lastDefence;
        }

        return playerStats;
    }

    /**
     * @param skillInfo = The text between brackets of the skill xp string
     * @return the precentege of completion until next skill level
     */
    float praseSkillPercentage(String skillInfo) {
        if (skillInfo == null) return -1f;
        if (skillInfo.contains("%")) {
            skillInfo = skillInfo.replace("%","");
            return Float.parseFloat(skillInfo) / 100f;
        } else if (skillInfo.contains("/")){
            String[] twoValues = skillInfo.split("/");
            float first = ApecUtils.hypixelShortValueFormattingToFloat(twoValues[0]);
            float second = ApecUtils.hypixelShortValueFormattingToFloat(twoValues[1]);
            return first / second;
        }
        return -1f;
    }

    public PlayerStats getPlayerStats () {
        return this.playerStats;
    }

    /**
     * Other Data Processing - This contains data that appears in special contexts that aren't really that big to have their own functions and what not
     * In extra info ###
     *    -potion effects
     *    -trials of fire
     *    -wood race
     *    -the end race
     *    -secrets
     *    etc.
     */

    private OtherData ProcessOtherData (ScoreBoardData sd) {
        OtherData otherData = new OtherData();
        if (actionBarData == null || isFromChat(actionBarData)) return otherData;
        String endRace = ApecUtils.segmentString(actionBarData,endRaceSymbol,'§',' ',2,2);
        String woodRacing = ApecUtils.segmentString(actionBarData,woodRacingSymbol,'§',' ',2,2);
        String dps = ApecUtils.segmentString(actionBarData,dpsSymbol,'§',' ',1,1);
        String sec = ApecUtils.segmentString(actionBarData,secSymbol,'§',' ',1,2);
        String secrets = ApecUtils.segmentString(actionBarData,secretSymbol,'§','§',1,1);
        String chickenRace = ApecUtils.segmentString(actionBarData,chickenRaceSymbol,'§',' ',2,2);
        String jump = ApecUtils.segmentString(actionBarData,jumpSymbol,'§','§',3,1);
        String crystalRace = ApecUtils.segmentString(actionBarData,crystalRaceSymbol,'§',' ',2,2);
        String mushroomRace = ApecUtils.segmentString(actionBarData,giantMushroomSymbol,'§',' ',2,2);
        String precursorRace = ApecUtils.segmentString(actionBarData,precursorRuinsSymbol,'§',' ',2,2);

        if ((endRace != null || woodRacing != null || dps != null || sec != null) && !otherData.ExtraInfo.isEmpty()) otherData.ExtraInfo.add(" ");

        if (endRace != null) otherData.ExtraInfo.add(endRace);
        if (woodRacing != null) otherData.ExtraInfo.add(woodRacing);
        if (dps != null) otherData.ExtraInfo.add(dps);
        if (secrets != null) otherData.ExtraInfo.add(secrets);
        // The condition is like this to make sure the actionData is not null
        if (actionBarData.contains("Revive")) {
            otherData.ExtraInfo.add(actionBarData);
        }
        // The revive message also contais the word second so we have to be sure is not that one
        else if (sec != null) otherData.ExtraInfo.add(sec);
        if (chickenRace != null) otherData.ExtraInfo.add(chickenRace);
        if (jump != null) otherData.ExtraInfo.add(jump);
        if (crystalRace != null) otherData.ExtraInfo.add(crystalRace);
        if (mushroomRace != null) otherData.ExtraInfo.add(mushroomRace);
        if (precursorRace != null) otherData.ExtraInfo.add(precursorRace);

        if (actionBarData.contains(armadilloName)) {
            String segmentEnergy = ApecUtils.segmentString(actionBarData,"/",'§','\0' /*placeholder*/,2,1, ApecUtils.SegmentationOptions.TOTALLY_INCLUSIVE);
            if (segmentEnergy != null) {
                String[] values = ApecUtils.removeAllCodes(segmentEnergy).split("/");
                otherData.ArmadilloEnergy = Float.parseFloat(values[0]);
                otherData.ArmadilloBaseEnergy = Float.parseFloat(values[1]);
            }
        }

        String treasureMetalDetectorSymbol = "TREASURE:";
        if (actionBarData.contains(treasureMetalDetectorSymbol)) {
            String segmentedString = ApecUtils.segmentString(actionBarData, treasureMetalDetectorSymbol,'§','m',2,1, ApecUtils.SegmentationOptions.TOTALLY_INCLUSIVE);
            if (segmentedString != null) {
                otherData.ExtraInfo.add(segmentedString);
            }
        }

        List<String> effects = getPlayerEffects();
        if (!effects.isEmpty()) {
            if (!otherData.ExtraInfo.isEmpty()) otherData.ExtraInfo.add(" ");
            otherData.ExtraInfo.addAll(effects);
        }

        otherData.currentEvents = getEvents(sd);

        return otherData;

    }

    /**
     * @param sd = ScoreBoardData
     * @return A list of the current events (see EventIDs.class for the events)
     */

    private ArrayList<EventIDs> getEvents (ScoreBoardData sd) {
        return new ArrayList<EventIDs>();
    }

    /**
     * @return Retuns the effects of the player
     */

    private List<String> getPlayerEffects () {

        try {
            List<String> effects = new ArrayList<String>();
            String[] lines = footerTabData.split("\n");
            if (!lines[2].contains("No effects")) {
                for (int i = 3; i < lines.length; i++) {
                    if (lines[i].contains(":")) {
                        effects.add(lines[i]);
                        break;
                    }
                }
            }
            ApecUtils.orderByWidth(effects);
            return effects;
        } catch (Exception err) {
            return new ArrayList<String>();
        }
    }

    /**
     * @return Returns true if the inventory of the player is full
     */

    private boolean isInvFull () {
        InventoryPlayer ip = mc.thePlayer.inventory;
        for (int i = 0;i < 36;i++) {
            if (ip.getStackInSlot(i) == null) return false;
        }
        return true;
    }

    /**
     * @return Returns the the data in the other data class
     */

    public OtherData getOtherData() {
        return otherData;
    }


    /**
     * @param s = The string that you input
     * @return From two integers divide by '/' (ex 230/500) it returns the vale and the base value (ex 230 is the value and 500 is the base value)
     */

    public Tuple<Integer,Integer> formatStringFractI(String s) {
        String[] tempSplit = s.replace(",","").split("/");
        return new Tuple<Integer, Integer>(Integer.parseInt(tempSplit[0]),Integer.parseInt(tempSplit[1]));
    }

    // Use with caution
    public boolean isFromChat(String s) {
        return (s.contains("[") && s.contains("]")) || (s.startsWith("§7") && s.contains(": "));
    }

    /**
     * The classes that encapsulate all the different types of data
     */

    public static class ScoreBoardData {
        public String Server = "";
        public String Purse = "Purse: §60";
        public String Bits = "Bits: §b0";
        public List<String> ExtraInfo = new ArrayList<String>();
        public String Zone = "";
        public String Date = "";
        public String Hour = "";
        public String IRL_Date = "";
        public String scoreBoardTitle = "";
    }

    public static class PlayerStats {
        public int Hp;
        public int BaseHp;
        public int HealDuration;
        public char HealDurationTicker;
        public int Ap; // Absorption Points
        public int BaseAp; // Base Absorption
        public int Op; // Overflow points
        public int BaseOp; // Base Overflow points
        public int Mp;
        public int BaseMp;
        public int Defence;
        public String SkillInfo;
        public float SkillExpPercentage;
        public boolean SkillIsShown;
        public boolean IsAbilityShown;
        public String AbilityText = "§b-00 Mana (§6Some Ability§b)";
    }

    public static class OtherData {

        public ArrayList<String> ExtraInfo  = new ArrayList<String>();
        public ArrayList<EventIDs> currentEvents = new ArrayList<EventIDs>();
        public float ArmadilloEnergy;
        public float ArmadilloBaseEnergy;

    }
}
