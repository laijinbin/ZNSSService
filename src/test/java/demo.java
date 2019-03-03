import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class demo {
    public static void main(String[] args) {
        try {
            Socket socket=new Socket("192.168.31.40",30000);
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String info = null;
            while (true){
                while((info=br.readLine())!=null){
                    System.out.println("我是客户端，服务器说："+info);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
