/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.tokenizer.utils;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OutPutWriter implements StreamingOutput {

    private final File file;

    public OutPutWriter(File file) {this.file = file;}

    @Override
    /**
     * write the stream
     */
    public void write(OutputStream out) throws IOException, WebApplicationException {
        FileInputStream input = null;
        byte[] buffer = new byte[256 * 1024];
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
        try {
            input = new FileInputStream(file);
            FileChannel channel = input.getChannel();
            for (int length = 0; (length = channel.read(byteBuffer)) != -1;) {
                out.write(buffer, 0, length);
                byteBuffer.clear();
            }
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    Logger.getLogger(OutPutWriter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            file.delete();
        }
    }


}
