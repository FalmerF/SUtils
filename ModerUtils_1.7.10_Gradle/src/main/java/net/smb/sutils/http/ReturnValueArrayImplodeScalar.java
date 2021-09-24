package net.smb.sutils.http;

import com.google.common.base.Joiner;
import net.eq2online.macros.scripting.api.ReturnValueArray;

public class ReturnValueArrayImplodeScalar extends ReturnValueArray
{
    private String glue;
    
    public ReturnValueArrayImplodeScalar(final boolean append, final String glue) {
        super(append);
        this.glue = glue;
    }
    
    public String getString() {
        if (this.size() > 0) {
            final Joiner j = Joiner.on(this.glue).skipNulls();
            return j.join((Iterable)this.getStrings());
        }
        return "";
    }
}
