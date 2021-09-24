package net.smb.moderutils.http;

public enum EnumHttpMethod
{
    GET("GET"), 
    POST("POST"), 
    PUT("PUT"), 
    DELETE("DELETE"), 
    PATCH("PATCH");
    
    private final String text;
    
    private EnumHttpMethod(final String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return this.text;
    }
}
