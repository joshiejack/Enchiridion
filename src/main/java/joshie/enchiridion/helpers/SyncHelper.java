package joshie.enchiridion.helpers;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.network.PacketHandler;
import joshie.enchiridion.network.PacketSyncFile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import static joshie.enchiridion.network.core.PacketPart.SEND_SIZE;

public class SyncHelper {
    public volatile static HashSet<EntityPlayerMP> playersSynced = new HashSet<>();
    public volatile static String[] servermd5;
    public volatile static String[] md5requests;

    public static void resetSyncing() {
        playersSynced = new HashSet<>(); //Reset the syncing data

        try {
            //Step one is to build a list of all the files in the enchiridion books directory
            ArrayList<String> temp = new ArrayList<>();
            File root = FileHelper.getBooksDirectory();
            Collection<File> files = FileUtils.listFiles(root, new String[]{"json", "png", "jpg", "jpeg", "gif"}, true);
            for (File file : files) {
                temp.add(DigestUtils.md5Hex(new FileInputStream(file)));
            }

            //Place this in to a string array
            servermd5 = new String[temp.size()];
            for (int i = 0; i < temp.size(); i++) {
                servermd5[i] = temp.get(i);
            }
        } catch (Exception ignored) {
        }
    }

    public static boolean doMD5Match(String[] received) {
        ArrayList<String> temp = new ArrayList<>();
        File root = FileHelper.getBooksDirectory();
        boolean foundAll = true;

        //Checking all these files, if there isn't a match, we'll ask the server to send us the file in question
        Collection<File> files = FileUtils.listFiles(root, new String[]{"json", "png", "jpg", "jpeg", "gif"}, true);
        for (String s : received) {
            boolean matchFound = false;
            for (File file : files) {
                try {
                    String md5 = DigestUtils.md5Hex(new FileInputStream(file));
                    if (md5.equals(s)) {
                        matchFound = true;
                        break;
                    }
                } catch (Exception ignored) {
                }
            }

            if (!matchFound) {
                temp.add(s);
                foundAll = false;
            }
        }

        //Client should ask the server for these strings
        md5requests = new String[temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            md5requests[i] = temp.get(i);
        }


        return foundAll;
    }

    public static HashMap<String, byte[][]> bytesServer = new HashMap<>();
    public static HashMap<String, byte[][]> bytesClient = new HashMap<>();

    //These md5 matching files should be sent to the client
    public static void sendFilesToClient(EntityPlayer player, String[] temp) {
        File root = FileHelper.getBooksDirectory();
        Collection<File> files = FileUtils.listFiles(root, new String[]{"json", "png", "jpg", "jpeg", "gif"}, true);
        for (String s : temp) {
            for (File file : files) {
                try {
                    String md5 = DigestUtils.md5Hex(new FileInputStream(file));
                    if (md5.equals(s)) {
                        String directory = file.toString().replace(root.toString(), "");
                        if (EConfig.debugMode) Enchiridion.log(Level.INFO, "Sending a file to the client");
                        //Send the length of the array of bytes
                        byte[] original = new byte[(int) file.length()];
                        try {
                            FileInputStream is = new FileInputStream(file);
                            is.read(original);
                            is.close();
                        } catch (Exception ignored) {
                        }
                        byte[][] bites = SplitHelper.splitByteArrayEvery(original, 2000);
                        bytesServer.put(directory, bites);
                        PacketHandler.sendToClient(new PacketSyncFile(directory, SEND_SIZE, bites.length), player);
                    }
                } catch (Exception ignored) {
                }
            }
        }
    }
}