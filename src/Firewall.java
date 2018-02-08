import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Firewall {
    // combine all rules into four "maps" to reduce operation time.
    // array[0] stands for port range.
    // array[1] - array[5] stands for four digits of IP address.
    // Whenever we read a new rule, we just add 1 to the corresponding digit (if we delete a rule in the future, just minus 1)
    // If we want to check whether the incoming string is valid, just check whether the digits in array have value larger than 0.
    private int[][] ruleSetInTCP = new int[5][65536];
    private int[][] ruleSetOutTCP = new int[5][65536];
    private int[][] ruleSetInUDP = new int[5][65536];
    private int[][] ruleSetOutUDP = new int[5][65536];


    // For test
//    public static void main(String[] args){
//        Firewall fw = new Firewall("/Users/david/Documents/Illumio/Firewall/test/test1.csv");
//        System.out.println(fw.accept_packet("inbound", "tcp", 80, "192.168.1.2"));
//        System.out.println(fw.accept_packet("inbound", "udp", 53, "192.168.2.1"));
//        System.out.println(fw.accept_packet("outbound", "tcp", 10234, "192.168.10.11"));
//        System.out.println(fw.accept_packet("inbound", "tcp", 81, "192.168.1.2"));
//        System.out.println(fw.accept_packet("inbound", "udp", 24, "52.12.48.92"));
//    }

    public Firewall(String path){
        Path pathToCSV = Paths.get(path);
        boolean firstElement = true;
        try(BufferedReader br = Files.newBufferedReader(pathToCSV, StandardCharsets.UTF_8)){
            String line = br.readLine();
            while(line != null){
                String[] content = line.split(",");
                if(firstElement){
                    content[0] = content[0].replace(String.valueOf((char) 65279), ""); // dealing with the starting non-break space of CSV file
                    firstElement = false;
                }
                String direction = content[0];
                String type = content[1];
                int[] port = getPort(content[2]);
                int[][] ip = getIP(content[3]);
                if(direction.equals("inbound") && type.equals("tcp")){
                    writeRule(ruleSetInTCP,port,ip);
                } else if(direction.equals("outbound") && type.equals("tcp")){
                    writeRule(ruleSetOutTCP,port,ip);
                } else if(direction.equals("inbound") && type.equals("udp")){
                    writeRule(ruleSetInUDP,port,ip);
                } else {
                    writeRule(ruleSetOutUDP,port,ip);
                }
                line = br.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public boolean accept_packet(String direction, String type, int port, String ip){
        String[] ipAddress = ip.split("\\.");
        int[] ipInNum = new int[4];
        for(int i = 0; i < 4; i++){
            ipInNum[i] = Integer.parseInt(ipAddress[i]);
        }
        if(direction.equals("inbound") && type.equals("tcp")){
            return checkRule(ruleSetInTCP,port,ipInNum);
        } else if(direction.equals("outbound") && type.equals("tcp")){
            return checkRule(ruleSetOutTCP,port,ipInNum);
        } else if(direction.equals("inbound") && type.equals("udp")){
            return checkRule(ruleSetInUDP,port,ipInNum);
        } else {
            return checkRule(ruleSetOutUDP,port,ipInNum);
        }
    }

    private boolean checkRule(int[][] rule, int port, int[] ip){
        return (rule[0][port] > 0) && (rule[1][ip[0]] > 0) && (rule[2][ip[1]] > 0) && (rule[3][ip[2]] > 0) && (rule[4][ip[3]] > 0);
    }

    private int[] getPort(String str){
        int[] res = new int[2];
        if(str.contains("-")){
            String[] range = str.split("-");
            res[0] = Integer.parseInt(range[0]);
            res[1] = Integer.parseInt(range[1]);
        } else {
            res[0] = Integer.parseInt(str);
            res[1] = res[0];
        }
        return res;
    }

    private int[][] getIP(String str){
        int[][] res = new int[2][4];
        if(str.contains("-")){
            String[] range = str.split("-");
            String[] lower = range[0].split("\\.");
            String[] upper = range[1].split("\\.");
            for(int i = 0; i < 4; i++){
                res[0][i] = Integer.parseInt(lower[i]);
                res[1][i] = Integer.parseInt(upper[i]);
            }
        } else {
            String[] address = str.split("\\.");
            for(int i = 0; i < 4; i++){
                res[0][i] = Integer.parseInt(address[i]);
                res[1][i] = res[0][i];
            }
        }
        return res;
    }

    private void writeRule(int[][] rule, int[] port, int[][] ip){
        for(int i = port[0]; i <= port[1]; i++){
            rule[0][i]++;
        }
        for(int i = ip[0][0]; i <= ip[1][0]; i++){
            rule[1][i]++;
        }
        for(int i = ip[0][1]; i <= ip[1][1]; i++){
            rule[2][i]++;
        }
        for(int i = ip[0][2]; i <= ip[1][2]; i++){
            rule[3][i]++;
        }
        for(int i = ip[0][3]; i <= ip[1][3]; i++){
            rule[4][i]++;
        }
    }

}
