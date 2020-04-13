import java.lang.Object.*;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;
import java.io.*;

/**
 * A sample class that demonstrates how to create and display a JFrame.
 *
 */

public class Main
{
  static HashSet<Integer> fileInputs = new HashSet<>();

  public static void main(String[] args)
  {
	System.out.println("Running");
	  
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        // create a jframe, giving it an initial title
        JFrame frame = new JFrame("IIA: Inverted Index Application");
        
        // set the jframe size. in a more complicated application you will
        // probably call frame.pack() before displaying it.
        frame.setSize(new Dimension(300,200));
        
        // center the frame
        frame.setLocationRelativeTo(null);
        
        // set this so your application can easily be stopped/killed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // display the frame
        frame.setVisible(true);

        final JButton search=new JButton("Search");
        search.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent e){
            String input = JOptionPane.showInputDialog(
                    null, "Enter a term...");
            if (input != null) {
                search(input);
            }
          }
        });
        search.setBounds(120,0,140,40);
        frame.add(search);

        final JButton topN=new JButton("Top N");
        topN.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent e){
            String input = JOptionPane.showInputDialog(
                    null, "Enter a positive integer...");
            if (input != null) {
              topN(Integer.parseInt(input));
            }
          }
        });
        topN.setBounds(120,50,140,40);
        frame.add(topN);

        final JButton b=new JButton("Hugo");
        b.setBackground(Color.RED);
        b.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent e){
            if (!fileInputs.contains(0)) {
              fileInputs.add(0);
              b.setBackground(Color.GREEN);
            } else {
              fileInputs.remove(0);
              b.setBackground(Color.RED);
            }
          }
        });
        b.setBounds(120,100,140,40);
        frame.add(b);

        final JButton c=new JButton("Tolstoy");
        c.setBackground(Color.RED);
        c.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent e){
            if (!fileInputs.contains(1)) {
              fileInputs.add(1);
              c.setBackground(Color.GREEN);
            } else {
              fileInputs.remove(1);
              c.setBackground(Color.RED);
            }
          }
        });
        c.setBounds(120,150,140,40);
        frame.add(c);

        final JButton d=new JButton("Shakespeare");
        d.setBackground(Color.RED);
        d.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent e){
            if (!fileInputs.contains(2)) {
              fileInputs.add(2);
              d.setBackground(Color.GREEN);
            } else {
              fileInputs.remove(2);
              d.setBackground(Color.RED);
            }
          }
        });
        d.setBounds(120,200,140,40);
        frame.add(d);

        final JButton a=new JButton("Select All");
        a.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent e){
            fileInputs.add(0);
            fileInputs.add(1);
            fileInputs.add(2);
            b.setBackground(Color.GREEN);
            c.setBackground(Color.GREEN);
            d.setBackground(Color.GREEN);
          }
        });
        a.setBounds(120,250,140,40);
        frame.add(a);

        final JButton z=new JButton("Deselect All");
        z.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent e){
            fileInputs.remove(0);
            fileInputs.remove(1);
            fileInputs.remove(2);
            b.setBackground(Color.RED);
            c.setBackground(Color.RED);
            d.setBackground(Color.RED);
          }
        });
        z.setBounds(120,300,140,40);
        frame.add(z);

        JButton e=new JButton("Generate");
        e.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent e){
            System.out.println(fileInputs);
            Integer messageInt = 0;
            for (Integer id: fileInputs) {
              if (id == 0) {
                messageInt += 100;
              } else if (id == 1) {
                messageInt += 10;
              } else if (id == 2) {
                messageInt += 1;
              }
            }
            String message = sendInfoToCloud(messageInt.toString());
          }
        });
        e.setBounds(120,350,140,40);
        frame.add(e);

        frame.setSize(400,400);
        frame.setLayout(null);
        frame.setVisible(true);
      }
    });
  }

  static String sendInfoToCloud(String msg) {
    try {
      DatagramSocket socket = new DatagramSocket();
      InetAddress address = InetAddress.getByName("192.168.1.175");
      byte[] buf = msg.getBytes();
      DatagramPacket packet
              = new DatagramPacket(buf, buf.length, address, 4445);
      socket.send(packet);
      packet = new DatagramPacket(buf, buf.length);
      socket.close();
      return "Done";
    } catch(Exception e) {
      return "Failed in sendInfoToCloud";
    }
  }

  static void search(String term) {
    File file = new File("output.txt");
    try {
      Scanner sc = new Scanner(file);
      String found = null;
      while (sc.hasNext()) {
        String word = sc.next();
        String data = sc.nextLine();
        if (word.equals(term)) {
          found = word + data;
          break;
        }
      }

      JFrame frame = new JFrame("Term Search Result");
      frame.setSize(new Dimension(800,200));
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);

      JLabel label1 = new JLabel("Result: " + found);
      if (found == null || found.equals("")) {
        label1.setText("Term Not Found!");
      }
      frame.add(label1);

      found = null;

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static void topN(Integer n) {
    File file = new File("output.txt");
    try {
      Scanner sc = new Scanner(file);
      ArrayList<String> words = new ArrayList<>();
      ArrayList<Integer> counts = new ArrayList<>();

      while (sc.hasNext()) {
        String line = sc.nextLine();
        Integer frequency = 0;
        StringTokenizer tokenizer = new StringTokenizer(line);
        String word = tokenizer.nextToken();
        while (tokenizer.hasMoreTokens()) {
          StringBuilder builder = new StringBuilder("");
          String currentNumber = tokenizer.nextToken();
          int i = currentNumber.length() - 1;
          while (true) {
            if (!"0123456789".contains(currentNumber.substring(i))) {
              currentNumber = currentNumber.substring(0, i);
              i--;
            } else {
              break;
            }
          }
          while (true) {
            if ("0123456789".contains(currentNumber.substring(i))) {
              builder.insert(0, currentNumber.substring(i));
              currentNumber = currentNumber.substring(0, i);
              i--;
            } else {
              break;
            }
          }
          frequency += Integer.parseInt(builder.toString());
        }

        if (words.size() < n) {
          words.add(word);
          counts.add(frequency);
        } else {
          for (int p = 0; p < n; p++) {
            if (frequency > counts.get(p)) {
              words.set(p, word);
              counts.set(p, frequency);
              break;
            }
          }
        }
      }

      JFrame frame = new JFrame("Top N Search Result");
      frame.setSize(new Dimension(800,200));
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);

      HashMap<String,Integer> result = new HashMap<>();
      for (int i = 0; i < n; i++) {
        result.put(words.get(i), counts.get(i));
      }

      JLabel label1 = new JLabel("Top " + n + " words: " + result.toString());
      frame.add(label1);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}



