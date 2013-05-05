package org.mcsg.survivalgames.net;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

import org.mcsg.survivalgames.SurvivalGames;




public class FileCache {

    private static FileCache instance = new FileCache();
    private static HashMap<String, String>html = new HashMap<String, String>();
    private FileCache(){

    }

    public static FileCache getInstance(){
        return instance;
    }

    public static String getHTML(String pagename, boolean template){
        if(html.get(pagename)== null){
            loadPage(pagename, template);
        }

        return html.get(pagename);

    }

    public static void loadPage(String pagename, boolean template){
        
        Scanner scan = null;          
        File f = new File(SurvivalGames.getPluginDataFolder()+((template)?"/www/template.html": "/www/pages/"+pagename+".html"));

        try{
            scan = new Scanner(f);
        }catch(Exception e){System.out.println("Survival Games webstats - Could not load page: " + pagename +"    "+f.getAbsolutePath());}
        String data = "";

        if(scan == null){
            html.put(pagename, "404 - Not found");
            return;
        }
        while(scan.hasNext()){
            data = data + scan.nextLine();
        }
        html.put(pagename, data);
    }
}

