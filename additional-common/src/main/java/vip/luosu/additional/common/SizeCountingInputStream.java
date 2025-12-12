package vip.luosu.additional.common;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SizeCountingInputStream extends FilterInputStream {

    private long size = 0;

    public SizeCountingInputStream(InputStream in) {
        super(in);
    }

    @Override
    public int read() throws IOException {
        int n = super.read();
        if (n != -1) size++;
        return n;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int n = super.read(b, off, len);
        if (n != -1) size += n;
        return n;
    }

    public long getSize() {
        return size;
    }
}
