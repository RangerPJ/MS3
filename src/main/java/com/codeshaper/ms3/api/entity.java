package com.codeshaper.ms3.api;

import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.python.core.Py;
import org.python.core.PyBoolean;
import org.python.core.PyException;
import org.python.core.PyList;
import org.python.core.PyObject;
import org.python.core.PyTuple;

import com.codeshaper.ms3.api.exception.MissingScriptException;
import com.codeshaper.ms3.apiBuilder.annotation.PythonClass;
import com.codeshaper.ms3.apiBuilder.annotation.PythonDocString;
import com.codeshaper.ms3.apiBuilder.annotation.PythonFieldGenerated;
import com.codeshaper.ms3.apiBuilder.annotation.PythonFunction;
import com.codeshaper.ms3.apiBuilder.annotation.PythonTypeExclude;
import com.codeshaper.ms3.capability.AttachedScript;
import com.codeshaper.ms3.capability.EntityMs3DataProvider;
import com.codeshaper.ms3.capability.IEntityMs3Data;
import com.codeshaper.ms3.script.RunnableScript;
import com.codeshaper.ms3.util.Assert;
import com.codeshaper.ms3.util.NbtHelper;
import com.codeshaper.ms3.util.Util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketPlayerPosLook.EnumFlags;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.GameType;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class entity {

	public static final entity instance = new entity();

	public static entity.Base<? extends Entity> createWrapperClassForEntity(@Nonnull Entity javaEntity) {
		// Specific types:
		if (javaEntity instanceof AbstractChestHorse) { // Donkey, mule and llama.
			return entity.instance.new ChestHorse((AbstractChestHorse) javaEntity);
		} else if (javaEntity instanceof EntityArmorStand) {
			return entity.instance.new ArmorStand((EntityArmorStand) javaEntity);
		} else if (javaEntity instanceof EntityBat) {
			return entity.instance.new Bat((EntityBat) javaEntity);
		} else if (javaEntity instanceof EntityBoat) {
			return entity.instance.new Boat((EntityBoat) javaEntity);
		} else if (javaEntity instanceof EntityChicken) {
			return entity.instance.new Chicken((EntityChicken) javaEntity);
		} else if (javaEntity instanceof EntityCreeper) {
			return entity.instance.new Creeper((EntityCreeper) javaEntity);
		} else if (javaEntity instanceof EntityEnderman) {
			return entity.instance.new Enderman((EntityEnderman) javaEntity);
		} else if (javaEntity instanceof EntityDragon) {
			return entity.instance.new EnderDragon((EntityDragon) javaEntity);
		} else if (javaEntity instanceof EntityEndermite) {
			return entity.instance.new Endermite((EntityEndermite) javaEntity);
		} else if (javaEntity instanceof EntityGhast) {
			return entity.instance.new Ghast((EntityGhast) javaEntity);
		} else if (javaEntity instanceof EntityHorse || javaEntity instanceof EntityZombieHorse) {
			return entity.instance.new Horse((EntityHorse) javaEntity);
		} else if (javaEntity instanceof EntityHusk) {
			return entity.instance.new Husk((EntityHusk) javaEntity);
		} else if (javaEntity instanceof EntityItem) {
			return entity.instance.new ItemEntity((EntityItem) javaEntity);
		} else if (javaEntity instanceof EntityIronGolem) {
			return entity.instance.new IronGolem((EntityIronGolem) javaEntity);
		} else if (javaEntity instanceof EntityOcelot) {
			return entity.instance.new Ocelot((EntityOcelot) javaEntity);
		} else if (javaEntity instanceof EntityPainting) {
			return entity.instance.new Painting((EntityPainting) javaEntity);
		} else if (javaEntity instanceof EntityParrot) {
			return entity.instance.new Parrot((EntityParrot) javaEntity);
		} else if (javaEntity instanceof EntityPig) {
			return entity.instance.new Pig((EntityPig) javaEntity);
		} else if (javaEntity instanceof EntityPlayer) {
			return entity.instance.new Player((EntityPlayerMP) javaEntity);
		} else if (javaEntity instanceof EntityRabbit) {
			return entity.instance.new Rabbit((EntityRabbit) javaEntity);
		} else if (javaEntity instanceof EntitySheep) {
			return entity.instance.new Sheep((EntitySheep) javaEntity);
		} else if (javaEntity instanceof EntityShulker) {
			return entity.instance.new Shulker((EntityShulker) javaEntity);
		} else if (javaEntity instanceof EntitySkeletonHorse) {
			return entity.instance.new SkeletonHorse((EntitySkeletonHorse) javaEntity);
		} else if (javaEntity instanceof EntityTNTPrimed) {
			return entity.instance.new Tnt((EntityTNTPrimed) javaEntity);
		} else if (javaEntity instanceof EntitySlime || javaEntity instanceof EntityMagmaCube) {
			return entity.instance.new Slime((EntitySlime) javaEntity);
		} else if (javaEntity instanceof EntitySnowman) {
			return entity.instance.new Snowman((EntitySnowman) javaEntity);
		} else if (javaEntity instanceof EntityVillager) {
			return entity.instance.new Villager((EntityVillager) javaEntity);
		} else if (javaEntity instanceof EntityVindicator) {
			return entity.instance.new Vindicator((EntityVindicator) javaEntity);
		} else if (javaEntity instanceof EntityWolf) {
			return entity.instance.new Wolf((EntityWolf) javaEntity);
		} else if (javaEntity instanceof EntityZombie) {
			return entity.instance.new Zombie<>((EntityZombie) javaEntity);
		} else if (javaEntity instanceof EntityZombieVillager) {
			return entity.instance.new ZombieVillager((EntityZombieVillager) javaEntity);
		}

		// More generic types:

		else if (javaEntity instanceof EntityTameable) {
			return entity.instance.new Tameable<>((EntityTameable) javaEntity);
		} else if (javaEntity instanceof EntityAnimal) {
			return entity.instance.new Animal<>((EntityAnimal) javaEntity);
		} else if (javaEntity instanceof EntityHanging) {
			return entity.instance.new Hanging<>((EntityHanging) javaEntity);
		} else if (javaEntity instanceof EntityLiving) {
			return entity.instance.new Living<>((EntityLiving) javaEntity);
		} else if (javaEntity instanceof EntityLivingBase) {
			return entity.instance.new LivingBase<>((EntityLivingBase) javaEntity);
		}

		// Most generic type.

		else {
			return entity.instance.new Base<>(javaEntity);
		}
	}

	/**
	 * Equivalent to {@link net.minecraft.entity.Entity}.
	 */
	@PythonClass
	public class Base<T extends Entity> extends PyObject {

		public T mcEntity;

		public Base(T entity) {
			this.mcEntity = entity;
		}

		@PythonFunction
		@PythonDocString("Returns the Entity's internal java instance.  For advanced users only!")
		public T getJavaEntity() {
			return this.mcEntity;
		}

		@PythonFunction
		@PythonDocString("Returns the entity's name as a string as seen in entityList.py.")
		public String getEntityName() {
			return this instanceof entity.Player ? entityList.PLAYER
					: EntityList.getKey(this.mcEntity).getResourcePath();
		}

		@PythonFunction
		@PythonDocString("Returns the Entity's X position as a float.")
		public double getX() {
			return this.mcEntity.posX;
		}

		@PythonFunction
		@PythonDocString("Returns the Entity's Y position as a float.")
		public double getY() {
			return this.mcEntity.posY;
		}

		@PythonFunction
		@PythonDocString("Returns the Entity's Z position as a float.")
		public double getZ() {
			return this.mcEntity.posZ;
		}

		@PythonFunction
		@PythonDocString("Returns the Entity's position as a tuple of (x, y, z).")
		public PyTuple getPosition() {
			return Util.makeTuple(this.getX(), this.getY(), this.getZ());
		}

		@PythonFunction
		@PythonDocString("Returns the Entity's yaw.")
		public double getYaw() {
			return this.mcEntity.rotationYaw;
		}

		@PythonFunction
		@PythonDocString("Returns the Entity's pitch.")
		public double getPitch() {
			return this.mcEntity.rotationPitch;
		}

		@PythonFunction
		@PythonDocString("Returns the Entity's rotation as a tuple of (yaw, pitch).")
		public PyTuple getRotation() {
			return Util.makeTuple(this.mcEntity.rotationYaw, this.mcEntity.rotationPitch);
		}

		@PythonFunction
		@PythonDocString("Returns the Entity's motion as a tuple of (xMotion, yMotion, zMotion).")
		public PyTuple getMotion() {
			return Util.makeTuple(this.mcEntity.motionX, this.mcEntity.motionY, this.mcEntity.motionZ);
		}

		@PythonFunction
		@PythonDocString("Sets the Entity's position.  Note, due to a Minecraft bug, this function will not work on Entities spawned on the same tick.")
		public void setPosition(double x, double y, double z) {
			this.mcEntity.setLocationAndAngles(x, y, z, this.mcEntity.rotationYaw, this.mcEntity.rotationPitch);
		}

		@PythonFunction
		@PythonDocString("Sets the Entity's yaw and pitch in degrees.  Yaw is y axis (side to side) and pitch x axis (up and down).")
		public void setRotation(float yaw, float pitch) {
			yaw = MathHelper.wrapDegrees(yaw);
			pitch = MathHelper.wrapDegrees(pitch);
			this.mcEntity.rotationYaw = yaw;
			this.mcEntity.rotationPitch = pitch;
			this.mcEntity.setRotationYawHead(yaw);
		}

		@PythonFunction
		public void setMotion(float x, float y, float z) {
			this.mcEntity.motionX = x;
			this.mcEntity.motionY = y;
			this.mcEntity.motionZ = z;
		}

		@PythonFunction
		@PythonDocString("Returns True if the Entity is touching the ground.")
		public boolean isOnGround() {
			return this.mcEntity.onGround;
		}

		@PythonFunction
		@PythonDocString("Returns True if the Entity is sneaking.")
		public boolean isSneaking() {
			return this.mcEntity.isSneaking();
		}

		@PythonFunction
		@PythonDocString("Returns True if the Entity is sprinting.")
		public boolean isSprinting() {
			return this.mcEntity.isSprinting();
		}

		@PythonFunction
		@PythonDocString("Sets how long the Entity is on fire in ticks.  Enchantments will effect this.")
		public void setFire(int ticks) {
			this.mcEntity.setFire(ticks * 20); // Method accepts seconds, not ticks.
		}

		@PythonFunction
		@PythonDocString("Returns how much air the Entity has.  0 is no air left and drowning will happen.")
		public int getAir() {
			return this.mcEntity.getAir();
		}

		@PythonFunction
		@PythonDocString("Sets how much air the Entity has. 300 is max, 0 is no air.")
		public void setAir(int air) {
			this.mcEntity.setAir(air);
		}

		// TODO fire getter, riding/passengers (TEST this one for sure)

		@PythonFunction
		@PythonDocString("Returns a list of all of the passengers riding this Entity.")
		public PyList getPassengers() {
			List<Entity> passengers = this.mcEntity.getPassengers();
			PyList list = new PyList();
			for (Entity e : passengers) {
				list.add(entity.createWrapperClassForEntity(e));
			}
			return list;
		}

		@PythonFunction
		public boolean isInvulnerable() {
			return this.mcEntity.getIsInvulnerable();
		}

		@PythonFunction
		public void setInvulnerable(boolean flag) {
			this.mcEntity.setEntityInvulnerable(flag);
		}

		@PythonFunction
		public boolean isSilent() {
			return this.mcEntity.isSilent();
		}

		@PythonFunction
		@PythonDocString("If True, the Entity will not make any noise.")
		public void setSilent(boolean isSilent) {
			this.mcEntity.setSilent(isSilent);
		}

		@PythonFunction
		public boolean isNoGravity() {
			return this.mcEntity.hasNoGravity();
		}

		@PythonFunction
		@PythonDocString("If True, the Entity will not be affected by gravity.")
		public void setNoGravity(boolean noGravity) {
			this.mcEntity.setNoGravity(noGravity);
		}

		@PythonFunction
		@PythonDocString("Returns the Entity's custom name as a string.")
		public String getCustomName() {
			return this.mcEntity.getCustomNameTag();
		}

		@PythonFunction
		@PythonDocString("Sets the Entity's name.  Color codes can be used.")
		public void setCustomName(String name) {
			this.mcEntity.setCustomNameTag(Util.correctColorCode(name));
		}

		@PythonFunction
		public boolean isCustomNameVisible() {
			return this.mcEntity.getAlwaysRenderNameTag();
		}

		@PythonFunction
		@PythonDocString("If True, the Entity's name will always be visible, if False, the cursor must be on the Entity to see the name.")
		public void setCustomNameVisible(boolean alwaysRenderNameTag) {
			this.mcEntity.setAlwaysRenderNameTag(alwaysRenderNameTag);
		}

		@PythonFunction
		@PythonDocString("Returns True if the Entity has the glowing effect.")
		public boolean isGlowing() {
			return this.mcEntity.isGlowing();
		}

		@PythonFunction
		@PythonDocString("Sets the Entity to be glowing.")
		public void setGlowing(boolean isGlowing) {
			this.mcEntity.setGlowing(isGlowing);
		}

		@PythonFunction
		@PythonDocString("Returns the value of an NBT tag on this Entity.  If the tag can't be found, None is returned.")
		@Nullable
		public Object getTag(String tagKey) {
			NBTBase tag = CommandBase.entityToNBT(this.mcEntity).getTag(tagKey);
			return NbtHelper.readObjFromNbt(tag);
		}

		@PythonFunction
		@PythonDocString("Sets an NBT tag for the Entity.  For byte tags that represtend boolean values 1/0, \"true\"/\"false\" or True/False will work.  For nested tags type them like you would for the /entitydata command, but without the beginning and ending curly braces {}")
		public void setTag(String tagKey, @PythonTypeExclude Object value) {
			if (tagKey.startsWith("{") || tagKey.endsWith("}")) {
				throw Py.ValueError("tag string can not start or end with curly brackets (\"{\", \"}\")");
			}

			NBTTagCompound originalNbt = CommandBase.entityToNBT(this.mcEntity);
			NBTTagCompound nbtTagCopy = originalNbt.copy();
			NBTTagCompound newNbt;

			try {
				newNbt = JsonToNBT.getTagFromJson("{" + tagKey + ":" + value.toString() + "}");
			} catch (NBTException nbtexception) {
				throw Py.ValueError("Nbt was not in valid JSON format!");
			}

			UUID uuid = this.mcEntity.getUniqueID();
			originalNbt.merge(newNbt);
			this.mcEntity.setUniqueId(uuid);

			this.mcEntity.readFromNBT(originalNbt);
		}

		@PythonFunction
		@PythonDocString("Sends a chat message to the Entity.  For Players this will show up in chat for them.")
		public void sendChatMessage(String message) {
			this.mcEntity.sendMessage(new TextComponentString(Util.correctColorCode(message)));
		}

		@PythonFunction
		@PythonDocString("Binds a script to this Entity, so it will execute every tick.  scriptPath is the path from the /scripts folder.")
		public void bindScript(String scriptPath) throws PyException, MissingScriptException {
			this.bindScript(scriptPath, null);
		}

		@PythonFunction
		@PythonDocString("Binds a script to this Entity, so it will execute every tick.  scriptPath is the path from the /scripts folder.")
		public void bindScript(String scriptPath, @Nullable PyList args) throws PyException, MissingScriptException {
			RunnableScript runnableScript = new RunnableScript(scriptPath, args);
			if(!runnableScript.exists()) {
				throw Py.ValueError("Can not bind script " + scriptPath + ", file does not exist");
			}
			boolean successfull = this.getCapability().addBoundScript(this, runnableScript);
			if (!successfull) {
				throw Py.ValueError("Entity already has a script bound with the same type");
			}
		}

		@PythonFunction
		@PythonDocString("Gets a script that has been bound to the entity.  If the passed script has not been bound, None is returned.")
		public BoundObject getBoundScript(String scriptPath) {
			IEntityMs3Data ms3EntityData = this.getCapability();
			AttachedScript as = ms3EntityData.getBoundScript(new RunnableScript(scriptPath));
			if (as == null) {
				return null;
			} else {
				return as.getInstance();
			}
		}

		@PythonFunction
		@PythonDocString("Removes a specific script that has been bound to this Entity.")
		public void removeBoundScript(String scriptPath) {
			RunnableScript rs = new RunnableScript(scriptPath);
			this.getCapability().removeBoundScript(rs);
		}

		@PythonFunction
		@PythonDocString("Removes all scripts that have been bound to this Entity.")
		public void removeAllBoundScripts(String scriptPath) {
			IEntityMs3Data ms3EntityData = this.getCapability();
			ms3EntityData.removeAllBoundScripts();
		}

		@PythonFunction
		@PythonDocString("Returns a custom property, or None if the property can't be found.")
		public Object getProperty(String propertyName) throws PyException {
			this.validatePropertyName(propertyName);

			IEntityMs3Data ms3EntityData = this.getCapability();
			return ms3EntityData.getCustomProperty(propertyName);
		}

		@PythonFunction
		@PythonDocString("Sets a custom property, overwriting the previous one if it exists.  Pass None for the value argument to remove the property.")
		public void setProperty(String propertyName, @PythonTypeExclude Object value) throws PyException {
			this.validatePropertyName(propertyName);

			IEntityMs3Data ms3EntityData = this.getCapability();

			if (value == null) {
				ms3EntityData.removeCustomProperty(propertyName);
			} else {
				if (value instanceof Number || value instanceof String || value instanceof entity.Base) {
					ms3EntityData.setCustomProperty(propertyName, value);
				} else {
					throw Py.ValueError("value must be a string, number, instance of entity.Base or None");
				}
			}
		}

		@PythonFunction
		@PythonDocString("Checks if this Entity has the specified property.")
		public boolean hasProperty(String propertyName) throws PyException {
			this.validatePropertyName(propertyName);

			IEntityMs3Data ms3EntityData = this.getCapability();
			return ms3EntityData.getCustomProperty(propertyName) != null;
		}

		@PythonFunction
		@PythonDocString("If True, the Entity is still alive and isn't in the process of dying.")
		public boolean isAlive() {
			return this.mcEntity.isEntityAlive();
		}

		@Override
		public PyObject __eq__(PyObject other) {
			if (other == null) {
				return Py.False;
			}
			if (other instanceof Base) {
				Base<?> e = (Base<?>) other;
				return new PyBoolean(e.mcEntity.getUniqueID().equals(this.mcEntity.getUniqueID()));
			}
			return Py.False;
		}

		@Override
		public PyObject __ne__(PyObject other) {
			return Util.pyNotHelper(this.__eq__(other));
		}

		/**
		 * Gets the Ms3 Capability on this Entity.
		 */
		private IEntityMs3Data getCapability() {
			return this.mcEntity.getCapability(EntityMs3DataProvider.ENTITY_MS3_DATA_CAP, null);
		}

		/**
		 * Makes sure a propertyName is valid (not null or an empty String).
		 *
		 * @throws PyException In the form of a ValueError if it is not valid.
		 */
		private void validatePropertyName(String propName) throws PyException {
			if (StringUtils.isNullOrEmpty(propName)) {
				throw Py.ValueError("propertyName may not be None or empty");
			}
		}
	}

	/**
	 * Equivalent to {@link net.minecraft.entity.EntityLivingBase}.
	 */
	@PythonClass
	public class LivingBase<T extends EntityLivingBase> extends Base<T> {

		public LivingBase(T entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Returns the Entity's health as an int.")
		public int getHealth() {
			return (int) this.mcEntity.getHealth();
		}

		@PythonFunction
		@PythonDocString("Sets the Entity's health.  Pass -1 to set it to their max health.")
		public void setHealth(int health) {
			this.mcEntity.setHealth(health == -1 ? this.mcEntity.getMaxHealth() : health);
		}

		@PythonFunction
		@PythonDocString("Returns the Entity's maximum health that they can regenerate to.")
		public int getMaxHealth() {
			return (int) this.mcEntity.getMaxHealth();
		}
	}

	/**
	 * Equivalent to {@link net.minecraft.entity.EntityLiving}.
	 */
	@PythonClass
	public class Living<T extends EntityLiving> extends LivingBase<T> implements IHasEquipment {

		public Living(T entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Makes the entity look at the passed position.  yawSpeed and pitchSpeed determine how fast the entity will look at the target.")
		public void lookAt(PyTuple position, float yawSpeed, float pitchSpeed) {
			this.mcEntity.getLookHelper().setLookPosition((double) position.get(0), (double) position.get(1),
					(double) position.get(2), yawSpeed, pitchSpeed);
		}

		@PythonFunction
		@PythonDocString("If True, the Entity has no AI.")
		public boolean isNoAi() {
			return this.mcEntity.isAIDisabled();
		}

		@PythonFunction
		@PythonDocString("Sets if the Entity has no AI.")
		public void setNoAi(boolean noAi) {
			this.mcEntity.setNoAI(noAi);
		}

		@PythonFunction
		@PythonDocString("Returns True if the Entity can pick up dropped items.")
		public boolean getCanPickUpLoot() {
			return this.mcEntity.canPickUpLoot();
		}

		@PythonFunction
		@PythonDocString("Lets the Entity pick up dropped items.")
		public void setCanPickUpLoot(boolean canPickUpLoot) {
			this.mcEntity.setCanPickUpLoot(canPickUpLoot);
		}

		@PythonFunction
		@PythonDocString("Returns True if the Entity is left handed.")
		public boolean isLeftHanded() {
			return this.mcEntity.isLeftHanded();
		}

		@PythonFunction
		@PythonDocString("Sets the Entity's dominate hand.")
		public void setLeftHanded(boolean isLeftHanded) {
			this.mcEntity.setLeftHanded(isLeftHanded);
		}

		@PythonFunction
		@PythonDocString("Returns an itemStack representing the contents of the slot, or None if it is empty.  See equipmentSlot.py for index constants.")
		@Override
		@Nullable
		public itemStack getSlotContents(int slotIndex) {
			Assert.isIndexInBounds(slotIndex, 6);
			return itemStack.makeWrapper(this.mcEntity.getItemStackFromSlot(equipmentSlot.indexToEnum(slotIndex)));
		}

		@PythonFunction
		@PythonDocString("Sets the contents of a slot.  Pass None for the itemStack to set the slot to be empty.  See equipmentSlot.py for index constants.")
		@Override
		public void setSlotContents(int slotIndex, @Nullable itemStack itemStack) {
			Assert.isIndexInBounds(slotIndex, 6);
			ItemStack s;
			if (itemStack == null || itemStack.getCount() <= 0) {
				s = ItemStack.EMPTY;
			} else {
				s = itemStack.getMcStack();
			}
			this.mcEntity.setItemStackToSlot(equipmentSlot.indexToEnum(slotIndex), s);
		}
	}

	/**
	 * Equivalent to {@link net.minecraft.entity.passive.EntityAnimal}.
	 */
	@PythonClass
	public class Animal<T extends EntityAnimal> extends Living<T> {

		public Animal(T entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Returns true if the Entity's \"InLove\" tag is greater than 0, meaning it is still trying to breed.")
		public boolean isInLove() {
			return this.mcEntity.isInLove();
		}

		@PythonFunction
		@PythonDocString("Sets the Entity's \\\"InLove\\\" tag to 600, causing it to try and breed for that many ticks.  entityPlayer is whoever set the entity into love, used for achievements and stats.  entityPlayer can be an instance of entity.Player or None.")
		public void setInLove(@Nullable entity.Player entityPlayer) {
			if (entityPlayer instanceof entity.Player) {
				this.mcEntity.setInLove(((entity.Player) entityPlayer).mcEntity);
			} else {
				this.mcEntity.setInLove(null);
			}
		}

		@PythonFunction
		@PythonDocString("Returns the Entity's age as an int.")
		public int getAge() {
			return this.mcEntity.getGrowingAge();
		}

		@PythonFunction
		@PythonDocString("Sets the Entity's age.  If the age is negative, the mob is a baby and it is incremented every tick until the mob grows up.  If age is above 0, that's how many ticks until the mob can breed again and it is decremented every tick..")
		public void setAge(int age) {
			this.mcEntity.setGrowingAge(age);
		}

		// ForcedAge is protected.
	}

	@PythonClass
	public class ArmorStand extends LivingBase<EntityArmorStand> {

		public ArmorStand(EntityArmorStand entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Returns True if the Armor Stand has no base plate, False if it does.")
		public boolean hasNoBaseplate() {
			return this.mcEntity.hasNoBasePlate();
		}

		@PythonFunction
		@PythonDocString("If True, the Armor Stand will have a size of 0, a tiny hitbox interactions will be disabled.")
		public boolean isMarker() {
			return this.mcEntity.hasMarker();
		}

		@PythonFunction
		@PythonDocString("Sets if the ArmorStand is a marker or not.")
		public void setMarker(boolean marker) {
			this.setTag("Marker", marker);
		}
	}

	@PythonClass
	public class Bat extends Living<EntityBat> {

		public Bat(EntityBat entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Returns True if the bat is hanging from a block, False if it is flying.")
		public boolean isHanging() {
			return this.mcEntity.getIsBatHanging();
		}

		@PythonFunction
		@PythonDocString("Sets if the Bat is hanging from a block.")
		public void setHanging(boolean isHanging) {
			this.mcEntity.setIsBatHanging(isHanging);
		}
	}

	@PythonClass
	public class Boat extends Base<EntityBoat> {

		@PythonFieldGenerated
		public static final String TYPE_OAK = "oak";
		@PythonFieldGenerated
		public static final String TYPE_SPRUCE = "spruce";
		@PythonFieldGenerated
		public static final String TYPE_BIRCH = "birch";
		@PythonFieldGenerated
		public static final String TYPE_JUNGLE = "jungle";
		@PythonFieldGenerated
		public static final String TYPE_ACACIA = "acacia";
		@PythonFieldGenerated
		public static final String TYPE_DARK_OAK = "dark_oak";

		public Boat(EntityBoat entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Sets the Boat's type.  Defaults to \"oak\" if the name is invalid.")
		public void setBoatType(String type) {
			this.mcEntity.setBoatType(EntityBoat.Type.getTypeFromString(type));
		}

		@PythonFunction
		@PythonDocString("Returns the name identifier of the Boat's type as a string.")
		public String getBoatType() {
			return this.mcEntity.getBoatType().getName();
		}
	}

	@PythonClass
	public class Chicken extends Animal<EntityChicken> {

		public Chicken(EntityChicken entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Returns the number of ticks until an egg is layed.")
		public int getEggLayTime() {
			return this.mcEntity.timeUntilNextEgg;
		}

		@PythonFunction
		@PythonDocString("Sets the number of ticks until an egg is layed.")
		public void setEggLayTime(int eggLayTime) {
			this.mcEntity.timeUntilNextEgg = eggLayTime;
		}

		@PythonFunction
		@PythonDocString("Returns True if the Chicken is a jockey.")
		public boolean isChickenJockey() {
			return this.mcEntity.isChickenJockey();
		}

		@PythonFunction
		@PythonDocString("Sets the IsChickenJockey flag.  This will not spawn a baby zombie, it only sets the flag.")
		public void setChickenJockey(boolean isChickenJockey) {
			this.mcEntity.setChickenJockey(isChickenJockey);
		}
	}

	@PythonClass
	public class Creeper extends Living<EntityCreeper> {

		public Creeper(EntityCreeper entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("If True, the Creeper is charged.")
		public boolean isCharged() {
			return this.mcEntity.getPowered();
		}

		@PythonFunction
		@PythonDocString("Returns True if the Creeper is charged, False otherwise.")
		public void setCharged(boolean ignited) {
			this.setTag("powered", ignited);
		}

		@PythonFunction
		@PythonDocString("Returns the radius of the Creeper's explosion.")
		public int getExplosionRadius() {
			return (int) this.getTag("ExplosionRadius");
		}

		@PythonFunction
		@PythonDocString("Sets the explosion radius of the Creeper.  The default is 3.")
		public void setExplosionRadius(int radius) {
			this.setTag("ExplosionRadius", radius);
		}

		@PythonFunction
		@PythonDocString("Returns True if the Creeper has been lit by Fint and Steel.")
		public boolean isIgnited() {
			return this.mcEntity.hasIgnited();
		}

		@PythonFunction
		@PythonDocString("Sets if the Creeper has been ignited and is about to explode.")
		public void setIgnited(boolean ignited) {
			this.setTag("ignited", ignited);
		}
	}

	@PythonClass
	public class EnderDragon extends Living<EntityDragon> {

		@PythonFieldGenerated
		@PythonDocString("The Dragon is circling the island.")
		public static final int PHASE_CIRCLING = 0;
		@PythonFieldGenerated
		@PythonDocString("The Dragon is about to shoot a fireball.")
		public static final int PHASE_STRAFING_PLAYER = 1;
		@PythonFieldGenerated
		@PythonDocString("The Dragon is flying to the portal to land.  this is the transition to the landed state.")
		public static final int PHASE_LANDING_APPROACH = 2;
		@PythonFieldGenerated
		@PythonDocString("The Dragon has landed on the portal.")
		public static final int PHASE_LANDING = 3;
		@PythonFieldGenerated
		@PythonDocString("The Dragon is taking off from the portal.  This is the transition out of landed state.")
		public static final int PHASE_TAKEOFF = 4;
		@PythonFieldGenerated
		public static final int PHASE_SITTING_FLAMING = 5;
		@PythonFieldGenerated
		public static final int PHASE_SITTING_SCANNING = 6;
		@PythonFieldGenerated
		public static final int PHASE_SITTING = 7;
		@PythonFieldGenerated
		public static final int PHASE_CHARGING_PLAYER = 8;
		@PythonFieldGenerated
		@PythonDocString("Flying to portal to die.")
		public static final int PHASE_DYING = 9;
		@PythonFieldGenerated
		@PythonDocString("Hovering with no AI. (Default when summoned via /summon)")
		public static final int PHASE_HOVER = 10;

		public EnderDragon(EntityDragon entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Returns the Ender Dragons current attack phase as an int.")
		public int getPhase() {
			return this.mcEntity.getPhaseManager().getCurrentPhase().getPhaseList().getId();
		}

		@PythonFunction
		@PythonDocString("Sets the Ender Dragons current attack phase.")
		public void setPhase(int phase) {
			this.mcEntity.getPhaseManager().setPhase(PhaseList.getById(phase));
		}

	}

	@PythonClass
	public class Enderman extends Living<EntityEnderman> {

		public Enderman(EntityEnderman entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Returns an itemStack of what the Enderman is carrying, or None if it isn't carrying anything.")
		@Nullable
		public itemStack getCarried() {
			IBlockState state = this.mcEntity.getHeldBlockState();
			if (state != null) {
				Block block = state.getBlock();
				return itemStack.makeWrapper(new ItemStack(block, 1, block.getMetaFromState(state)));
			} else {
				return null;
			}
		}

		@PythonFunction
		@PythonDocString("Sets the Enderman's carried block.  Pass an itemStack to set the held block, or None if it shouldn't carry anything.")
		@Nullable
		public void setCarried(itemStack stack) {
			if (stack == null) {
				this.mcEntity.setHeldBlockState(null);
			} else {
				Item i = stack.getMcStack().getItem();
				if (i instanceof ItemBlock) {
					ItemBlock ib = ((ItemBlock) i);
					this.mcEntity.setHeldBlockState(ib.getBlock().getStateFromMeta(stack.getMeta()));
				} else {
					throw Py.ValueError("Enderman can not carry items, only blocks");
				}
			}
		}
	}

	@PythonClass
	public class Endermite extends Living<EntityEndermite> {

		public Endermite(EntityEndermite entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Sets if the Endermite is player spawned.")
		public boolean isPlayerSpawned() {
			return this.mcEntity.isSpawnedByPlayer();
		}

		@PythonFunction
		@PythonDocString("If true, Endermen will attack this Endermite.")
		public void setPlayerSpawned(boolean spawnedByPlayer) {
			this.mcEntity.setSpawnedByPlayer(spawnedByPlayer);
		}

		// lifetime is private.
	}

	@PythonClass
	public class Ghast extends Living<EntityGhast> {

		public Ghast(EntityGhast entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Returns the Ghast's fireball strength as an int.")
		public int getExplosionRadius() {
			return this.mcEntity.getFireballStrength();
		}

		@PythonFunction
		@PythonDocString("Sets the Ghast's fireball explosion radius.  The default is 1.")
		public void setExplosionRadius(int power) {
			this.setTag("ExplosionPower", power);
		}
	}

	@PythonClass
	public class ItemEntity extends Base<EntityItem> {

		public ItemEntity(EntityItem entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Sets the Item's age.  Once it's age reaches 6000 the item is destroyed.")
		public int getAge() {
			return this.mcEntity.getAge();
		}

		@PythonFunction
		@PythonDocString("Returns an itemStack representing the stack.")
		public itemStack getStack() {
			return itemStack.makeWrapper(this.mcEntity.getItem());
		}

		@PythonFunction
		@PythonDocString("Sets the Item Stack that this entity represents.")
		public void setStack(itemStack stack) {
			if (stack == null) {
				throw Py.ValueError("stack can not be None");
			}
			this.mcEntity.setItem(stack.getMcStack());
		}

		@PythonFunction
		@PythonDocString("Sets the item's pickup delay to 32767 to stop it from being able to be picked up.")
		public void setInfinitePickupDelay() {
			this.mcEntity.setInfinitePickupDelay();
		}

		@PythonFunction
		@PythonDocString("Sets the item's age to -6000 to stop it from despawning.")
		public void setNoDespawn() {
			this.setNoDespawn();
		}
	}

	/**
	 * Equivalent to {@link net.minecraft.entity.passive.AbstractHorse}.
	 */
	@PythonClass
	public class GenericHorse<T extends AbstractHorse> extends Animal<T> {

		public GenericHorse(T entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("If True, the house is in love mode.")
		public boolean isBreeding() {
			return this.mcEntity.isBreeding();
		}

		@PythonFunction
		public void setBreeding(boolean breed) {
			this.mcEntity.setBreeding(breed);
		}

		@PythonFunction
		@PythonDocString("Returns True if the Horse is grazing the ground.  Used for animations.")
		public boolean isEatingHaystack() {
			return this.mcEntity.isEatingHaystack();
		}

		@PythonFunction
		@PythonDocString("Sets if the Horse is grazing.  Used for animations.")
		public void setEatingHaystack(boolean eating) {
			this.mcEntity.setEatingHaystack(eating);
		}

		@PythonFunction
		@PythonDocString("Returns True if the Horse is taimed, False if it is not.")
		public boolean isTame() {
			return this.mcEntity.isTame();
		}

		@PythonFunction
		@PythonDocString("Sets if the Horse is tamed.")
		public void setTamed(boolean tamed) {
			this.mcEntity.setHorseTamed(tamed);
		}

		@PythonFunction
		@PythonDocString("Returns the Horse's temper as an int.")
		public int getTemper() {
			return this.mcEntity.getTemper();
		}

		@PythonFunction
		@PythonDocString("Sets the Horse's temper.  Higher values make it easier to tame.")
		public void setTemper(int temper) {
			this.mcEntity.setTemper(temper);
		}

		@PythonFunction
		@PythonDocString("Returns True if the Horse is wearing a saddle, False if it isn't.")
		public boolean isSaddled() {
			return this.mcEntity.isHorseSaddled();
		}

		@PythonFunction
		@PythonDocString("Sets if the Horse is wearing a saddle.")
		public void setSaddled(boolean saddled) {
			this.mcEntity.setHorseSaddled(saddled);
		}
	}

	/**
	 * Equivalent to {@link net.minecraft.entity.EntityHanging}.
	 */
	@PythonClass
	@PythonDocString("Class to represent Entities that hang on a wall.")
	public class Hanging<T extends EntityHanging> extends Base<T> {

		public Hanging(T entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Returns the Entity's direction as an int.  0 = south, 1 = west, 2 = north, 3 = east.")
		public int getFacing() {
			return this.mcEntity.facingDirection.getHorizontalIndex();
		}

		@PythonFunction
		@PythonDocString("Sets the direction that the Entity is facing.  0 = south, 1 = west, 2 = north, 3 = east.")
		public void setFacing(int facing) {
			this.setTag("Facing", EnumFacing.getHorizontal(facing));
		}

		@PythonFunction
		@PythonDocString("Returns the Entity's hanging position as a tuple of (x, y, z).")
		public PyTuple getTilePos() {
			BlockPos pos = this.mcEntity.getHangingPosition();
			return Util.makeTuple(pos.getX(), pos.getY(), pos.getZ());
		}

		@PythonFunction
		@PythonDocString("Sets the hanging position of the Entity.")
		public void setTilePos(int x, int y, int z) {
			this.mcEntity.setPosition(x, y, z); // Sets hanging position in method.
		}
	}

	/**
	 * Equivalent to {@link net.minecraft.entity.passive.EntityHorse}
	 */
	@PythonClass
	public class Horse extends GenericHorse<EntityHorse> {

		@PythonFieldGenerated
		public static final int COLOR_WHITE = 0;
		@PythonFieldGenerated
		public static final int COLOR_CREAMY = 1;
		@PythonFieldGenerated
		public static final int COLOR_CHESTNUT = 2;
		@PythonFieldGenerated
		public static final int COLOR_BROWN = 3;
		@PythonFieldGenerated
		public static final int COLOR_BLACK = 4;
		@PythonFieldGenerated
		public static final int COLOR_GRAY = 5;
		@PythonFieldGenerated
		public static final int COLOR_DARK_BROWN = 6;

		@PythonFieldGenerated
		public static final int MARKING_NONE = 0;
		@PythonFieldGenerated
		public static final int MARKING_WHITE = 1;
		@PythonFieldGenerated
		public static final int MARKING_WHITE_FIELD = 2;
		@PythonFieldGenerated
		public static final int MARKING_WHITE_DOTS = 3;
		@PythonFieldGenerated
		public static final int MARKING_BLACK_DOTS = 4;

		public Horse(EntityHorse entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Returns the Horse's variant as an int.")
		public int getVariant() {
			return this.mcEntity.getHorseVariant();
		}

		@PythonFunction
		@PythonDocString("Sets the Horse's variant.  Use the computeVariantFlag function to get a variant to pass in.")
		public void setVariant(int variant) {
			this.mcEntity.setHorseVariant(variant);
		}

		@PythonFunction
		@PythonDocString("Returns a variant integer from a color and marking.  Algorithm is (color | markings << 8)")
		public int computeVariantFlag(int color, int markings) {
			int variant = color | markings << 8;
			return variant;
		}

		@PythonFunction
		@PythonDocString("Sets the horses armor stack.")
		public void setArmorItemStack(itemStack stack) {
			this.mcEntity.setHorseArmorStack(stack.getMcStack());

		}

		// TODO get armor stack
	}

	@PythonClass
	public class Husk extends Zombie<EntityHusk> {

		public Husk(EntityHusk entity) {
			super(entity);
		}
	}

	@PythonClass
	public class ChestHorse extends GenericHorse<AbstractChestHorse> {

		public ChestHorse(AbstractChestHorse entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Returns True if the Horse has a chest.")
		public boolean isCarryingChest() {
			return this.mcEntity.hasChest();
		}

		@PythonFunction
		@PythonDocString("Sets if the Horse is carrying a chest.")
		public void setCarryingChest(boolean chested) {
			this.mcEntity.setChested(chested);
		}
	}

	@PythonClass
	public class SkeletonHorse extends GenericHorse<EntitySkeletonHorse> {

		public SkeletonHorse(EntitySkeletonHorse entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Returns True if the horse is a Skeleton Trap Horse, False if it isn't.")
		public boolean isSkeletonTrap() {
			return this.mcEntity.isTrap();
		}

		@PythonFunction
		@PythonDocString("Sets the Skeleton Horse to be a trapped horse.")
		public void setSkeletonTrap(boolean isTrapped) {
			this.mcEntity.setTrap(isTrapped);
		}
	}

	public class IronGolem extends Living<EntityIronGolem> {

		public IronGolem(EntityIronGolem entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("If True, the Iron Golem was created by a player.")
		public boolean isPlayerCreated() {
			return this.mcEntity.isPlayerCreated();
		}

		@PythonFunction
		@PythonDocString("Sets if the Iron Golem was created by a player.")
		public void setPlayerCreated(boolean playerCreated) {
			this.mcEntity.setPlayerCreated(playerCreated);
		}
	}

	@PythonClass
	public class Ocelot extends Tameable<EntityOcelot> {

		@PythonFieldGenerated
		public static final int TYPE_WILD = 0;
		@PythonFieldGenerated
		public static final int TYPE_TUXEDO = 1;
		@PythonFieldGenerated
		public static final int TYPE_TABBY = 2;
		@PythonFieldGenerated
		public static final int TYPE_SIAMESE = 3;

		public Ocelot(EntityOcelot entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Returns the Cat's type as an int.")
		public int getCatType() {
			return this.mcEntity.getTameSkin();
		}

		@PythonFunction
		@PythonDocString("Sets the Cat's type.")
		public void setCatType(int catType) {
			this.mcEntity.setTameSkin(catType);
		}
	}

	@PythonClass
	public class Painting extends Hanging<EntityPainting> {

		@PythonFieldGenerated
		public static final String KEBAB = "Kebab";
		@PythonFieldGenerated
		public static final String AZTEC = "Aztec";
		@PythonFieldGenerated
		public static final String ALBAN = "Alban";
		@PythonFieldGenerated
		public static final String AZTEC_2 = "Aztec2";
		@PythonFieldGenerated
		public static final String BOMB = "Bomb";
		@PythonFieldGenerated
		public static final String PLANT = "Plant";
		@PythonFieldGenerated
		public static final String WASTELAND = "Wasteland";
		@PythonFieldGenerated
		public static final String POOL = "Pool";
		@PythonFieldGenerated
		public static final String COURBET = "Courbet";
		@PythonFieldGenerated
		public static final String SEA = "Sea";
		@PythonFieldGenerated
		public static final String SUNSET = "Sunset";
		@PythonFieldGenerated
		public static final String CREEBET = "Creebet";
		@PythonFieldGenerated
		public static final String WANDERER = "Wanderer";
		@PythonFieldGenerated
		public static final String GRAHAM = "Graham";
		@PythonFieldGenerated
		public static final String MATCH = "Match";
		@PythonFieldGenerated
		public static final String BUST = "Bust";
		@PythonFieldGenerated
		public static final String STAGE = "Stage";
		@PythonFieldGenerated
		public static final String VOID = "Void";
		@PythonFieldGenerated
		public static final String SKULL_AND_ROSES = "SkullAndRoses";
		@PythonFieldGenerated
		public static final String WITHER = "Wither";
		@PythonFieldGenerated
		public static final String FIGHTERS = "Fighters";
		@PythonFieldGenerated
		public static final String POINTER = "Pointer";
		@PythonFieldGenerated
		public static final String PIGSCENE = "Pigscene";
		@PythonFieldGenerated
		public static final String BURNING_SKULL = "BurningSkull";
		@PythonFieldGenerated
		public static final String SKELETON = "Skeleton";
		@PythonFieldGenerated
		public static final String DONKEY_KONG = "DonkeyKong";

		public Painting(EntityPainting entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Returns the name of the Painting's art as a string.")
		public String getArtName() {
			return this.mcEntity.art.title;
		}

		@PythonFunction
		@PythonDocString("Sets the art on the Painting to the passed name, or to " + Painting.KEBAB
				+ " if the name is invalid.  Due to a Minecraft Bug, the change will not be visible until after the chunk is reloaded.")
		public void setArt(String artName) {
			for (EntityPainting.EnumArt art : EntityPainting.EnumArt.values()) {
				if (art.title.equals(artName)) {
					this.mcEntity.art = art;
				}
			}
			this.mcEntity.art = EntityPainting.EnumArt.KEBAB;
		}
	}

	@PythonClass
	public class Parrot extends Tameable<EntityParrot> {

		@PythonFieldGenerated
		public static final int COLOR_RED = 0;
		@PythonFieldGenerated
		public static final int COLOR_BLUE = 1;
		@PythonFieldGenerated
		public static final int COLOR_GREEN = 2;
		@PythonFieldGenerated
		public static final int COLOR_CYAN = 3;
		@PythonFieldGenerated
		public static final int COLOR_SILVER = 4;

		public Parrot(EntityParrot entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Returns the Parrots variant as an int.")
		public int getVariant() {
			return this.mcEntity.getVariant();
		}

		@PythonFunction
		@PythonDocString("Sets the Parrot's variant.")
		public void setVariant(int variant) {
			this.mcEntity.setVariant(variant);
		}
	}

	@PythonClass
	public class Pig extends Animal<EntityPig> {

		public Pig(EntityPig entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Returns True if the Pig is saddled, False if it is not.")
		public boolean isSaddled() {
			return this.mcEntity.getSaddled();
		}

		@PythonFunction
		@PythonDocString("Sets if the Pig is wearing a saddle.")
		public void setSaddled(boolean saddled) {
			this.mcEntity.setSaddled(saddled);
		}
	}

	@PythonClass
	public class Player extends LivingBase<EntityPlayerMP> implements IHasEquipment {

		public Player(EntityPlayerMP entity) {
			super(entity);
		}

		// Override the set position and set rotation functions.
		@PythonFunction
		@Override
		public void setPosition(double x, double y, double z) {
			Set<EnumFlags> set = EnumSet.<EnumFlags>noneOf(EnumFlags.class);
			set.add(EnumFlags.X_ROT);
			set.add(EnumFlags.Y_ROT);

			((EntityPlayerMP) this.mcEntity).connection.setPlayerLocation(x, y, z, 0, 0, set);
		}

		@PythonFunction
		@Override
		public void setRotation(float yaw, float pitch) {
			Set<EnumFlags> set = EnumSet.<EnumFlags>noneOf(EnumFlags.class);
			set.add(EnumFlags.X);
			set.add(EnumFlags.Y);
			set.add(EnumFlags.Z);

			((EntityPlayerMP) this.mcEntity).connection.setPlayerLocation(0, 0, 0, yaw, pitch, set);
		}

		@PythonFunction
		@PythonDocString("Returns the index (0-8 inclusive) of the selected hotbar slot.")
		public int getHeldIndex() {
			return this.mcEntity.inventory.currentItem;
		}

		@PythonFunction
		@PythonDocString("Returns the held item of the player as an itemStack, or None if they're not holding anything.  Same as getSlotContents with a slotIndex of 0")
		public itemStack getHeldStack() {
			return itemStack.makeWrapper(this.mcEntity.inventory.getCurrentItem());
		}

		@PythonFunction
		@PythonDocString("Returns a piece or armor, held item or off hand item as an itemStack.")
		@Override
		public itemStack getSlotContents(int slotIndex) {
			Assert.isIndexInBounds(slotIndex, 6);
			return itemStack.makeWrapper(this.mcEntity.getItemStackFromSlot(equipmentSlot.indexToEnum(slotIndex)));
		}

		@PythonFunction
		@PythonDocString("Sets a piece or armor, held item or offhand item.")
		@Override
		public void setSlotContents(int slotIndex, itemStack stack) {
			Assert.isIndexInBounds(slotIndex, 6);
			this.mcEntity.setItemStackToSlot(equipmentSlot.indexToEnum(slotIndex),
					stack == null ? ItemStack.EMPTY : stack.getMcStack());
			this.mcEntity.inventoryContainer.detectAndSendChanges();
		}

		@PythonFunction
		@PythonDocString("Returns an itemStack in the player's main inventory.  slotIndex is 0-35 inclusive.")
		public itemStack getInventoryStack(int slotIndex) {
			Assert.isIndexInBounds(slotIndex, 36);
			return itemStack.makeWrapper(this.mcEntity.inventory.mainInventory.get(slotIndex));
		}

		@PythonFunction
		@PythonDocString("Sets an itemStack in the player's main inventory.  slotIndex is 0-35 inclusive.")
		public void setInventoryStack(int slotIndex, itemStack stack) {
			Assert.isIndexInBounds(slotIndex, 36);
			this.mcEntity.inventory.mainInventory.set(slotIndex, stack == null ? ItemStack.EMPTY : stack.getMcStack());
			this.mcEntity.inventoryContainer.detectAndSendChanges();
		}

		@PythonFunction
		@PythonDocString("Adds an itemStack to the player's main inventory.  Returns the leftover items that couldn't be added, or None if all the items were added.")
		public itemStack addItemStack(itemStack stack) {
			this.mcEntity.inventory.addItemStackToInventory(stack == null ? ItemStack.EMPTY : stack.getMcStack());
			return stack;
		}

		@PythonFunction
		@PythonDocString("Returns True if the passed itemStack is in the player's main inventory.")
		public boolean hasItemStack(itemStack stack) {
			return this.mcEntity.inventory.hasItemStack(stack == null ? ItemStack.EMPTY : stack.getMcStack());
		}

		@PythonFunction
		@PythonDocString("Returns how much food the Player has left as an int.")
		public int getFood() {
			return this.mcEntity.getFoodStats().getFoodLevel();
		}

		@PythonFunction
		@PythonDocString("Sets the Player's food level.")
		public void setFood(int foodLevel) {
			this.mcEntity.getFoodStats().setFoodLevel(foodLevel);
		}

		@PythonFunction
		@PythonDocString("Returns the Player's game mode as an int.")
		public int getGameMode() {
			this.mcEntity.interactionManager.getGameType().getID();
			return 0;
		}

		@PythonFunction
		@PythonDocString("Sets the Player's game mode.  0 = Survival, 1 = Creative, 2 = Adventure, 3 = Spectator.")
		public void setGameMode(int gamemode) {
			if (gamemode < 0 || gamemode > 3) {
				throw Py.ValueError("game mode must be between 0 and 3");
			}
			this.mcEntity.setGameType(GameType.getByID(gamemode));
		}

		@PythonFunction
		@PythonDocString("Closes the container that the Player currently has open, if any.")
		public void closeContainer() {
			this.mcEntity.closeContainer();
		}
	}

	@PythonClass
	public class Rabbit extends Animal<EntityRabbit> {

		@PythonFieldGenerated
		public static final int TYPE_BROWN = 0;
		@PythonFieldGenerated
		public static final int TYPE_WHITE = 1;
		@PythonFieldGenerated
		public static final int TYPE_BLACK = 2;
		@PythonFieldGenerated
		public static final int TYPE_BLACK_WHITE = 3;
		@PythonFieldGenerated
		public static final int TYPE_GOLD = 4;
		@PythonFieldGenerated
		public static final int TYPE_SALT_PEPPER = 5;
		@PythonFieldGenerated
		public static final int TYPE_KILLER = 99;

		public Rabbit(EntityRabbit entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Returns the Rabbit's type as an int.")
		public int getRabbitType() {
			return this.mcEntity.getRabbitType();
		}

		@PythonFunction
		@PythonDocString("Sets the Rabbit's type.")
		public void setRabbitType(int rabbitType) {
			this.mcEntity.setRabbitType(rabbitType);
		}
	}

	@PythonClass
	public class Sheep extends Animal<EntitySheep> {

		public Sheep(EntitySheep entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Returns the color of the Sheep as an int.  See color.py for the color constants.")
		public int getColor() {
			return this.mcEntity.getFleeceColor().getMetadata();
		}

		@PythonFunction
		@PythonDocString("Sets the color of the Sheep.  See color.py for the color constants.")
		public void setColor(int color) {
			this.mcEntity.setFleeceColor(EnumDyeColor.byDyeDamage(color));
		}

		@PythonFunction
		@PythonDocString("Returns True if the Sheep is sheared, False if it has wool.")
		public boolean isSheared() {
			return this.mcEntity.getSheared();
		}

		@PythonFunction
		@PythonDocString("Sets if the Sheep has been sheared.")
		public void setSheared(boolean isSheared) {
			this.mcEntity.setSheared(isSheared);
		}
	}

	@PythonClass
	public class Shulker extends Living<EntityShulker> {

		public Shulker(EntityShulker entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Returns the color ID of the Shulker.  See color.py for the color constants.")
		public int getColor() {
			return this.mcEntity.getColor().getMetadata();
		}

		@PythonFunction
		@PythonDocString("Sets the color of the Shulker from an id.  See color.py for the color constants.")
		public void setColor(int color) {
			this.setTag("Color", color);
		}
	}

	@PythonClass
	public class Slime extends Living<EntitySlime> {

		public Slime(EntitySlime entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Returns the Slime's size as an int.")
		public int getSize() {
			return this.mcEntity.getSlimeSize();
		}

		@PythonFunction
		@PythonDocString("Sets the Slime's size. 0, 1 and 3 are the default sizes.")
		public void setSize(int size) {
			this.setTag("Size", size);
		}
	}

	@PythonClass
	public class Snowman extends Living<EntitySnowman> {

		public Snowman(EntitySnowman entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Returns True if the Snowman is wearing a pumpkin, False if it is not.")
		public boolean isWearingPumpkin() {
			return this.mcEntity.isPumpkinEquipped();
		}

		@PythonFunction
		@PythonDocString("Sets if the Snowman is wearing a pumpkin.")
		public void setWearingPumpkin(boolean pumpkinEquipped) {
			this.mcEntity.setPumpkinEquipped(pumpkinEquipped);
		}
	}

	@PythonClass
	public class Tameable<T extends EntityTameable> extends Animal<T> {

		public Tameable(T entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Returns True if the Entity is sitting, False if it is not.")
		public boolean isSitting() {
			return this.mcEntity.isSitting();
		}

		@PythonFunction
		@PythonDocString("Sets if the Entity is sitting or not.")
		public void setSitting(boolean isSitting) {
			this.mcEntity.setSitting(isSitting);
		}

		@PythonFunction
		@PythonDocString("Sets the Player who owns the Entity.  Must be an instance of entity.Player or net.minecraft.entity.player.EntityPlayer")
		public void setOwner(@PythonTypeExclude Object owner) {
			if (owner instanceof EntityPlayer) {
				this.mcEntity.setTamedBy((EntityPlayer) owner);
			} else if (owner instanceof Player) {
				this.mcEntity.setTamedBy(((Player) owner).mcEntity);
			} else {
				throw Py.ValueError(
						"owner must be an instance of entity.Player or net.minecraft.entity.player.EntityPlayer");
			}
		}
	}

	@PythonClass
	public class Tnt extends Base<EntityTNTPrimed> {

		public Tnt(EntityTNTPrimed entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Returns how many ticks until the Tnt explodes.")
		public int getFuse() {
			return this.mcEntity.getFuse();
		}

		@PythonFunction
		@PythonDocString("Sets how many tick until the Tnt explodes.  Default is 80 ticks or 4 seconds..")
		public void setFuse(int fuse) {
			this.mcEntity.setFuse(fuse);
		}
	}

	@PythonClass
	public class Villager extends Living<EntityVillager> {

		private Field field;

		@PythonFieldGenerated
		public static final int PROFESSION_FARMER_ID = 0;
		@PythonFieldGenerated
		public static final int PROFESSION_LIBRARIAN_ID = 1;
		@PythonFieldGenerated
		public static final int PROFESSION_PRIEST_ID = 2;
		@PythonFieldGenerated
		public static final int PROFESSION_BLACKSMITH_ID = 3;
		@PythonFieldGenerated
		public static final int PROFESSION_BUTCHER_ID = 4;
		@PythonFieldGenerated
		public static final int PROFESSION_NITWIT_ID = 5;

		@PythonFieldGenerated
		public static final int CAREER_FARMER_ID = 1;
		@PythonFieldGenerated
		public static final int CAREER_FISHERMAN_ID = 2;
		@PythonFieldGenerated
		public static final int CAREER_SHEPHERD_ID = 3;
		@PythonFieldGenerated
		public static final int CAREER_FLETCHER_ID = 4;
		@PythonFieldGenerated
		public static final int CAREER_LIBRARIAN_ID = 1;
		@PythonFieldGenerated
		public static final int CAREER_CARTOGRAPHER_ID = 2;
		@PythonFieldGenerated
		public static final int CAREER_CLERIC_ID = 1;
		@PythonFieldGenerated
		public static final int CAREER_ARMORER_ID = 1;
		@PythonFieldGenerated
		public static final int CAREER_WEAPON_SMITH_ID = 2;
		@PythonFieldGenerated
		public static final int CAREER_TOOL_SMITH_ID = 3;
		@PythonFieldGenerated
		public static final int CAREER_BUTCHER_ID = 1;
		@PythonFieldGenerated
		public static final int CAREER_LEATHERWORKER_ID = 2;
		@PythonFieldGenerated
		public static final int CAREER_NITWIT = 1;

		public Villager(EntityVillager entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Returns the ID of the Villager's profession.")
		public int getProfessionId() {
			return this.mcEntity.getProfession();
		}

		@PythonFunction
		@PythonDocString("Sets the Villager's profession..")
		public void setProfessionId(int professionId) {
			this.mcEntity.setProfession(professionId);
		}

		@PythonFunction
		@PythonDocString("Returns the ID of the Villager's career.")
		public int getCareerId() {
			return (int) this.getTag("Career");
		}

		@PythonFunction
		@PythonDocString("Sets the Villager's career.")
		public void setCareerId(int careerId) {
			this.setTag("Career", careerId);
			this.addNewTrade(null, null);
		}

		@PythonFunction
		@PythonDocString("Adds a new trade to the Villager.  Item stacks are represented as tuples like (item_id, [optional]amount, [optional]meta)."
				+ "\n  First Arg: A Tuple of itemStacks that the villager buys, up to 2 long."
				+ "\n  Second Arg: The itemStack that the villager buys."
				+ "\n  Third Arg: (Optional) The number of times the trade can be used.  If not specified or it's -1, a random number between 2 and 12 is used."
				+ "\n  Fourth Arg: (Optional) The numbers of times this trade has been used.")
		public void addNewTrade(Object buy, itemStack sellStack, Object... args) {
			if (this.field == null) {
				this.field = ReflectionHelper.findField(EntityVillager.class,
						new String[] { "buyingList", "field_70963_i" });
			}
			MerchantRecipeList recipeList = null;
			try {
				recipeList = (MerchantRecipeList) this.field.get(this.mcEntity);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}

			if (recipeList == null) {
				recipeList = new MerchantRecipeList();
			}

			// Add recipe:

			// Default values.
			ItemStack buy1;
			ItemStack buy2 = ItemStack.EMPTY;
			ItemStack sell;
			int timesTradeUsed = 0;
			int maxTrades;
			// TODO give XP

			// Parse args:
			if (buy instanceof itemStack) {
				buy1 = ((itemStack) buy).getMcStack();
				buy2 = ItemStack.EMPTY;
			} else if (buy instanceof PyTuple) {
				PyTuple buyTuple = ((PyTuple) buy);
				if (buyTuple.__len__() <= 0 || buyTuple.__len__() > 2) {
					throw Py.ValueError("Buy list may only contain 1 or 2 items");
				}

				buy1 = ((itemStack) buyTuple.get(0)).getMcStack();
				if (buyTuple.size() > 1) {
					buy2 = ((itemStack) buyTuple.get(1)).getMcStack();
				} else {
					buy2 = ItemStack.EMPTY;
				}
			} else {
				throw Py.ValueError("Argument \"buy\" must be an instance of itemStack or a sequence.");
			}

			sell = sellStack.getMcStack();
			if (args.length > 0 && (int) args[0] != -1) {
				maxTrades = (int) args[0];
			} else {
				// Default Minecraft random value.
				maxTrades = this.mcEntity.getRNG().nextInt(11) + 2;
			}
			if (args.length > 1) {
				timesTradeUsed = (int) args[1];
			}

			// Add trade to list.
			recipeList.add(new MerchantRecipe(buy1, buy2, sell, /* NBT "uses" */ timesTradeUsed, maxTrades));

			// Set the field within the villager.
			try {
				this.field.set(this.mcEntity, recipeList);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	@PythonClass
	public class Vindicator extends Living<EntityVindicator> {

		private final String[] NAMES = new String[] { "johnny", "field_190643_b" };

		public Vindicator(EntityVindicator entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Sets the Johnny flag.  Vindicators with the Johnny flag enabled will attack most mobs.")
		public void setJohnny(boolean flag) {
			ReflectionHelper.setPrivateValue(EntityVindicator.class, this.mcEntity, flag, NAMES);
		}

		@PythonFunction
		@PythonDocString("Returns the Johnny flag.")
		public boolean getJohnny() {
			return ReflectionHelper.getPrivateValue(EntityVindicator.class, this.mcEntity, NAMES);
		}
	}

	@PythonClass
	public class Wolf extends Tameable<EntityWolf> {

		public Wolf(EntityWolf entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Returns the Wolf's collar color.  Uses dye meta ordering.")
		public int getCollarColor() {
			return this.mcEntity.getCollarColor().getDyeDamage();
		}

		@PythonFunction
		@PythonDocString("Sets the Wolf's collar color.  Uses dye meta ordering.  Default is 1 for red.")
		public void setCollarColor(int dyeColor) {
			this.mcEntity.setCollarColor(EnumDyeColor.byDyeDamage(dyeColor));
		}

		@PythonFunction
		@PythonDocString("Returns True if the Wolf is angry, False if it is not.")
		public boolean isAngry() {
			return this.mcEntity.isAngry();
		}

		@PythonFunction
		@PythonDocString("Sets if the Wolf is angry.")
		public void setAngry(boolean isAngry) {
			this.mcEntity.setAngry(isAngry);
		}
	}

	@PythonClass
	public class Zombie<T extends EntityZombie> extends Living<T> {

		public Zombie(T entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Returns True if the Entity is a baby, False if it is not.")
		public boolean isBaby() {
			return this.mcEntity.isChild();
		}

		@PythonFunction
		@PythonDocString("Sets if the Entity is a baby.")
		public void setIsBaby(boolean isBaby) {
			this.mcEntity.setChild(isBaby);
		}

		@PythonFunction
		@PythonDocString("Returns True if the Entity can break doors, False if it can't.")
		public boolean getCanBreakDoors() {
			return this.mcEntity.isBreakDoorsTaskSet();
		}

		@PythonFunction
		@PythonDocString("Sets if the Entity can break doors.")
		public void setCanBreakDoors(boolean canBreakDoors) {
			this.mcEntity.setBreakDoorsAItask(canBreakDoors);
		}
	}

	@PythonClass
	public class ZombieVillager extends Zombie<EntityZombieVillager> {

		public ZombieVillager(EntityZombieVillager entity) {
			super(entity);
		}

		@PythonFunction
		@PythonDocString("Sets the Zombie Villager's profession.  See entity.Villager for profession IDs.")
		public String getProfession() {
			return this.mcEntity.getForgeProfession().getRegistryName().toString();
		}

		@PythonFunction
		@PythonDocString("Returns the Zombie Villager's profession.  See entity.Villager for profession IDs.")
		public void setProfession(int profession) {
			this.mcEntity.setProfession(profession);
		}
	}

	// Internal beyond here.

	private interface IHasEquipment {

		@PythonFunction
		@Nullable
		public itemStack getSlotContents(int slotIndex);

		@PythonFunction
		public void setSlotContents(int slotIndex, @Nullable itemStack itemStack);
	}
}