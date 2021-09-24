package net.smb.moderutils;

import java.util.List;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.eq2online.console.Log;
import net.eq2online.macros.compatibility.Reflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class WorldRender extends RenderPlayer {
	public static WorldRender instance = new WorldRender(Minecraft.getMinecraft().getRenderManager());
	
	public WorldRender(RenderManager renderManager) {
		super(renderManager);
	}
	
	    public static void drawEspBlock(double x, double y, double z, float r, float g, float b, float a, float scale) {
	    	double pX = 0;
	        double pY = 0;
	        double pZ = 0;
	    	try {
		        pX = Reflection.getPrivateValue(RenderManager.class, RenderManager.class, "field_78725_b");
		        pY = Reflection.getPrivateValue(RenderManager.class, RenderManager.class, "field_78726_c");
		        pZ = Reflection.getPrivateValue(RenderManager.class, RenderManager.class, "field_78723_d");
	    	} catch(Exception e) {}
	        float tr = (1 - scale) / 2;
	        GL11.glPushMatrix();
	        GL11.glTranslated(-pX, -pY, -pZ);
	        GL11.glAlphaFunc(GL11.GL_GREATER, 0);
	        GL11.glDisable(GL11.GL_TEXTURE_2D);
	        GL11.glDisable(GL11.GL_DEPTH_TEST);
	        GL11.glDisable(GL11.GL_LIGHTING);
	        GL11.glEnable(GL11.GL_BLEND);
	        GL11.glColor4f(r, g, b, a);
	        GL11.glTranslated(x*scale, y*scale, z*scale);
	        GL11.glDepthMask(false);
	        GL11.glPushMatrix();
	        GL11.glTranslatef(tr, tr, tr);
	        GL11.glScalef(scale, scale, scale);
	        GL11.glBegin(GL11.GL_QUADS);
	        GL11.glVertex3f(1, 1, 0);
	        GL11.glVertex3f(0, 1, 0);
	        GL11.glVertex3f(0, 1, 1);
	        GL11.glVertex3f(1, 1, 1);
	        GL11.glVertex3f(1, 0, 1);
	        GL11.glVertex3f(0, 0, 1);
	        GL11.glVertex3f(0, 0, 0);
	        GL11.glVertex3f(1, 0, 0);
	        GL11.glVertex3f(1, 1, 1);
	        GL11.glVertex3f(0, 1, 1);
	        GL11.glVertex3f(0, 0, 1);
	        GL11.glVertex3f(1, 0, 1);
	        GL11.glVertex3f(1, 0, 0);
	        GL11.glVertex3f(0, 0, 0);
	        GL11.glVertex3f(0, 1, 0);
	        GL11.glVertex3f(1, 1, 0);
	        GL11.glVertex3f(0, 1, 1);
	        GL11.glVertex3f(0, 1, 0);
	        GL11.glVertex3f(0, 0, 0);
	        GL11.glVertex3f(0, 0, 1);
	        GL11.glVertex3f(1, 1, 0);
	        GL11.glVertex3f(1, 1, 1);
	        GL11.glVertex3f(1, 0, 1);
	        GL11.glVertex3f(1, 0, 0);
	        GL11.glEnd();
	        GL11.glPopMatrix();
	        GL11.glEnable(GL11.GL_DEPTH_TEST);
	        GL11.glEnable(GL11.GL_TEXTURE_2D);
	        GL11.glEnable(GL11.GL_LIGHTING);
	        GL11.glDisable(GL11.GL_BLEND);
	        GL11.glDepthMask(true);
	        GL11.glPopMatrix();
	    }
	    
	    public static void drawOutlinedEspBlock(double x, double y, double z, float r, float g, float b, float a, float scale) {
	    	double pX = 0;
	        double pY = 0;
	        double pZ = 0;
	    	try {
		        pX = Reflection.getPrivateValue(RenderManager.class, RenderManager.class, "field_78725_b");
		        pY = Reflection.getPrivateValue(RenderManager.class, RenderManager.class, "field_78726_c");
		        pZ = Reflection.getPrivateValue(RenderManager.class, RenderManager.class, "field_78723_d");
	    	} catch(Exception e) {}
	        float tr = (1 - scale) / 2;
	        GL11.glPushMatrix();
	        GL11.glTranslated(-pX, -pY, -pZ);
	        GL11.glAlphaFunc(GL11.GL_GREATER, 0);
	        GL11.glDisable(GL11.GL_TEXTURE_2D);
	        GL11.glDisable(GL11.GL_DEPTH_TEST);
	        GL11.glDisable(GL11.GL_LIGHTING);
	        GL11.glEnable(GL11.GL_BLEND);
	        GL11.glColor4f(r, g, b, a);
	        GL11.glTranslated(x*scale, y*scale, z*scale);
	        GL11.glDepthMask(false);
	        GL11.glPushMatrix();
	        GL11.glTranslatef(tr, tr, tr);
	        GL11.glScalef(scale, scale, scale);
	        GL11.glBegin(GL11.GL_LINES);
	        GL11.glVertex3f(0, 0, 0);
	        GL11.glVertex3f(1, 0, 0);
	        GL11.glVertex3f(0, 0, 0);
	        GL11.glVertex3f(0, 1, 0);
	        GL11.glVertex3f(0, 0, 0);
	        GL11.glVertex3f(0, 0, 1);
	        GL11.glVertex3f(0, 1, 1);
	        GL11.glVertex3f(1, 1, 1);
	        GL11.glVertex3f(0, 1, 1);
	        GL11.glVertex3f(0, 0, 1);
	        GL11.glVertex3f(0, 1, 1);
	        GL11.glVertex3f(0, 1, 0);
	        GL11.glVertex3f(1, 0, 1);
	        GL11.glVertex3f(0, 0, 1);
	        GL11.glVertex3f(1, 0, 1);
	        GL11.glVertex3f(1, 1, 1);
	        GL11.glVertex3f(1, 0, 1);
	        GL11.glVertex3f(1, 0, 0);
	        GL11.glVertex3f(1, 1, 0);
	        GL11.glVertex3f(0, 1, 0);
	        GL11.glVertex3f(1, 1, 0);
	        GL11.glVertex3f(1, 0, 0);
	        GL11.glVertex3f(1, 1, 0);
	        GL11.glVertex3f(1, 1, 1);
	        GL11.glEnd();
	        GL11.glPopMatrix();
	        GL11.glEnable(GL11.GL_DEPTH_TEST);
	        GL11.glEnable(GL11.GL_TEXTURE_2D);
	        GL11.glEnable(GL11.GL_LIGHTING);
	        GL11.glDisable(GL11.GL_BLEND);
	        GL11.glDepthMask(true);
	        GL11.glPopMatrix();
	    }
	    
	    public static void renderPlayerText(EntityLivingBase entity, float partialTicks, String text) {
	    	try {
		    	RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
		    	FontRenderer var14 = Minecraft.getMinecraft().fontRenderer;
		    	float var9 = 3.0F;
		    	var9 /= 45.0F;
		    	var9 = (float)(var9 * 0.3D);
		    	if(var14 != null && !entity.isSneaking()) {
					double posX = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks) - renderManager.viewerPosX;
			    	double posY = (entity.lastTickPosY+2.5f + (entity.posY  - entity.lastTickPosY) * partialTicks) - renderManager.viewerPosY;
			    	double posZ = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks) - renderManager.viewerPosZ;
		    		
		            GL11.glPushMatrix();
		            GL11.glTranslated(posX, posY, posZ);
		            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		            GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		            GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		            GL11.glScalef(-var9, -var9, var9);
		            GL11.glDisable(GL11.GL_LIGHTING);
		            GL11.glDepthMask(false);
		            GL11.glDisable(2929);
		            GL11.glEnable(GL11.GL_BLEND);
		            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		            Tessellator var15 = Tessellator.getInstance();
		            GL11.glDisable(GL11.GL_TEXTURE_2D);
		            int var16 = var14.getStringWidth(text) / 2;
		            BufferBuilder vertexbuffer = var15.getBuffer();
		            vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		            vertexbuffer.pos((double)(-var16 - 1), (double)(-1), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
		            vertexbuffer.pos((double)(-var16 - 1), (double)(8), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
		            vertexbuffer.pos((double)(var16 + 1), (double)(8), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
		            vertexbuffer.pos((double)(var16 + 1), (double)(-1), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
		            var15.draw();
		            
		            GL11.glEnable(GL11.GL_TEXTURE_2D);
		            var14.drawString(text, -var14.getStringWidth(text) / 2, 0, -1);
		            GL11.glEnable(2929);
		            GL11.glDepthMask(true);
		            GL11.glEnable(GL11.GL_LIGHTING);
		            GL11.glDisable(GL11.GL_BLEND);
		            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		            GL11.glPopMatrix();
		    	}
	    	} catch(Exception e) {}
	    }
	    
		private static void renderOne() {
			GL11.glPushAttrib(1048575);
			GL11.glDisable(3008);
			GL11.glDisable(3553);
			GL11.glDisable(2896);
			GL11.glEnable(3042);
			GL11.glBlendFunc(770, 771);
			GL11.glLineWidth(0.2f);
			GL11.glEnable(2848);
			GL11.glHint(3154, 4354);
			GL11.glEnable(2960);
			GL11.glClear(1024);
			GL11.glClearStencil(15);
			GL11.glStencilFunc(512, 1, 15);
			GL11.glStencilOp(7681, 7681, 7681);
			GL11.glLineWidth(4.0f);
			GL11.glStencilOp(7681, 7681, 7681);
			GL11.glPolygonMode(1032, 6913);
		}
		
		public static void checkSetupFBO() {
			Framebuffer fbo = Minecraft.getMinecraft().getFramebuffer();
			if (fbo != null && fbo.depthBuffer > -1) {
				setupFBO(fbo);
				fbo.depthBuffer = -1;
			}
		}

		public static void setupFBO(Framebuffer fbo) {
			EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
			int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
			EXTFramebufferObject.glBindRenderbufferEXT(36161, stencil_depth_buffer_ID);
			EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, Minecraft.getMinecraft().displayWidth,
					Minecraft.getMinecraft().displayHeight);
			EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencil_depth_buffer_ID);
			EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencil_depth_buffer_ID);
		}

		private static void renderTwo() {
			GL11.glStencilFunc(512, 0, 15);
			GL11.glStencilOp(7681, 7681, 7681);
			GL11.glPolygonMode(1032, 6914);
		}

		private static void renderThree() {
			GL11.glStencilFunc(514, 1, 15);
			GL11.glStencilOp(7680, 7680, 7680);
			GL11.glPolygonMode(1032, 6913);
		}

		private static void renderFour() {
			GL11.glEnable(10754);
			GL11.glPolygonOffset(-1.0f, 10000.0f);
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
		}

		private static void renderFive() {
			GL11.glPolygonOffset(1.0f, 2000000.0f);
			GL11.glDisable(10754);
			GL11.glDisable(2960);
			GL11.glDisable(2848);
			GL11.glHint(3154, 4352);
			GL11.glDisable(3042);
			GL11.glEnable(2896);
			GL11.glEnable(3553);
			GL11.glEnable(3008);
			GL11.glPopAttrib();
		}

		public void renderGlow(AbstractClientPlayer entity, float partialTicks, Color col) {
			if(Minecraft.getMinecraft().player == entity) return;
			
			this.mainModel.isChild = entity.isChild();
			this.mainModel.isRiding = entity.isRiding();
			this.mainModel.swingProgress = this.getSwingProgress(entity, partialTicks);
			this.getMainModel().isSneak = entity.isSneaking();
			if(!entity.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) this.getMainModel().rightArmPose = ModelBiped.ArmPose.ITEM;
			else this.getMainModel().rightArmPose = ModelBiped.ArmPose.EMPTY;
			
			if(!entity.getHeldItem(EnumHand.OFF_HAND).isEmpty()) this.getMainModel().leftArmPose = ModelBiped.ArmPose.ITEM;
			else this.getMainModel().leftArmPose = ModelBiped.ArmPose.EMPTY;
			
			this.getMainModel().bipedHead.isHidden = true;
			this.getMainModel().bipedBody.isHidden = true;
			this.getMainModel().bipedLeftArm.isHidden = true;
			this.getMainModel().bipedLeftLeg.isHidden = true;
			this.getMainModel().bipedRightArm.isHidden = true;
			this.getMainModel().bipedRightLeg.isHidden = true;
			
			float f2 = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
			float f3 = this.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
			float f4;

			if (entity.isRiding() && entity.getRidingEntity() instanceof EntityLivingBase) {
				EntityLivingBase entitylivingbase1 = (EntityLivingBase) entity.getRidingEntity();
				f2 = this.interpolateRotation(entitylivingbase1.prevRenderYawOffset, entitylivingbase1.renderYawOffset,
						partialTicks);
				f4 = MathHelper.wrapDegrees(f3 - f2);

				if (f4 < -85.0F) {
					f4 = -85.0F;
				}

				if (f4 >= 85.0F) {
					f4 = 85.0F;
				}

				f2 = f3 - f4;

				if (f4 * f4 > 2500.0F) {
					f2 += f4 * 0.2F;
				}
			}
			
			float f13 = entity.prevRotationPitch
					+ (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
			float f7 = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);
			float f6 = entity.prevLimbSwingAmount
					+ (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
			float f5 = 0.0585F;
			f4 = this.handleRotationFloat(entity, partialTicks);
			
			double posX = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks) - this.renderManager.viewerPosX;
	    	double posY = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks) - this.renderManager.viewerPosY;
	    	double posZ = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks) - this.renderManager.viewerPosZ;
	    	
	    	GL11.glPushMatrix();
			this.renderLivingAt(entity, posX, posY, posZ);
			this.applyRotations(entity, f4, f2, partialTicks);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glScalef(-1.0F, -1.0F, 1.0F);
			if(Minecraft.getMinecraft().player == entity) GL11.glTranslatef(0.0F, 0.22f, 0.0F);
			else { 
				if(entity.isSneaking()) GL11.glTranslatef(0.0F, -22.0F * f5 - 0.0078125F, 0.0F);
				else GL11.glTranslatef(0.0F, -24.0F * f5 - 0.0078125F, 0.0F);
			}
			
			checkSetupFBO();
			GL11.glColor4f(col.colorR, col.colorG, col.colorB, col.colorA);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, 771);
			renderOne();
			GL11.glLineWidth(4.0f);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, 771);
			GL11.glEnable(3042);
			GL11.glEnable(2848);
			GL11.glHint(3154, 4354);
			GL11.glEnable(3024);
			//this.renderModel2(entity, f7, f6, f4, f3 - f2, f13, f5);
			//renderTwo();
			//this.renderModel2(entity, f7, f6, f4, f3 - f2, f13, f5);
			renderThree();
			this.renderModel2(entity, f7, f6, f4, f3 - f2, f13, f5);
//			renderFour();
//			this.renderModel(entity, f7, f6, f4, f3 - f2, f13, f5);
			renderFive();
			GL11.glDisable(3024);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
		}
		
		protected float interpolateRotation(float p_77034_1_, float p_77034_2_, float p_77034_3_) {
			float f3;

			for (f3 = p_77034_2_ - p_77034_1_; f3 < -180.0F; f3 += 360.0F) {
				;
			}

			while (f3 >= 180.0F) {
				f3 -= 360.0F;
			}

			return p_77034_1_ + p_77034_3_ * f3;
		}
		
		protected void renderModel2(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor)
	    {
	        boolean flag = !entitylivingbaseIn.isInvisible() || this.renderOutlines;
	        boolean flag1 = !flag && !entitylivingbaseIn.isInvisibleToPlayer(Minecraft.getMinecraft().player);

	        if (flag || flag1)
	        {
	            if (!this.bindEntityTexture((AbstractClientPlayer) entitylivingbaseIn))
	            {
	                return;
	            }

	            if (flag1)
	            {
	                GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
	            }

	            this.mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);

	            if (flag1)
	            {
	                GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
	            }
	        }
	    }
}
