package com.github.supernova.gui;

import com.github.supernova.Supernova;
import com.github.supernova.gui.impl.TextBox;
import com.github.supernova.util.client.SecurityUtil;
import com.github.supernova.util.render.ColourUtil;
import com.github.supernova.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.json.JSONObject;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GuiLogin extends GuiScreen {

    private DynamicTexture viewportTexture;
    private ResourceLocation backgroundTexture;
    private float panoramaTimer = 0.5f;
    private long initTime = System.currentTimeMillis();
    private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[] {new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")};


    private String currentStatus = "Ready To Login";


    private boolean hovered;
    private float lastClicked = 0;

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        renderSkybox(mouseX,mouseX,partialTicks);
        if (lastClicked > 0) {
            lastClicked -= 0.05f * partialTicks;
        }
        ScaledResolution sr = new ScaledResolution(mc);
        float midX = sr.getScaledWidth()/2f;
        float midY = sr.getScaledHeight()/2f;
        Gui.drawRect(midX-90,midY-110,midX+90,midY+110, 0xFF353535);
        RenderUtil.drawRectOutlineWidth(midX-90,midY-108,180,218, 0xFF303030, 2f);
        Gui.drawRect(midX-92,midY-110,midX+92,midY-108, ColourUtil.astolfoColour(0,10000).getRGB());
        RenderUtil.drawRectWidth(midX-80, midY+70, 160, 25, 0xFF303030);

        if (RenderUtil.isHoveredWidth(mouseX,mouseY,midX-80-2, midY+70-1, 160+3, 25+2)) {
            hovered = true;
        } else {
            hovered = false;
        }

        int buttonOutline = hovered ? ColourUtil.astolfoColour(0,10000).getRGB() : 0xFF808080;
        RenderUtil.drawRectOutlineWidth(midX-80, midY+70, 160, 25, buttonOutline, 1f);
        drawCenteredString(mc.fontRendererObj,"Login",midX,midY+78.5f,buttonOutline);
        drawCenteredString(mc.fontRendererObj,currentStatus,midX,midY-45,lastClicked > 0 ? ColourUtil.astolfoColour(0,10000).getRGB() : 0xFF808080);

        float xAdd = mc.blockyFontObj.getStringWidth("Supernova")/2f;

        usernameTextbox.drawTextBox();
        passwordTextbox.drawHiddenTextBox();
        mc.blockyFontObj.drawString("Supernova",midX-xAdd,midY-95,0xFFFFFFFF,true);

        if(Supernova.INSTANCE.isAuthed()) {
            Supernova.INSTANCE.startup();
            Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.usernameTextbox.mouseClicked(mouseX, mouseY, mouseButton);
        this.passwordTextbox.mouseClicked(mouseX, mouseY, mouseButton);
        if (hovered) {
            attemptLogin();
        }
    }

    private String tryAuthentication(final String username, final String password, final String HWID) {
        String secret = SecurityUtil.encode(username.getBytes(StandardCharsets.UTF_8))+
                SecurityUtil.encode(password.getBytes(StandardCharsets.UTF_8))+
                SecurityUtil.encode(HWID.getBytes(StandardCharsets.UTF_8));
        secret = SecurityUtil.hash(secret);
        try {
            System.out.println("Authenticating");
            URL authURL = new URL("http://91.109.117.58/api/v1/authenticate?secret="+secret);
            JSONObject authBody = new JSONObject();
            authBody.put("username", username);
            authBody.put("password", password);
            authBody.put("hwid", HWID);
            HttpURLConnection conn = (HttpURLConnection) authURL.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("client", "supernova");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write(authBody.toString());
            bw.flush();
            bw.close();
            System.out.println(conn.getResponseCode());
            if(conn.getResponseCode() != 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                StringBuilder responseBuilder = new StringBuilder();
                if (br.ready()) {
                    br.lines().forEach(responseBuilder::append);
                }
                JSONObject responseObject = new JSONObject(responseBuilder.toString());
                if (responseObject.has("error")) {
                    currentStatus = responseObject.getString("error");
                    return "";
                }
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder responseBuilder = new StringBuilder();
                if (br.ready()) {
                    br.lines().forEach(responseBuilder::append);
                }
                JSONObject responseObject = new JSONObject(responseBuilder.toString());
                if (responseObject.has("status")) {
                    return responseObject.getString("status");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void attemptLogin() {
        lastClicked = 15;
        new Thread("CLIENT LOGIN") {
            @Override
            public void run() {
                String username = usernameTextbox.getText();
                String password = passwordTextbox.getText();
                // DETAILS CHECK
                if (username.equals("") || password.equals("")) {
                    currentStatus = "Invalid Details";
                    return;
                }
                String passHash = SecurityUtil.hash(password);
                String hwid = SecurityUtil.getHWID(username);
                if(passHash == null) {
                    currentStatus = "Invalid Details";
                    return;
                }
                currentStatus = "Logging in...";
                String authentication = tryAuthentication(username, passHash, hwid);
                if(authentication.equals("success")) {
                    Supernova.INSTANCE.setPlayerInfo(username, 0);
                    currentStatus = "Success";
                    //TODO: Init Client Here
                }
            }
        }.start();

    }
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (passwordTextbox.isFocused()) {
            passwordTextbox.textboxKeyTyped(typedChar, keyCode);
        } else if (usernameTextbox.isFocused()) {
            usernameTextbox.textboxKeyTyped(typedChar, keyCode);
        }
        if (keyCode == 28) {
            if (passwordTextbox.isFocused()) {
                attemptLogin();
            } else if (usernameTextbox.isFocused()) {
                usernameTextbox.setFocused(false);
                passwordTextbox.setFocused(true);
            }
        }
    }

    private TextBox usernameTextbox;
    private TextBox passwordTextbox;
    public void updateScreen() {
        ++this.panoramaTimer;
        passwordTextbox.updateCursorCounter();
        usernameTextbox.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    public void initGui() {

        Keyboard.enableRepeatEvents(true);
        ScaledResolution sr = new ScaledResolution(mc);
        float midX = sr.getScaledWidth()/2f;
        float midY = sr.getScaledHeight()/2f;

        String username = "", password = "";
        boolean focUser = false;
        boolean focPass = false;
        if (usernameTextbox != null && passwordTextbox != null) {
            username = usernameTextbox.getText();
            password = passwordTextbox.getText();
            if (usernameTextbox.isFocused()) focUser = true;
            if (passwordTextbox.isFocused()) focPass = true;
        }
        usernameTextbox = new TextBox(20, mc.fontRendererObj, midX-80, midY-25, 160, 20);
        usernameTextbox.setMaxStringLength(64);
        usernameTextbox.setEmptyString("Username");
        passwordTextbox = new TextBox(20, mc.fontRendererObj, midX-80, midY+5, 160, 20);
        passwordTextbox.setMaxStringLength(64);
        passwordTextbox.setEmptyString("Password");

        if (!username.equals("")) {
            usernameTextbox.setText(username);
        }
        if (!password.equals("")) {
            passwordTextbox.setText(password);
        }

        if (focUser) usernameTextbox.setFocused(true);
        else if (focPass) passwordTextbox.setFocused(true);
        else usernameTextbox.setFocused(true);

        this.viewportTexture = new DynamicTexture(256, 256);
        this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
    }


    private void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        int i = 8;

        for (int j = 0; j < i * i; ++j)
        {
            GlStateManager.pushMatrix();
            float f = ((float)(j % i) / (float)i - 0.5F) / 64.0F;
            float f1 = ((float)(j / i) / (float)i - 0.5F) / 64.0F;
            float f2 = 0.0F;
            GlStateManager.translate(f, f1, f2);
            GlStateManager.rotate(MathHelper.sin(((float)this.panoramaTimer + p_73970_3_) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-((float)this.panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);

            for (int k = 0; k < 6; ++k)
            {
                GlStateManager.pushMatrix();

                if (k == 1)
                {
                    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (k == 2)
                {
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                }

                if (k == 3)
                {
                    GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (k == 4)
                {
                    GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (k == 5)
                {
                    GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                }

                this.mc.getTextureManager().bindTexture(titlePanoramaPaths[k]);
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                int l = 255 / (j + 1);
                float f3 = 0.0F;
                worldrenderer.pos(-1.0D, -1.0D, 1.0D).tex(0.0D, 0.0D).color(255, 255, 255, l).endVertex();
                worldrenderer.pos(1.0D, -1.0D, 1.0D).tex(1.0D, 0.0D).color(255, 255, 255, l).endVertex();
                worldrenderer.pos(1.0D, 1.0D, 1.0D).tex(1.0D, 1.0D).color(255, 255, 255, l).endVertex();
                worldrenderer.pos(-1.0D, 1.0D, 1.0D).tex(0.0D, 1.0D).color(255, 255, 255, l).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
            }

            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
        }

        worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }
    private void rotateAndBlurSkybox(float p_73968_1_)
    {
        this.mc.getTextureManager().bindTexture(this.backgroundTexture);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.colorMask(true, true, true, false);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        GlStateManager.disableAlpha();
        int i = 3;

        for (int j = 0; j < i; ++j)
        {
            float f = 1.0F / (float)(j + 1);
            int k = this.width;
            int l = this.height;
            float f1 = (float)(j - i / 2) / 256.0F;
            worldrenderer.pos((double)k, (double)l, (double)this.zLevel).tex((double)(0.0F + f1), 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos((double)k, 0.0D, (double)this.zLevel).tex((double)(1.0F + f1), 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos(0.0D, 0.0D, (double)this.zLevel).tex((double)(1.0F + f1), 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos(0.0D, (double)l, (double)this.zLevel).tex((double)(0.0F + f1), 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
        }

        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask(true, true, true, true);
    }

    /**
     * Renders the skybox in the main menu
     */
    private void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_)
    {
        this.mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 256, 256);
        this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        float f = this.width > this.height ? 120.0F / (float)this.width : 120.0F / (float)this.height;
        float f1 = (float)this.height * f / 256.0F;
        float f2 = (float)this.width * f / 256.0F;
        int i = this.width;
        int j = this.height;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0D, (double)j, (double)this.zLevel).tex((double)(0.5F - f1), (double)(0.5F + f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos((double)i, (double)j, (double)this.zLevel).tex((double)(0.5F - f1), (double)(0.5F - f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos((double)i, 0.0D, (double)this.zLevel).tex((double)(0.5F + f1), (double)(0.5F - f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos(0.0D, 0.0D, (double)this.zLevel).tex((double)(0.5F + f1), (double)(0.5F + f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        tessellator.draw();
    }
}
