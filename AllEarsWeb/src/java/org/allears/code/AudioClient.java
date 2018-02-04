package org.allears.code;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author MSI Laptop
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.sound.sampled.*;

public class AudioClient extends JFrame {

    /**
     * @return the ipaddr
     */
    public String getIpaddr() {
        return ipaddr;
    }

    /**
     * @param ipaddr the ipaddr to set
     */
    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }

    boolean stopaudioCapture = false;
    ByteArrayOutputStream byteOutputStream;
    AudioFormat adFormat;
    TargetDataLine targetDataLine;
    AudioInputStream InputStream;
    SourceDataLine sourceLine;
    Graphics g;
    final int bufferSize = 7940; //8192, 7940
    private String ipaddr;
    int socketPort = 8786;
    int packetPort = 9786; 

    public static void main(String args[]) {
        new AudioClient();
    }

    public AudioClient() {
        final JButton capture = new JButton("Capture");
        final JButton stop = new JButton("Stop");
        final JButton play = new JButton("Playback");
        
        ipaddr = null;
        
        capture.setEnabled(true);
        stop.setEnabled(false);

        capture.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                capture.setEnabled(false);
                stop.setEnabled(true);
                captureAudio();
            }
        });
        getContentPane().add(capture);

        stop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                capture.setEnabled(true);
                stop.setEnabled(false);
                stopaudioCapture = true;
                targetDataLine.close();
            }
        });
        getContentPane().add(stop);

        getContentPane().setLayout(new FlowLayout());
        setTitle("Capture Demo");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 100);
        getContentPane().setBackground(Color.white);
        setVisible(true);

        g = (Graphics) this.getGraphics();
    }
    
    private void captureAudio() {
        try {
            adFormat = getAudioFormat();
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, adFormat);
            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            targetDataLine.open(adFormat, bufferSize);
            targetDataLine.start();

            Thread captureThread = new Thread(new CaptureThread());
            captureThread.start();
        } catch (Exception e) {
            StackTraceElement stackEle[] = e.getStackTrace();
            for (StackTraceElement val : stackEle) {
                System.out.println(val);
            }
            System.exit(0);
        }
    }

    private AudioFormat getAudioFormat() {
        float sampleRate = 16000.0F;
        int sampleInbits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleInbits, channels, signed, bigEndian);
    }

    class CaptureThread extends Thread {

        byte tempBuffer[] = new byte[bufferSize];

        public void run() {

            byteOutputStream = new ByteArrayOutputStream();
            stopaudioCapture = false;
            try {
                DatagramSocket clientSocket = new DatagramSocket(8786);
                InetAddress IPAddress = InetAddress.getByName(getIpaddr());
                while (!stopaudioCapture) {
                    int cnt = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
                    if (cnt > 0) {
                        DatagramPacket sendPacket = new DatagramPacket(tempBuffer,
                                tempBuffer.length, IPAddress, 9786);
                        clientSocket.send(sendPacket);
                        byteOutputStream.write(tempBuffer, 0, cnt);
                    }
                }
                byteOutputStream.close();
            } catch (Exception e) {
                System.out.println("CaptureThread::run()" + e);
                System.exit(0);
            }
        }
    }
}
