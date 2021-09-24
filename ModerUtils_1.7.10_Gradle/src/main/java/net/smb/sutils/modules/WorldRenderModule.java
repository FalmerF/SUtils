package net.smb.sutils.modules;

import java.nio.FloatBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;

import net.eq2online.console.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.smb.sutils.utils.Color;

public class WorldRenderModule extends RenderPlayer {
		public static WorldRenderModule instance = new WorldRenderModule();
	
	    public static void drawEspBlock(double x, double y, double z, float r, float g, float b, float a, float scale) {
	        double pX = RenderManager.renderPosX;
	        double pY = RenderManager.renderPosY;
	        double pZ = RenderManager.renderPosZ;
	        float tr = (1 - scale) / 2;
	        GL11.glPushMatrix();
	        GL11.glTranslated(-pX, -pY, -pZ);
	        GL11.glAlphaFunc(GL11.GL_GREATER, 0);
	        GL11.glDisable(GL11.GL_TEXTURE_2D);
	        GL11.glDisable(GL11.GL_DEPTH_TEST);
	        GL11.glDisable(GL11.GL_LIGHTING);
	        GL11.glEnable(GL11.GL_BLEND);
	        GL11.glColor4f(r, g, b, a);
	        GL11.glTranslated(x, y, z);
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
	        double pX = RenderManager.renderPosX;
	        double pY = RenderManager.renderPosY;
	        double pZ = RenderManager.renderPosZ;
	        float tr = (1 - scale) / 2;
	        GL11.glPushMatrix();
	        GL11.glTranslated(-pX, -pY, -pZ);
	        GL11.glAlphaFunc(GL11.GL_GREATER, 0);
	        GL11.glDisable(GL11.GL_TEXTURE_2D);
	        GL11.glDisable(GL11.GL_DEPTH_TEST);
	        GL11.glDisable(GL11.GL_LIGHTING);
	        GL11.glEnable(GL11.GL_BLEND);
	        GL11.glColor4f(r, g, b, a);
	        GL11.glTranslated(x, y, z);
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
	    
	    public static void renderText(String text, float x, float y, float z, float distance) {
	    	try {
		    	RenderManager renderManager = RenderManager.instance;
		    	FontRenderer var14 = Minecraft.getMinecraft().fontRenderer;
		    	float var9 = distance;
		    	var9 /= 45.0F;
		    	var9 = (float)(var9 * 0.3D);
		    	if(var14 != null) {
		            GL11.glPushMatrix();
		            GL11.glEnable(10754);
					GL11.glPolygonOffset(1.0f, -2000000.0f);
		            GL11.glTranslatef(x, y, z);
		            GL11.glNormal3f(1.0F, 1.0F, 1.0F);
		            GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		            GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		            GL11.glScalef(-var9, -var9, var9);
		            GL11.glDisable(GL11.GL_LIGHTING);
		            GL11.glDepthMask(false);
		            GL11.glDisable(2929);
		            GL11.glEnable(GL11.GL_BLEND);
		            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		            Tessellator var15 = Tessellator.instance;
		            GL11.glDisable(GL11.GL_TEXTURE_2D);
		            var15.startDrawingQuads();
		            int var16 = var14.getStringWidth(text) / 2;
		            var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.5F);
		            var15.addVertex((double)(-var16 - 3), -1.0D, 0.0D);
		            var15.addVertex((double)(-var16 - 3), 8.0D, 0.0D);
		            var15.addVertex((double)(var16 + 3), 8.0D, 0.0D);
		            var15.addVertex((double)(var16 + 3), -1.0D, 0.0D);
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
	    
	    public static void renderPlayerText(EntityLivingBase entity, float partialTicks, String text) {
	    	try {
		    	RenderManager renderManager = RenderManager.instance;
		    	FontRenderer var14 = Minecraft.getMinecraft().fontRenderer;
		    	float var9 = 3.0F;
		    	var9 /= 45.0F;
		    	var9 = (float)(var9 * 0.3D);
		    	if(var14 != null && !entity.isSneaking()) {
					double posX = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks) - RenderManager.instance.viewerPosX;
			    	double posY = (entity.lastTickPosY+2.5f + (entity.posY  - entity.lastTickPosY) * partialTicks) - RenderManager.instance.viewerPosY;
			    	double posZ = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks) - RenderManager.instance.viewerPosZ;
		    		
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
		            Tessellator var15 = Tessellator.instance;
		            GL11.glDisable(GL11.GL_TEXTURE_2D);
		            var15.startDrawingQuads();
		            int var16 = var14.getStringWidth(text) / 2;
		            var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
		            var15.addVertex((double)(-var16 - 3), -1.0D, 0.0D);
		            var15.addVertex((double)(-var16 - 3), 8.0D, 0.0D);
		            var15.addVertex((double)(var16 + 3), 8.0D, 0.0D);
		            var15.addVertex((double)(var16 + 3), -1.0D, 0.0D);
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
			GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glLineWidth(0.2f);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
			GL11.glEnable(GL11.GL_STENCIL_TEST);
			GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
			GL11.glClearStencil(15);
			GL11.glStencilFunc(512, 1, 15);
			GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE);
			GL11.glLineWidth(4.0f);
			GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE);
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
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
			EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencil_depth_buffer_ID);
			EXTFramebufferObject.glRenderbufferStorageEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, 34041, Minecraft.getMinecraft().displayWidth,
					Minecraft.getMinecraft().displayHeight);
			EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencil_depth_buffer_ID);
			EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencil_depth_buffer_ID);
		}

		private static void renderTwo() {
			GL11.glStencilFunc(512, 0, 15);
			GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE);
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		}

		private static void renderThree() {
			GL11.glStencilFunc(514, 1, 15);
			GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
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

		public void renderGlow(EntityLivingBase entity, float partialTicks, Color col) {
			this.mainModel.isChild = entity.isChild();
			this.mainModel.isRiding = entity.isRiding();
			this.mainModel.onGround = this.renderSwingProgress(entity, partialTicks);
			this.modelBipedMain.isSneak = entity.isSneaking();
			if(entity.getHeldItem() != null) this.modelBipedMain.heldItemRight = 1;
			else this.modelBipedMain.heldItemRight = 0;
			
			float f2 = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
			float f3 = this.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
			float f4;

			if (entity.isRiding() && entity.ridingEntity instanceof EntityLivingBase) {
				EntityLivingBase entitylivingbase1 = (EntityLivingBase) entity.ridingEntity;
				f2 = this.interpolateRotation(entitylivingbase1.prevRenderYawOffset, entitylivingbase1.renderYawOffset,
						partialTicks);
				f4 = MathHelper.wrapAngleTo180_float(f3 - f2);

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
			
			double posX = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks) - RenderManager.instance.viewerPosX;
	    	double posY = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks) - RenderManager.instance.viewerPosY;
	    	double posZ = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks) - RenderManager.instance.viewerPosZ;
	    	
	    	GL11.glPushMatrix();
			this.renderLivingAt(entity, posX, posY, posZ);
			this.rotateCorpse(entity, f4, f2, partialTicks);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glScalef(-1.0F, -1.0F, 1.0F);
			if(Minecraft.getMinecraft().thePlayer == entity) 
				GL11.glTranslatef(0.0F, 0.22f, 0.0F);
			else { 
				if(entity.isSneaking()) GL11.glTranslatef(0.0F, -22.0F * f5 - 0.0078125F, 0.0F);
				else GL11.glTranslatef(0.0F, -24.0F * f5 - 0.0078125F, 0.0F);
			}
			
			//enableOutlineMode(col);
			//this.renderModel(entity, f7, f6, f4, f3 - f2, f13, f5);
			//disableOutlineMode();
			
			//checkSetupFBO();
			GL11.glColor4f(col.colorR, col.colorG, col.colorB, col.colorA);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, 771);
			renderOne();
			GL11.glLineWidth(4.0f);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, 771);
			GL11.glEnable(3042);
			GL11.glEnable(2848);
			GL11.glHint(3154, 4354);
			GL11.glEnable(3024);
			this.renderModel(entity, f7, f6, f4, f3 - f2, f13, f5);
//			renderTwo();
//			this.renderModel(entity, f7, f6, f4, f3 - f2, f13, f5);
//			renderThree();
//			this.renderModel(entity, f7, f6, f4, f3 - f2, f13, f5);
//			renderFive();
//			GL11.glDisable(3024);
			GL11.glPopMatrix();
		}
		
		private static Framebuffer framebuffer;
		
		public void drawQuad() {
		    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		    GL11.glBegin(GL11.GL_QUADS);
		    GL11.glVertex2i(0, 1);
		    GL11.glVertex2i(1, 0);
		    GL11.glVertex2i(1, 1);
		    GL11.glVertex2i(0, 0);
	        GL11.glEnd();
		}
		
		public static void enableOutlineMode(Color col)
	    {
			FloatBuffer BUF_FLOAT_4 = BufferUtils.createFloatBuffer(4);
	        BUF_FLOAT_4.put(0, col.colorR);
	        BUF_FLOAT_4.put(1, col.colorG);
	        BUF_FLOAT_4.put(2, col.colorB);
	        BUF_FLOAT_4.put(3, col.colorA);
	        GL11.glTexEnv(8960, 8705, BUF_FLOAT_4);
	        GL11.glTexEnvi(8960, 8704, 34160);
	        GL11.glTexEnvi(8960, 34161, 7681);
	        GL11.glTexEnvi(8960, 34176, 34166);
	        GL11.glTexEnvi(8960, 34192, 768);
	        GL11.glTexEnvi(8960, 34162, 7681);
	        GL11.glTexEnvi(8960, 34184, 5890);
	        GL11.glTexEnvi(8960, 34200, 770);
	    }

	    public static void disableOutlineMode()
	    {
	    	GL11.glTexEnvi(8960, 8704, 8448);
	    	GL11.glTexEnvi(8960, 34161, 8448);
	    	GL11.glTexEnvi(8960, 34162, 8448);
	    	GL11.glTexEnvi(8960, 34176, 5890);
	    	GL11.glTexEnvi(8960, 34184, 5890);
	    	GL11.glTexEnvi(8960, 34192, 768);
	    	GL11.glTexEnvi(8960, 34200, 770);
	    }
		
		private float interpolateRotation(float p_77034_1_, float p_77034_2_, float p_77034_3_) {
			float f3;

			for (f3 = p_77034_2_ - p_77034_1_; f3 < -180.0F; f3 += 360.0F) {
				;
			}

			while (f3 >= 180.0F) {
				f3 -= 360.0F;
			}

			return p_77034_1_ + p_77034_3_ * f3;
		}
		
		protected void renderModel(EntityLivingBase p_77036_1_, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float p_77036_7_)
	    {
	        if (!p_77036_1_.isInvisible())
	        {
	            this.mainModel.render(p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
	        }
	        else if (!p_77036_1_.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer))
	        {
	            GL11.glPushMatrix();
	            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.15F);
	            GL11.glDepthMask(false);
	            GL11.glEnable(GL11.GL_BLEND);
	            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	            GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
	            this.mainModel.render(p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
	            GL11.glDisable(GL11.GL_BLEND);
	            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
	            GL11.glPopMatrix();
	            GL11.glDepthMask(true);
	        }
	        else
	        {
	            this.mainModel.setRotationAngles(p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_, p_77036_1_);
	        }
	    }
		
	    public static Entity getMouseOver(float p_78473_1_)
	    {
	    	Minecraft mc = Minecraft.getMinecraft();
	        if (mc.renderViewEntity != null)
	        {
	            if (mc.theWorld != null)
	            {
	                mc.pointedEntity = null;
	                double var2 = (double)mc.playerController.getBlockReachDistance();
	                mc.objectMouseOver = mc.renderViewEntity.rayTrace(var2, p_78473_1_);
	                double var4 = var2;
	                Vec3 var6 = mc.renderViewEntity.getPosition(p_78473_1_);

	                if (mc.playerController.extendedReach())
	                {
	                    var2 = 1000.0D;
	                    var4 = 1000.0D;
	                }
	                else
	                {
	                    var4 = 1000.0D;
	                    var2 = 1000.0D;
	                }

	                Vec3 var7 = mc.renderViewEntity.getLook(p_78473_1_);
	                Vec3 var8 = var6.addVector(var7.xCoord * var2, var7.yCoord * var2, var7.zCoord * var2);
	                Entity pointedEntity = null;
	                Vec3 var9 = null;
	                float var10 = 1.0F;
	                List var11 = mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.renderViewEntity, mc.renderViewEntity.boundingBox.addCoord(var7.xCoord * var2, var7.yCoord * var2, var7.zCoord * var2).expand((double)var10, (double)var10, (double)var10));
	                double var12 = var4;

	                for (int var14 = 0; var14 < var11.size(); ++var14)
	                {
	                    Entity var15 = (Entity)var11.get(var14);

	                    if (var15.canBeCollidedWith() && var15 instanceof EntityPlayer)
	                    {
	                        float var16 = var15.getCollisionBorderSize();
	                        AxisAlignedBB var17 = var15.boundingBox.expand((double)var16, (double)var16, (double)var16);
	                        MovingObjectPosition var18 = var17.calculateIntercept(var6, var8);

	                        if (var17.isVecInside(var6))
	                        {
	                            if (0.0D < var12 || var12 == 0.0D)
	                            {
	                                pointedEntity = var15;
	                                var9 = var18 == null ? var6 : var18.hitVec;
	                                var12 = 0.0D;
	                            }
	                        }
	                        else if (var18 != null)
	                        {
	                            double var19 = var6.distanceTo(var18.hitVec);

	                            if (var19 < var12 || var12 == 0.0D)
	                            {
	                                if (var15 == mc.renderViewEntity.ridingEntity)
	                                {
	                                    if (var12 == 0.0D)
	                                    {
	                                        pointedEntity = var15;
	                                        var9 = var18.hitVec;
	                                    }
	                                }
	                                else
	                                {
	                                    pointedEntity = var15;
	                                    var9 = var18.hitVec;
	                                    var12 = var19;
	                                }
	                            }
	                        }
	                    }
	                }

	                if (pointedEntity != null && (var12 < var4))
	                {
	                	return pointedEntity;
	                }
	            }
	        }
	        return null;
	    }
}
