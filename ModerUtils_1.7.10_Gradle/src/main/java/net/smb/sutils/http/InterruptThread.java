package net.smb.sutils.http;

import java.net.HttpURLConnection;
import java.util.concurrent.atomic.AtomicInteger;
import java.net.URLConnection;

public class InterruptThread implements Runnable
{
    Thread parent;
    URLConnection con;
    AtomicInteger responseCode;
    
    public InterruptThread(final Thread parent, final URLConnection con, final AtomicInteger responseCode) {
        this.parent = parent;
        this.con = con;
        this.responseCode = responseCode;
    }
    
    @Override
    public void run() {
        try {
            Thread.sleep(15000L);
        }
        catch (InterruptedException ex) {}
        if (this.responseCode.get() == 0) {
            System.out.println("Timer thread forcing parent to quit URL connection");
            ((HttpURLConnection)this.con).disconnect();
            System.out.println("Timer thread closed URL connection held by parent, exiting");
            this.responseCode.lazySet(-1);
        }
    }
}