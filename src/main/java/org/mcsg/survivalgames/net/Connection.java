package org.mcsg.survivalgames.net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;



public class Connection extends Thread{

    BufferedReader in;
    DataOutputStream out;
    Socket skt;
    HashMap<String, String>html = new HashMap<String, String>();

    public Connection(Socket skt){
        try{
            this.in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
            this.out = new DataOutputStream(skt.getOutputStream());
            this.skt = skt;
        }catch(Exception e){}
    }

    public void run(){
        try{
            write("ADFSADFDSAF",out, in.readLine());
            skt.close();
        }catch(Exception e){e.printStackTrace();}


    }




    public void getHTML(String pageName){

    }

    public void parseHTML(String page){


    }
    
    public void write(String str, OutputStream out, String header){
        String s = "HTTP/1.0 ";
        s = s + "200 OK";
        s = s + "\r\n"; 
        s = s + "Connection: close\r\n"; 
        s = s + "Server: SurvivalGames v0\r\n"; 
        s = s + "Content-Type: text/html\r\n";
        s = s + "\r\n";
     
        String template = FileCache.getHTML("template", true);
       
        
        
        String[] args = header.split(" ")[1].trim().split("/");
        System.out.print(args[1]);
        
        String page     = template; //.replace("{#page}", FileCache.getHTML(args[1], false));
        
        
        page = parse(page);
        
        str = s + page;
        
        try {
            out.write(str.getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    public String parse(String page){
        return page;
    }

}
