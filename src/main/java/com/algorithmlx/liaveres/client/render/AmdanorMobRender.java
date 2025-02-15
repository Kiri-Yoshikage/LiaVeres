package com.algorithmlx.liaveres.client.render;

import com.algorithmlx.liaveres.client.model.AmdanorMobModel;
import com.algorithmlx.liaveres.common.entity.AmdanorMob;
import com.algorithmlx.liaveres.common.setup.Constants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class AmdanorMobRender extends HumanoidMobRenderer<AmdanorMob, AmdanorMobModel<AmdanorMob>> {
    public static final float mobScale = 0.75F;
    private static final ResourceLocation MOB_TEXTURE = new ResourceLocation(Constants.ModId, "textures/entity/amdanor_skeleton.png");

    public AmdanorMobRender(EntityRendererProvider.Context context) {
        this(context, ModelLayers.SKELETON, ModelLayers.SKELETON_INNER_ARMOR, ModelLayers.SKELETON_OUTER_ARMOR);
    }
    public AmdanorMobRender(EntityRendererProvider.Context context, ModelLayerLocation location, ModelLayerLocation location1, ModelLayerLocation location2) {
        super(context, new AmdanorMobModel<>(context.bakeLayer(location)), 0.5f);
        this.addLayer(new HumanoidArmorLayer<>(this, new AmdanorMobModel<>(context.bakeLayer(location1)), new AmdanorMobModel<>(context.bakeLayer(location2))));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(AmdanorMob entity) {
        return MOB_TEXTURE;
    }

    @Override
    protected void scale(AmdanorMob mob, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(mobScale, mobScale, mobScale);
    }
}
