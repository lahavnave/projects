package src;

public class test {
    public static void main(String[] args) {
        B b = new B();
    }

    static class A{
        A(){
            System.out.println("A");
        }
    }

    static class B extends A{
        B(){
            System.out.println("B");
        }
    }
}