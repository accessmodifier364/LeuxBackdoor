package me.accessmodifier364.leuxbackdoor.client.manager;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventRender;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.modules.client.*;
import me.accessmodifier364.leuxbackdoor.client.modules.combat.*;
import me.accessmodifier364.leuxbackdoor.client.modules.exploit.*;
import me.accessmodifier364.leuxbackdoor.client.modules.hidden.EntityDesync;
import me.accessmodifier364.leuxbackdoor.client.modules.misc.*;
import me.accessmodifier364.leuxbackdoor.client.modules.movement.*;
import me.accessmodifier364.leuxbackdoor.client.modules.player.*;
import me.accessmodifier364.leuxbackdoor.client.modules.render.*;
import me.accessmodifier364.leuxbackdoor.client.util.turok.draw.RenderHelp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Comparator;

public class ModuleManager {

	public static ArrayList<Module> array_hacks = new ArrayList<>();

	public static Minecraft mc = Minecraft.getMinecraft();

	public ModuleManager() {

		add_hack(new GuiModule());

		// UI.
		add_hack(new Colors());
		add_hack(new ClickGUI());
		add_hack(new ClickHUD());
		add_hack(new Particles());

		// Chat.
		add_hack(new Announcer());
		add_hack(new AutoRacist());
		add_hack(new ChatColors());
		add_hack(new ChatSuffix());
		add_hack(new ClearChat());
		add_hack(new FastSuicide());
		add_hack(new Notifications());
		add_hack(new AutoGG());
		add_hack(new GlobalLocation());
		add_hack(new EntitySearch());
		add_hack(new StashFinder());
		add_hack(new AutoBuilder());
		add_hack(new AntiSpam());
		add_hack(new Spammer());
		add_hack(new ChatEncryption());
		add_hack(new ReverseStep());

		// Combat.
		add_hack(new AnvilAura());
		add_hack(new Auto32k());
		add_hack(new AutoArmor());
		add_hack(new AutoCrystal());
		add_hack(new Elevator());
		add_hack(new KillAura());
		add_hack(new Surround());
		add_hack(new HoleFill());
		add_hack(new AutoTrap());
		add_hack(new AntiCityBoss());
		add_hack(new SelfTrap());
		add_hack(new Webfill());
		add_hack(new AutoWeb());
		add_hack(new BedAura());
		add_hack(new Offhand());
		add_hack(new AutoTotem());
		add_hack(new AutoObiBreaker());

		add_hack(new Criticals());
		add_hack(new TotemPopCounter());
		add_hack(new Quiver());
		add_hack(new CevBreaker());
		add_hack(new PistonAura());
		add_hack(new BowAim());
		add_hack(new WebAura());

		// Exploit.
		add_hack(new XCarry());
		add_hack(new NoSwing());
		add_hack(new BurrowBreaker());
		add_hack(new PortalGodMode());
		add_hack(new PacketMine());
		add_hack(new BlockAura());
		add_hack(new NoEntityTrace());
		add_hack(new VanillaPayload());
		add_hack(new PlayerFinder());
		add_hack(new InstantBreak());
		add_hack(new MultiTask());
		add_hack(new NoForceLook());
		add_hack(new AutoSalDupe());
		add_hack(new AutoCraftingDupe());
		add_hack(new PingSpoof());
		add_hack(new EntityRide());
		add_hack(new InstantBurrow());
		add_hack(new GodModule());
		add_hack(new EntityDesync());
		add_hack(new MoonJump());
		add_hack(new AutoMount());
		add_hack(new AutoFrameDupe());
		add_hack(new CoordExploits());
		add_hack(new Timer());

		// Movement.
		add_hack(new Speed());
		add_hack(new Step());
		add_hack(new Sprint());
		add_hack(new Freecam());
		add_hack(new Anchor());
		add_hack(new NoSlow());
		add_hack(new InventoryMove());
		add_hack(new Velocity());
		add_hack(new ElytraFly());
		add_hack(new AntiWeb());
		add_hack(new Phase());
		add_hack(new Yaw());
		add_hack(new AutoWalk());
		add_hack(new NoFall());
		add_hack(new Scaffold());
		add_hack(new Jesus());
		add_hack(new LongJump());
		add_hack(new BoatFly());
		add_hack(new CreativeFly());
		add_hack(new PacketFly());

		// Render.
		add_hack(new BlockHighlight());
		add_hack(new HoleESP());
		add_hack(new ShulkerPreview());
		add_hack(new ViewModel());
		add_hack(new VoidESP());
		add_hack(new NameTags());
		add_hack(new FuckedDetector());
		add_hack(new StorageESP());
		add_hack(new BreakHighlight());
		add_hack(new ThrowTrails());
		add_hack(new Tracers());
		add_hack(new SkyColour());
		add_hack(new HandColor());
		add_hack(new NoRender());
		add_hack(new ESP());
		add_hack(new Trajectories());
		add_hack(new Capes());
		add_hack(new FastSwim());
		add_hack(new CityEsp());
		add_hack(new Brightness());
		add_hack(new Settings());
		add_hack(new BurrowESP());
		add_hack(new SmallShield());
		add_hack(new CrystalModifier());
		add_hack(new AntiHunger());

		// Misc.
		add_hack(new MiddleClickGang());
		add_hack(new AutoReplenish());
		add_hack(new FastUse());
		add_hack(new AutoRespawn());
		add_hack(new FakeCreative());
		add_hack(new FakePlayer());
		add_hack(new RPC());
		add_hack(new EffectsSide());
		add_hack(new AntiAFK());
		add_hack(new AutoWither());
		add_hack(new MiddleClickPearl());
		add_hack(new MiddleClickXP());

		array_hacks.sort(Comparator.comparing(Module::get_name));
	}

	public void add_hack(Module module) {
		array_hacks.add(module);
	}

	public ArrayList<Module> get_array_hacks() {
		return array_hacks;
	}

	public ArrayList<Module> get_array_active_hacks() {
		ArrayList<Module> actived_modules = new ArrayList<>();

		for (Module modules : get_array_hacks()) {
			if (modules.is_active()) {
				actived_modules.add(modules);
			}
		}

		return actived_modules;
	}

	public Vec3d process(Entity entity, double x, double y, double z) {
		return new Vec3d(
			(entity.posX - entity.lastTickPosX) * x,
			(entity.posY - entity.lastTickPosY) * y,
			(entity.posZ - entity.lastTickPosZ) * z);
	}

	public Vec3d get_interpolated_pos(Entity entity, double ticks) {
		return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(process(entity, ticks, ticks, ticks)); // x, y, z.
	}

	public void render(RenderWorldLastEvent event) {
		mc.profiler.startSection("leux");
		mc.profiler.startSection("setup");

		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		GlStateManager.disableDepth();

		GlStateManager.glLineWidth(1f);

		Vec3d pos = get_interpolated_pos(mc.player, event.getPartialTicks());

		EventRender event_render = new EventRender(RenderHelp.INSTANCE, pos);

		event_render.reset_translation();

		mc.profiler.endSection();

		for (Module modules : get_array_hacks()) {
			if (modules.is_active()) {
				mc.profiler.startSection(modules.get_tag());

				modules.render(event_render);

				mc.profiler.endSection();
			}
		}

		mc.profiler.startSection("release");

		GlStateManager.glLineWidth(1f);

		GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
		GlStateManager.enableDepth();
		GlStateManager.enableCull();

		RenderHelp.release_gl();

		mc.profiler.endSection();
		mc.profiler.endSection();
	}

	public void update() {
		for (Module modules : get_array_hacks()) {
			if (modules.is_active()) {
				modules.update();
			}
		}
	}

	public void onTick() {
		for (Module modules : get_array_hacks()) {
			if (modules.is_active()) {
				modules.onTick();
			}
		}
	}

	public void render() {
		for (Module modules : get_array_hacks()) {
			if (modules.is_active()) {
				modules.render();
			}
		}
	}

	public void bind(int event_key) {
		if (event_key == 0) {
			return;
		}

		for (Module modules : get_array_hacks()) {
			if (modules.get_bind(0) == event_key) {
				modules.toggle();
			}
		}
	}

	public Module get_module_with_tag(String tag) {
		Module module_requested = null;

		for (Module module : get_array_hacks()) {
			if (module.get_tag().equalsIgnoreCase(tag)) {
				module_requested = module;
			}
		}

		return module_requested;
	}

	public ArrayList<Module> get_modules_with_category(Category category) {
		ArrayList<Module> module_requesteds = new ArrayList<>();

		for (Module modules : get_array_hacks()) {
			if (modules.get_category().equals(category)) {
				module_requesteds.add(modules);
			}
		}

		return module_requesteds;
	}

}
