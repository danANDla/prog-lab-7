package udp;

import utils.IOutil;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;

public class UDPserver {
    private IOutil io;

    // Создайте новый экземпляр DatagramSocket, чтобы получать ответы от клиента
    private DatagramChannel datagramChannel;

    // Серверный UDP-сокет запущен на этом порту
    private final static int SERVICE_PORT=50001;
    private final static int BUFFER_SIZE=4096;

    private ByteBuffer buffer;

    public UDPserver(IOutil ioutil){
        io = ioutil;
        try{
            datagramChannel = DatagramChannel.open();
            datagramChannel.bind(new InetSocketAddress(SERVICE_PORT));
            buffer = ByteBuffer.allocate(BUFFER_SIZE);
            buffer.clear();
        } catch (SocketException e){
            io.printError("Unable to create server socket");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Request deserializeReq(ByteBuffer buffer){
        byte[] array = new byte[buffer.remaining()];
        buffer.get(array);
        ByteArrayInputStream bais = new ByteArrayInputStream(array);
        try (ObjectInputStream oip = new ObjectInputStream(bais)) {
            Request result = (Request) oip.readObject();
            return result;
        } catch (IOException | ClassNotFoundException e) {
            io.printError("Exception while deserializing request" + e);
        }
        return null;
    }

    public Request recieveRequest() throws IOException{
        /* Создайте экземпляр UDP-пакета для хранения клиентских данных с использованием буфера для полученных данных */
        System.out.println("Waiting for a client to connect...");

        datagramChannel.receive(buffer);

        System.out.println("Buffer state: " + buffer + ", content: " + Arrays.toString(buffer.array()));
        buffer.flip();
        Request received = deserializeReq(buffer);
        System.out.println(received);
        buffer.clear();

        return received;
    }


    private ByteArrayOutputStream serializeResp(Response newResp) throws IOException {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutput oo = new ObjectOutputStream(bStream);
        oo.writeObject(newResp);
        oo.close();
        return bStream;
    }

    public void sendReponse(Response resp, SocketAddress receiver){
        try{
            resp.setReceiver(receiver);
            System.out.println(resp);
            System.out.println("Sending response to client...");
            ByteBuffer sendingBuffer = ByteBuffer.wrap(serializeResp(resp).toByteArray());
            datagramChannel.send(sendingBuffer, resp.getReceiver());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
