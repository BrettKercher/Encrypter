
import java.io.*;
import java.util.ArrayList;
/**
 *
 * @author Brett
 */
public class Encrypt {

    static File inFile;
    static File key;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
	String plaintext = args[0];
	String keyFile = args[1];

        inFile = new File(plaintext);
        key = new File(keyFile);
        
        char[] charArray;
        byte[] asciiArray;
        ArrayList<String> binaryList = new ArrayList<String>();
        String binBlock = "";
        long numBlock = 0;
        
        
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
        
        String binaryE = Long.toBinaryString(e);
        String padding = "00000000";
        
        
        //Read file (getArray method from io)
        char[] ret = new char[(int) (inFile.length()+1)];
	    int retCursor = 0;
            try
            {
                FileInputStream from = new FileInputStream(inFile); 
                while (true) 
                    {
                        byte nextByte = (byte) from.read(); 
                        if ( nextByte == -1) break ; 
                        char nextChar = (char) nextByte; 
                        ret[retCursor++] = nextChar; 
                    }
                ret[(int)(inFile.length())] = '\000';
                
            } catch (FileNotFoundException ex) {
                System.err.println("FileNotFoundException: " + ex.getMessage());
            } catch (IOException ex) {
                System.err.println("IOException: " + ex.getMessage());
            }
            charArray = ret;
            //
            
            asciiArray = new byte[charArray.length-1];
            
            for(int i = 0; i< asciiArray.length; i++)
                asciiArray[i] = (byte)charArray[i];
            
            for(int i : asciiArray)
            {    
                String temp = padding + ((Integer)i).toBinaryString(i);
                temp = temp.substring(temp.length() - 8, temp.length());
                binaryList.add(temp);
            }
            
            while(binaryList.size() % 3 != 0)
                binaryList.add(padding);
            
            OutputStream out = null;
            try
            {    
            out = new BufferedOutputStream(new FileOutputStream("encrypted"));    
            
            for(int i = 0; i < binaryList.size(); i+=3)
            {    
                for(int j = 0; j < 3; j++)
                    binBlock += binaryList.get(i + j);
                binBlock = padding + binBlock;
                numBlock = Long.parseLong(binBlock, 2);

                //Encryption
                long c = 1;
                for(int k = 0; k < binaryE.length(); k++)
                {
                    if(binaryE.charAt(k) == '0')
                        c = ((c*c) % n);
                    else
                        c = ((((c*c) % n)*numBlock) % n);
                }
                
                String encrypted = Long.toBinaryString(c);
                String temp = padding + encrypted;
                
                if(temp.length() >= 32)
					encrypted = temp.substring(temp.length() - 32);
				else
					encrypted = temp.substring(0);
                
                binBlock = "";
                
                    out.write((byte)((c >> 24) & 0xFF));
                    out.write((byte)((c >> 16) & 0xFF));
                    out.write((byte)((c >> 8) & 0xFF));
                    out.write((byte)(c & 0xFF));
                    
            } 
            out.close();
            }
            catch(Exception ex)
                {
                    System.err.println("Error:" + ex.getMessage());
                }  
    }
    
    
    
}