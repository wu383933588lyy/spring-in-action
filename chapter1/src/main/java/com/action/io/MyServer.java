package com.action.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @Author WuRui
 * @ClassName MyServer
 * @Date 2021/2/22 14:28
 * @Version 1.0
 * @Description //TODO
 **/
public class MyServer {

    private int size = 1024;
    private ServerSocketChannel serverSocketChannel;
    private ByteBuffer byteBuffer;
    private Selector selector;
    private int remoteClientNum = 0;

    public MyServer(int port) {
        try {
            initChannel(port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    // Channel 初始化
    public void initChannel(int port) throws IOException {
        // 打开 Channel
        serverSocketChannel = ServerSocketChannel.open();
        // 设置为非阻塞模式
        serverSocketChannel.configureBlocking(false);
        // 绑定端口
        serverSocketChannel.bind(new InetSocketAddress(port));
        System.out.println("listener on port: " + port);
        // 创建选择器
        selector = Selector.open();
        // 向选择器注册通道
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 分配缓冲区的大小
        byteBuffer = ByteBuffer.allocate(size);
    }

    // 监听器，用于监听 Channel 上的数据变化
    private void listener() throws Exception {
        while (true) {
            // 返回 int 值表示有多少个 Channel 处于就绪状态
            int n = selector.select();
            if (n == 0) {
                continue;
            }
            // 每个 selector 对应多个 SelectionKey，每个 SelectionKey 对应一个 Channel
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                // 如果 SelectionKey 处于连接就绪状态，则开始接收客户端的连接
                if (key.isAcceptable()) {
                    // 获取 Channel
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    // Channel 接受连接
                    SocketChannel channel = server.accept();
                    // Channel 注册
                    registerChannel(selector, channel, SelectionKey.OP_READ);
                    // 远程客户端的连接数
                    remoteClientNum++;
                    System.out.println("online client num = " + remoteClientNum);
                    write(channel, "hello client".getBytes());
                }
                // 如果通道已经处于读就绪状态
                if (key.isReadable()) {
                    read(key);
                }
                iterator.remove();
            }
        }
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        int count;
        byteBuffer.clear();
        // 从通道中读取数据到缓冲区
        while ((count = socketChannel.read(byteBuffer)) > 0) {
            // byteBuffer 写模式变为读模式
            byteBuffer.flip();
            while (byteBuffer.hasRemaining()) {
                System.out.println((char) byteBuffer.get());
            }
            byteBuffer.clear();
        }
        if (count < 0) {
            socketChannel.close();
        }
    }

    private void write(SocketChannel channel, byte[] writeData) throws IOException {
        byteBuffer.clear();
        byteBuffer.put(writeData);
        // byteBuffer 从写模式变成读模式
        byteBuffer.flip();
        // 将缓冲区的数据写入通道中
        channel.write(byteBuffer);
    }

    private void registerChannel(Selector selector, SocketChannel channel, int opRead) throws IOException {
        if (channel== null){
            return;
        }
        channel.configureBlocking(false);
        channel.register(selector,opRead);
    }

    public static void main(String[] args) {
        MyServer myServer = new MyServer(9999);
        try {
            myServer.listener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
