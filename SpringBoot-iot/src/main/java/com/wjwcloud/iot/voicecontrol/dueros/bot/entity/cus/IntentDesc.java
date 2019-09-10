package com.wjwcloud.iot.voicecontrol.dueros.bot.entity.cus;

import java.util.List;

/**
 * 意图描述
 */
public class IntentDesc {
    private String name;
    private List<SlotDesc> slots;

    public IntentDesc(){

    }
    public IntentDesc(String name){
        this.name=name;
    }

    public IntentDesc(String name, List<SlotDesc> slots) {
        this.name = name;
        this.slots = slots;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SlotDesc> getSlots() {
        return slots;
    }

    public void setSlots(List<SlotDesc> slots) {
        this.slots = slots;
    }
}
