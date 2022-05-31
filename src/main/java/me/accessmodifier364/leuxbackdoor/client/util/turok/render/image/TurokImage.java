/*
 * Decompiled with CFR 0.151.
 *
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  net.minecraft.util.ResourceLocation
 */
package me.accessmodifier364.leuxbackdoor.client.util.turok.render.image;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TurokImage {
    private final String path;
    private BufferedImage bufferedImage;
    private final ResourceLocation resourceLocation;
    private final DynamicTexture dynamicTexture;

    public TurokImage(String path) {
        this.path = path;
        try {
            this.bufferedImage = ImageIO.read(TurokImage.class.getResourceAsStream(this.path));
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        this.dynamicTexture = new DynamicTexture(this.bufferedImage);
        this.resourceLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("turok/images", this.dynamicTexture);
    }

    public int getWidth() {
        return this.bufferedImage.getWidth();
    }

    public int getHeight() {
        return this.bufferedImage.getHeight();
    }

    public String getPath() {
        return this.path;
    }

    public BufferedImage getBufferedImage() {
        return this.bufferedImage;
    }

    public ResourceLocation getResourceLocation() {
        return this.resourceLocation;
    }

    public DynamicTexture getDynamicTexture() {
        return this.dynamicTexture;
    }
}

