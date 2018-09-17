package bitcamp.java110.cms;
import java.util.Scanner;

public class App {

    static Scanner keyIn = new Scanner(System.in);

    public static void main(String[] args) throws Exception {

        Thread main = Thread.currentThread();
        System.out.println(main.getName()); 

        ThreadGroup mainGroup = main.getThreadGroup();
        System.out.println(mainGroup.getName()); 

        ThreadGroup systemGroup = mainGroup.getParent();
        System.out.println(systemGroup.getName());


        Thread[] threads = new Thread[20];
        
        System.out.println("[쓰레드]");
        int count = systemGroup.enumerate(threads, false);
        for(int i = 0 ; i < count ; i++) {
            System.out.println(threads[i].getName());
        }


        ThreadGroup[] tgs = new ThreadGroup[20];
        
        System.out.println("[쓰레드 그룹]");
        count = systemGroup.enumerate(tgs, false);
        for(int i = 0 ; i < count ; i++) {
            System.out.println(tgs[i].getName());
        }
        
        
        
        System.out.println("[main 그룹]");
        count = mainGroup.enumerate(threads, false);
        for(int i = 0 ; i < count ; i++) {
            System.out.println(threads[i].getName());
        }
        
        
        System.out.println("[Inno~ 그룹]");
        count = tgs[1].enumerate(threads, false);
        for(int i = 0 ; i < count ; i++) {
            System.out.println(threads[i].getName());
        }
        
    }


}






















