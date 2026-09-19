package xyz.kadr.yoko.modules;

import emu.grasscutter.data.excels.monster.MonsterData;
import emu.grasscutter.game.props.ElementType;
import emu.grasscutter.game.props.FightProperty;
import emu.grasscutter.game.world.Position;
import emu.grasscutter.game.world.Scene;
import emu.grasscutter.game.entity.EntityMonster;
import emu.grasscutter.net.proto.SceneEntityInfoOuterClass;
import emu.grasscutter.net.proto.SceneMonsterInfoOuterClass;

public class DPSEntity extends EntityMonster {

    public float dmg;

    public DPSEntity(Scene scene, MonsterData monsterData, Position pos, Position rot, int level) {
        super(scene, monsterData, pos, rot, level);
        this.dmg = 0;
    }

    @Override
    public void damage(float amount, int killerId, ElementType attackType) {
        this.dmg += amount;
        
        //Scale Total Damage to HP bar
        super.damage(this.getFightProperty(FightProperty.FIGHT_PROP_CUR_HP) - (float)(73060 * Math.max(0, 1 - Math.log10(this.dmg / 100 + 1) / 8) + 1), killerId, attackType);
    }

    @Override
    public SceneEntityInfoOuterClass.SceneEntityInfo toProto() {
        // Use Grasscutter's default proto generation to completely avoid missing variables
        SceneEntityInfoOuterClass.SceneEntityInfo.Builder entityInfo = super.toProto().toBuilder();
        SceneMonsterInfoOuterClass.SceneMonsterInfo.Builder monsterInfo = entityInfo.getMonster().toBuilder();
        
        // This crashes his AI and he stays AFK
        monsterInfo.setAiConfigId(12001001);
        
        entityInfo.setMonster(monsterInfo.build());
        return entityInfo.build();
    }
}
