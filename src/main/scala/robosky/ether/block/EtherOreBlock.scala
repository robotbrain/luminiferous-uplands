package robosky.ether.block

import java.util.Random

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.fabricmc.fabric.api.tools.FabricToolTags
import net.minecraft.block.{Block, BlockState, Material}
import net.minecraft.enchantment.{EnchantmentHelper, Enchantments}
import net.minecraft.item.ItemStack
import net.minecraft.util.math.{BlockPos, MathHelper}
import net.minecraft.world.World

class EtherOreBlock(oreType: EtherOreBlock.EtherOreType) extends Block(oreType.blockSettings) {
  override def onStacksDropped(state: BlockState, world: World, pos: BlockPos, stack: ItemStack): Unit =
    if (EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, stack) == 0) {
      val xp = this.getExperienceWhenMined(world.random)
      if (xp > 0) this.dropExperience(world, pos, xp)
    }

  protected def getExperienceWhenMined(rand: Random): Int = MathHelper.nextInt(rand, oreType.xp.start, oreType.xp.end)
}

object EtherOreBlock {
  val oreTypes: IndexedSeq[EtherOreType] = IndexedSeq(OreTypeAegisalt)

  sealed abstract class EtherOreType(val name: String, val xp: Range) {
    val blockSettings: Block.Settings

    def genSettings(): OreGenSettings
  }

  case class OreGenSettings(clusterSize: Int, clusterCount: Int, maxHeight: Int, minHeight: Int, ores: Set[BlockState])

  case object OreTypeAegisalt extends EtherOreType("aegisalt", 2 to 5) {
    override val blockSettings: Block.Settings = FabricBlockSettings.of(Material.STONE)
      .strength(3.0F, 3.0F).breakByTool(FabricToolTags.PICKAXES, 2).build()

    override def genSettings(): OreGenSettings = OreGenSettings(9, 6, 128, 1,
      Set(BlockRegistry.ETHER_ORES(OreTypeAegisalt).getDefaultState))
  }

}