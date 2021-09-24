package net.smb.moderutils;

public class Color {
	public float colorR = 0, colorG = 0, colorB = 0, colorA = 1.0F;
	
	public Color(float colorR, float colorG, float colorB) {
		this.colorR = colorR;
		this.colorG = colorG;
		this.colorB = colorB;
		this.colorA = 1;
	}
	
	public Color(float colorR, float colorG, float colorB, float colorA) {
		this.colorR = colorR;
		this.colorG = colorG;
		this.colorB = colorB;
		this.colorA = colorA;
	}
	
	public Color(Color color, float colorA) {
		this.colorR = color.colorR;
		this.colorG = color.colorG;
		this.colorB = color.colorB;
		this.colorA = colorA;
	}
	
	public Color(String hexColor) {
		setHexColor(hexColor);
	}
	
	public String getHexColor() {
		String hexColor = "";
		String s;
		s = Integer.toHexString((int)(this.colorR*255.0f));
		if(s.length() == 1) s = "0"+s;
		hexColor += s;
		
		s =  Integer.toHexString((int)(this.colorG*255.0f));
		if(s.length() == 1) s = "0"+s;
		hexColor += s;
		
		s =  Integer.toHexString((int)(this.colorB*255.0f));
		if(s.length() == 1) s = "0"+s;
		hexColor += s;
		
		s =  Integer.toHexString((int)(this.colorA*255.0f));
		if(s.length() == 1) s = "0"+s;
		hexColor += s;
		return hexColor;
	}
	
	public void setHexColor(String hexColor) {
		char[] chars = hexColor.toCharArray();
		this.colorR = Integer.parseInt(chars[0]+""+chars[1], 16)/255.0F;
		this.colorG = Integer.parseInt(chars[2]+""+chars[3], 16)/255.0F;
		this.colorB = Integer.parseInt(chars[4]+""+chars[5], 16)/255.0F;
		if(chars.length >= 8) this.colorA = Integer.parseInt(chars[6]+""+chars[7], 16)/255.0F;
		else this.colorA = 1.0f;
	}
}
