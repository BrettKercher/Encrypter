/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.*;

/**
 *
 * @author Brett
 */
public class Decrypt {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    

	String encryptedIn = args[0];
	String key = args[1];

        long encrypted = 0;
        String padding = "00000000";
        
        long n = 0;
        long e = 0;
        long d = 0;
        try
        {    
            BufferedReader in = new BufferedReader(new FileReader(key));
            n = Long.valueOf(in.readLine()).longValue();
            e = Long.valueOf(in.readLine()).longValue();
            d = Long.valueOf(in.readLine()).longValue();
            in.close();
        } catch (FileNotFoundException ex) {
            System.err.println("FileNotFoundException: " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
        }
        
        String binaryD = Long.toBinaryString(d);
        

        try
        {    
            InputStream input = new BufferedInputStream(new FileInputStream(encryptedIn));
            OutputStream output = new BufferedOutputStream(new FileOutputStream("decrypted"));
        
        
            while(input.available() > 0)
            {    

                byte[] result = new byte[4];
                //InputStream input = new BufferedInputStream(new FileInputStream("encrypted"));
                input.read(result, 0, 4); 
                int finalInt = 0;
                finalInt = (finalInt << 8) + (int)(0xFF & result[0]);
                finalInt = (finalInt << 8) + (int)(0xFF & result[1]); 
                finalInt = (finalInt << 8) + (int)(0xFF & result[2]); 
                finalInt = (finalInt << 8) + (int)(0xFF & result[3]);

                encrypted = finalInt;

                long c = 1;
                for(int j = 0; j < binaryD.length(); j++)
                {
                    if(binaryD.charAt(j) == '0')
                        c = ((c*c) % n);
                    else
                        c = ((((c*c) % n)*encrypted) % n);
                }

                String decrypted = padding + padding + Long.toBinaryString(c);
                decrypted = decrypted.substring(decrypted.length() - 24);
                c = Long.parseLong(decrypted, 2);
                
                output.write((byte)((c >> 16) & 0xFF));
                output.write((byte)((c >> 8) & 0xFF));
                output.write((byte)(c & 0xFF));


            }
            output.close();
        }
        catch(Exception ex)
        {
            System.err.println("Error:" + ex.getMessage());
        }
        
        
    }
}
