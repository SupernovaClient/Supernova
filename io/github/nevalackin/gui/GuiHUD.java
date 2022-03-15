package io.github.nevalackin.gui;

import io.github.nevalackin.Supernova;
import io.github.nevalackin.modules.Module;
import io.github.nevalackin.modules.ModuleManager;
import io.github.nevalackin.modules.render.HUD;
import io.github.nevalackin.util.render.ColourUtil;
import io.github.nevalackin.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntityEndPortalRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;

public class GuiHUD {

	private HUD HUDModule;
	private Minecraft mc = Minecraft.getMinecraft();

	public GuiHUD(HUD hud) {
		this.HUDModule = hud;
	}

	public void render(float partialTicks) {
		ScaledResolution sr = new ScaledResolution(mc);
		renderWatermark(4, 4, sr);
		renderArrayList(sr.getScaledWidth() - 1, 4, sr);
	}

	public void renderWorld(float partialTicks) {
	}

	private void renderWatermark(float x, float y, ScaledResolution sr) {
		String watermarkString = Supernova.CLIENT_NAME + " | UID 000 | Royalty";
		int width = mc.blockyFontObj.getStringWidth(watermarkString);
		RenderUtil.drawRectWidth(x, y, width + 4, mc.blockyFontObj.FONT_HEIGHT + 3, 0x802D2D2D);
		RenderUtil.drawRectOutlineWidth(x, y, width + 4, mc.blockyFontObj.FONT_HEIGHT + 3,
				RenderUtil.astolfoColour(0, 10000).getRGB(), 0.8f);
		mc.blockyFontObj.drawStringWithShadow(watermarkString, x + 2, y + 2, 0xFFFFFFFF);
	}

	private void renderArrayList(float baseX, float baseY, ScaledResolution sr) {
		ArrayList<Module> enabledModules = ModuleManager.INSTANCE.getEnabledModules();
		enabledModules = enabledModules.stream().filter(Module::isVisible).collect(Collectors.toCollection(ArrayList::new));
		enabledModules = enabledModules.stream().sorted(Comparator.comparingInt((module) -> mc.blockyFontObj.getStringWidth(module.getModuleDisplayName())))
				.collect(Collectors.toCollection(ArrayList::new));

		int spacing = mc.blockyFontObj.FONT_HEIGHT + 1;
		float y = baseY;
		int count = 1;

		for (Module module : enabledModules) {
			int currentColour = ColourUtil.interpolateColorsDynamic(10, count * 20, new Color(0xFFAA77FF), new Color(0xFFAA77FF).darker().darker()).getRGB();
			int width = mc.blockyFontObj.getStringWidth(module.getModuleDisplayName());
			RenderUtil.drawRectWidth(baseX - width - 8, y - 1.3f, width + 5, mc.blockyFontObj.FONT_HEIGHT + 0.8f, 0x60444444);
			RenderUtil.drawRectWidth(baseX - 4, y - 1.3f, 2, mc.blockyFontObj.FONT_HEIGHT + 0.8f, currentColour);
			mc.blockyFontObj.drawStringWithShadow(module.getModuleDisplayName(), baseX - width - 6, y - 1, currentColour);

			y += spacing;
			count++;
		}
	}

	private static final ResourceLocation END_SKY_TEXTURE = new ResourceLocation("textures/environment/end_sky.png");
	private static final ResourceLocation END_PORTAL_TEXTURE = new ResourceLocation("textures/entity/end_portal.png");

	public void renderTileEntityAt(double x, double y, double z)
	{
		Random field_147527_e = new Random();
		float f = (float) mc.thePlayer.posX;
		float f1 = (float) mc.thePlayer.posY;
		float f2 = (float) mc.thePlayer.posZ;
		GlStateManager.disableLighting();
		field_147527_e.setSeed(31100L);
		float f3 = 0.75F;
		for (int i = 0; i < 16; ++i)
		{
			GlStateManager.pushMatrix();
			float f4 = (float)(16 - i);
			float f5 = 0.0625F;
			float f6 = 1.0F / (f4 + 1.0F);

			if (i == 0)
			{
				mc.getTextureManager().bindTexture(END_SKY_TEXTURE);
				f6 = 0.1F;
				f4 = 65.0F;
				f5 = 0.125F;
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(770, 771);
			}

			if (i >= 1)
			{
				mc.getTextureManager().bindTexture(END_PORTAL_TEXTURE);
			}

			if (i == 1)
			{
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(1, 1);
				f5 = 0.5F;
			}

			float f7 = (float)(-(y + (double)f3));
			float f8 = f7 + (float) ActiveRenderInfo.getPosition().yCoord;
			float f9 = f7 + f4 + (float)ActiveRenderInfo.getPosition().yCoord;
			float f10 = f8 / f9;
			f10 = (float)(y + (double)f3) + f10;
			GlStateManager.translate(f, f10, f2);
			GlStateManager.texGen(GlStateManager.TexGen.S, 9217);
			GlStateManager.texGen(GlStateManager.TexGen.T, 9217);
			GlStateManager.texGen(GlStateManager.TexGen.R, 9217);
			GlStateManager.texGen(GlStateManager.TexGen.Q, 9216);
			GlStateManager.func_179105_a(GlStateManager.TexGen.S, 9473, this.func_147525_a(1.0F, 0.0F, 0.0F, 0.0F));
			GlStateManager.func_179105_a(GlStateManager.TexGen.T, 9473, this.func_147525_a(0.0F, 0.0F, 1.0F, 0.0F));
			GlStateManager.func_179105_a(GlStateManager.TexGen.R, 9473, this.func_147525_a(0.0F, 0.0F, 0.0F, 1.0F));
			GlStateManager.func_179105_a(GlStateManager.TexGen.Q, 9474, this.func_147525_a(0.0F, 1.0F, 0.0F, 0.0F));
			GlStateManager.enableTexGenCoord(GlStateManager.TexGen.S);
			GlStateManager.enableTexGenCoord(GlStateManager.TexGen.T);
			GlStateManager.enableTexGenCoord(GlStateManager.TexGen.R);
			GlStateManager.enableTexGenCoord(GlStateManager.TexGen.Q);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5890);
			GlStateManager.pushMatrix();
			GlStateManager.loadIdentity();
			GlStateManager.translate(0.0F, (float)(Minecraft.getSystemTime() % 700000L) / 700000.0F, 0.0F);
			GlStateManager.scale(f5, f5, f5);
			GlStateManager.translate(0.5F, 0.5F, 0.0F);
			GlStateManager.rotate((float)(i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.translate(-0.5F, -0.5F, 0.0F);
			GlStateManager.translate(-f, -f2, -f1);
			f8 = f7 + (float)ActiveRenderInfo.getPosition().yCoord;
			GlStateManager.translate((float)ActiveRenderInfo.getPosition().xCoord * f4 / f8, (float)ActiveRenderInfo.getPosition().zCoord * f4 / f8, -f1);
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
			float f11 = (field_147527_e.nextFloat() * 0.5F + 0.1F) * f6;
			float f12 = (field_147527_e.nextFloat() * 0.5F + 0.4F) * f6;
			float f13 = (field_147527_e.nextFloat() * 0.5F + 0.5F) * f6;

			if (i == 0)
			{
				f11 = f12 = f13 = 1.0F * f6;
			}

			worldrenderer.pos(x, y + (double)f3, z).color(f11, f12, f13, 1.0F).endVertex();
			worldrenderer.pos(x, y + (double)f3, z + 1.0D).color(f11, f12, f13, 1.0F).endVertex();
			worldrenderer.pos(x + 1D, y + (double)f3, z + 1.0D).color(f11, f12, f13, 1.0F).endVertex();
			worldrenderer.pos(x + 1D, y + (double)f3, z).color(f11, f12, f13, 1.0F).endVertex();
			tessellator.draw();
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
			mc.getTextureManager().bindTexture(END_SKY_TEXTURE);
		}

		GlStateManager.disableBlend();
		GlStateManager.disableTexGenCoord(GlStateManager.TexGen.S);
		GlStateManager.disableTexGenCoord(GlStateManager.TexGen.T);
		GlStateManager.disableTexGenCoord(GlStateManager.TexGen.R);
		GlStateManager.disableTexGenCoord(GlStateManager.TexGen.Q);
		GlStateManager.enableLighting();
	}
	FloatBuffer field_147528_b = GLAllocation.createDirectFloatBuffer(16);
	private FloatBuffer func_147525_a(float p_147525_1_, float p_147525_2_, float p_147525_3_, float p_147525_4_)
	{
		this.field_147528_b.clear();
		this.field_147528_b.put(p_147525_1_).put(p_147525_2_).put(p_147525_3_).put(p_147525_4_);
		this.field_147528_b.flip();
		return this.field_147528_b;
	}
}
