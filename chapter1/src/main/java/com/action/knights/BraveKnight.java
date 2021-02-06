package com.action.knights;

/**
 * @ClassName BraveKnight
 * @Author WuRui
 * @Date 2021/2/5 21:41
 * @Version 1.0
 * @Description //TODO
 **/
public class BraveKnight implements Knight{

    private Quest quest;

    public BraveKnight(Quest quest) {
        this.quest = quest;
    }

    public void embarkOnQuest(){
        quest.embark();
    }
}
