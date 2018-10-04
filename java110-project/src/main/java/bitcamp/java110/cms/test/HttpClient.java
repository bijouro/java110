package bitcamp.java110.cms.test;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class HttpClient {

    public static void main(String[] args) throws Exception{
        try (
            Socket socket = new Socket("localhost", 80);
            PrintStream out = new PrintStream(socket.getOutputStream());
            Scanner in = new Scanner(socket.getInputStream());

             )   {
            // HTTP ?öîÏ≤?
            // 1) Request-line Ï∂úÎ†•
            out.println("GET / HTTP/1.1");

            // 2) Request Header
            out.println("Host: localhost");
            out.println("Connection: keep-alive");
            out.println("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
            out.println("Accept: text/html,application/xhtml+xml,application/xml; q=0.9,image/webp,image/apng,*/*;q=0.8");
            out.println("Accept-Language: ko-KR,ko;q=0.9,en-US; q=0.8,en;q=0.7,ja;q=0.6");

            // 3) End of Header
            out.println();

            // 4) ?ÑúÎ≤ÑÏóê Î≥¥ÎÇº ?ç∞?ù¥?Ñ∞ ( message-body) ?Éù?ûµ Í∞??ä•.


            while(true) {
                String str = in.nextLine();
                System.out.println(str);


            }
        }
    }
}
