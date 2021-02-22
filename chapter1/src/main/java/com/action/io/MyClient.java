package com.action.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Author WuRui
 * @ClassName MyClient
 * @Date 2021/2/22 15:09
 * @Version 1.0
 * @Description //TODO
 **/
public class MyClient {

    private int size = 1024;
    private ByteBuffer byteBuffer;
    private SocketChannel socketChannel;


    public void connectServer() throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1",9999));
        socketChannel.configureBlocking(false);
        byteBuffer = ByteBuffer.allocate(size);
        receive();
    }

    // 接收服务端发来的数据
    private void receive() throws IOException {
        while (true){
            byteBuffer.clear();
            int count;
            // 如果没有数据可读，则 read 方法一直阻塞，直到读取到新的数据
            while ((count = socketChannel.read(byteBuffer)) > 0){
                byteBuffer.flip();
                while (byteBuffer.hasRemaining()){
                    System.out.println((char)byteBuffer.get());
                }
                send2Server("say hi".getBytes());
                byteBuffer.clear();
            }
        }
    }

    // 向服务端发送数据
    private void send2Server(byte[] bytes) throws IOException {
        byteBuffer.clear();
        byteBuffer.put(bytes);
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
    }

    public static void main(String[] args) throws IOException {
        new MyClient().connectServer();

    }
}
