package com.action.knights;

/**
 * @ClassName DamselRescuingKnight
 * @Author WuRui
 * @Date 2021/2/5 21:13
 * @Version 1.0
 * @Description //TODO
 **/
public class DamselRescuingKnight  implements Knight{

    private RescueDamselQuest quest;

    public DamselRescuingKnight() {
        this.quest = new RescueDamselQuest();
    }

    public void embarkOnQuest(){
        quest.embark();
    }
}
