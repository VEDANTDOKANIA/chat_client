package com.company;


import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;


public class Client{
    private static Socket Socket;

    private static BufferedReader br;
    private static BufferedWriter bw ;
    private static String username;

    public static int getnumber(){
        Scanner scn = new Scanner(System.in);
        int amount;
        while(true)
        {
            try {
                amount = Integer.parseInt(scn.next());
                break;
            }
            catch (NumberFormatException e )
            {
                System.out.print("\033[1;31m");
                System.out.println("Enter valid character only");
                System.out.print("\033[0m");
            }
        }
        return amount;

    }


    public static void sendMessage(){
        try{

            Scanner scn = new Scanner(System.in);
            while(Socket.isConnected())
            {
                String msgtosend= scn.nextLine();
                bw.write(username + ":"+ msgtosend);
                bw.newLine();
                bw.flush();
                listenformessage();
            }

        }
        catch (Exception e)
        {
            CloseEverything(Socket , br, bw);
        }
    }

    public static void listenformessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgfromgroupchat ;

                while(Socket.isConnected())
                {
                    try {
                        msgfromgroupchat = br.readLine();
                        System.out.println(msgfromgroupchat);
                        //System.out.println("i am here");
                        Log my_log = new Log("log.txt");
                    } catch (IOException e) {
                        CloseEverything(Socket,br,bw);
                        e.printStackTrace();
                    }

                }

            }
        }).start();
    }

    private static void CloseEverything(Socket socket, BufferedReader br, BufferedWriter bw) {

        try {
            if(br!= null)
            {
                br.close();
            }
            if(bw!= null)
            {
                bw.close();
            }
            if(socket!= null)
            {
                socket.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {
        Scanner scn = new Scanner(System.in);
        Socket socket = new Socket("localhost",5555);
        try {
            Client.Socket = socket;
            Client.bw= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())) ;
            Client.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Enter your username");
            String username = scn.nextLine();
            bw.write(username);
            bw.newLine();
            bw.flush();

            Client.username = username;

        } catch (Exception e) {
            e.printStackTrace();
            CloseEverything(socket,br,bw);
        }


       System.out.println("1. Chat with individual person");
        System.out.println("2. Broadcast Message");
        int option = getnumber();
        if(option==1)
        {
            bw.write("Unique_id_available");
            bw.newLine();
            bw.flush();

            System.out.println("Enter the username of the person with whom you want to chat");
            var uniqueid = scn.nextLine();
            bw.write(uniqueid);
            bw.newLine();
            bw.flush();


                Client.sendMessage();
                Client.listenformessage();

        }
        if(option==2)
        {
            bw.write("unique_id_notavailable");
            bw.newLine();
            bw.flush();


                Client.sendMessage();
                Client.listenformessage();

        }
        else if(option !=1 || option!=2)
        {
            System.out.println("Pls select valid options");
        }






    }
}
