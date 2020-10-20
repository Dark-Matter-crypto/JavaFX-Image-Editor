package csc2b.client;

import javafx.scene.image.Image;

import java.io.*;
import java.net.Socket;
import java.util.Base64;

public class ImageProcessor {
    private int port;

    //API call function URLs
    private static final String GREYSCALE_URL = "/api/GrayScale";
    private static final String CROP_URL = "/api/Crop";
    private static final String ROTATE_URL = "/api/Rotate";
    private static final String EROSION_URL = "/api/Erosion";
    private static final String DILATION_URL = "/api/Dilation";
    private static final String CANNY_URL = "/api/Canny";
    private static final String FAST_URL = "/api/Fast";
    private static final String ORB_URL = "/api/ORB";

    Socket socket = null;
    BufferedReader br = null;
    InputStream is = null;
    DataOutputStream dos = null;
    BufferedOutputStream bos = null;
    OutputStream os = null;

    private boolean isConnected = false;

    //Class constructor to connect to server
    public ImageProcessor(int port){
        this.port = port;
        try {
            socket = new Socket("localhost", this.port);
            is = socket.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            os = socket.getOutputStream();
            bos = new BufferedOutputStream(os);
            dos = new DataOutputStream(bos);
            isConnected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Method to check if connected to server
    public boolean isConnected(){
        return isConnected;
    }

    //Function that takes an image file and the type of effect to apply to image
    public Image processImage(File file, String effect){
        String encodedFile = null;
        Image result = null;
        try{
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte [(int)file.length()];
            fis.read(bytes);

            encodedFile = new String(Base64.getEncoder().encodeToString(bytes));
            byte[] bytesToSend = encodedFile.getBytes();

            //Check which function call to make
            switch(effect){
                case "GREYSCALE":
                    dos.write(("POST " + GREYSCALE_URL + " HTTP/1.1\r\n").getBytes());
                    break;
                case "CROP":
                    dos.write(("POST " + CROP_URL + " HTTP/1.1\r\n").getBytes());
                    break;
                case "ROTATE":
                    dos.write(("POST " + ROTATE_URL + " HTTP/1.1\r\n").getBytes());
                    break;
                case "EROSION":
                    dos.write(("POST " + EROSION_URL + " HTTP/1.1\r\n").getBytes());
                    break;
                case "DILATION":
                    dos.write(("POST " + DILATION_URL + " HTTP/1.1\r\n").getBytes());
                    break;
                case "CANNY":
                    dos.write(("POST " + CANNY_URL + " HTTP/1.1\r\n").getBytes());
                    break;
                case "FAST":
                    dos.write(("POST " + FAST_URL + " HTTP/1.1\r\n").getBytes());
                    break;
                case "ORB":
                    dos.write(("POST " + ORB_URL + " HTTP/1.1\r\n").getBytes());
                    break;
            }

            dos.write(("Content-Type: application/text\r\n").getBytes());
            dos.write(("Content-Length: " + encodedFile.length() + "\r\n").getBytes());
            dos.write(("\r\n").getBytes());
            dos.write(bytesToSend);
            dos.flush();
            dos.write(("\r\n").getBytes());

            //Get response
            String response = "";
            String line = "";
            while (!(line = br.readLine()).equals("")){
                response += line + "\n";
            }
            System.out.println(response);

            //Get image data
            String imageData = "";
            while ((line = br.readLine()) != null){
                imageData += line;
            }
            System.out.println(imageData);

            String base64Str = imageData.substring(imageData.indexOf('\'') + 1, imageData.lastIndexOf('}')-1);
            System.out.println(base64Str);

            //Decode base64Str
            byte[] decodedStr = Base64.getDecoder().decode(base64Str);

            //Set the result image
            result = new Image(new ByteArrayInputStream(decodedStr));
            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            try {
                dos.close();
                os.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Return the processed image
            return result;
        }
    }


}
