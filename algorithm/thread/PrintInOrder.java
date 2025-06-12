import java.util.concurrent.CountDownLatch;
import java.util.Arrays;
import java.util.concurrent.Semaphore;
/* 通过改变线程顺序，改变数组中元素顺序 */
class Foo {
    private Semaphore semaphore1 = new Semaphore(1);
    private Semaphore semaphore2 = new Semaphore(0);
    private Semaphore semaphore3 = new Semaphore(0);
    private int[] originalArray = {1, 2, 3};
    private int[] newArray = new int[3];
    private int indexArray = 0;

    public Foo() {
    }

    public void first(Runnable printFirst) throws InterruptedException {
        synchronized (this) {
            // 打印第一个元素并放入新数组
            printFirst.run();
            newArray[indexArray] = originalArray[0];
            indexArray++;
        }
    }

    public void second(Runnable printSecond) throws InterruptedException {
        synchronized (this) {
            // 打印第二个元素并放入新数组
            printSecond.run();
            newArray[indexArray] = originalArray[1];
            indexArray++;
        }
    }

    public void third(Runnable printThird) throws InterruptedException {
        synchronized (this) {
            // 打印第三个元素并放入新数组
            printThird.run();
            newArray[indexArray] = originalArray[2];
            indexArray++;
        }
    }

    public static void main(String[] args) {
        Foo foo = new Foo();

        Thread t1 = new Thread(() -> {
            try {
                foo.first(() -> System.out.print(foo.originalArray[0]));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                foo.second(() -> System.out.print(foo.originalArray[1]));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t3 = new Thread(() -> {
            try {
                foo.third(() -> System.out.print(foo.originalArray[2]));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // 您可以通过调整下面的启动顺序来控制输出结果
        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t3.join();
            t2.join();
            System.out.println("新数组: " + Arrays.toString(foo.newArray));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}