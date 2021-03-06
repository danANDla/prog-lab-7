package udp;


import utils.IOutil;

import java.io.*;
import java.net.*;

public class UDPclient {
    /* Порт сервера, к которому собирается
    подключиться клиентский сокет */
    private final static int SERVICE_PORT = 50001;
    private DatagramSocket clientSocket;
    private InetAddress IPAddress;
    private IOutil io;

    // Создайте соответствующие буферы
    private byte[] sendingDataBuffer = new byte[1024];
    private byte[] receivingDataBuffer = new byte[1024];

    public UDPclient(IOutil ioutil) {
        /* Создайте экземпляр клиентского сокета.
        Нет необходимости в привязке к определенному порту */
        try {
            clientSocket = new DatagramSocket();
            // Получите IP-адрес сервера
            IPAddress = InetAddress.getByName("localhost");
        } catch (SocketException | UnknownHostException e) {
            io.printError("Error: unable create client socket");
        }
        io = ioutil;
    }

    public ByteArrayOutputStream serializeReq(Request newReq) throws IOException {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutput oo = new ObjectOutputStream(bStream);
        oo.writeObject(newReq);
        oo.close();
        return bStream;
    }

    public void sendCommand(Request newReq) {
        try {
            /* Преобразуйте данные в байты
            и разместите в буферах */
            sendingDataBuffer = serializeReq(newReq).toByteArray();
            receivingDataBuffer = new byte[1024];

            // Создайте UDP-пакет
            DatagramPacket sendingPacket = new DatagramPacket(sendingDataBuffer, sendingDataBuffer.length, IPAddress, SERVICE_PORT);
            System.out.println("Created DatagramPacket, " + "buffer length: " + sendingDataBuffer.length);

            // Отправьте UDP-пакет серверу
            clientSocket.send(sendingPacket);
            System.out.println("file was sent");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Response deserializeResp(byte[] buffer) {
        ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
        try (ObjectInputStream oip = new ObjectInputStream(bais)) {
            Response result = (Response) oip.readObject();
            return result;
        } catch (IOException | ClassNotFoundException e) {
            io.printError("deserializeResp: " + e);
        }
        return null;
    }

    public Response receiveResponse() {
        try {
            clientSocket.setSoTimeout(10000);
            // Получите ответ от сервера, т.е. предложение из заглавных букв
            DatagramPacket receivingPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);
            clientSocket.receive(receivingPacket);
            Response received = deserializeResp(receivingDataBuffer);

            return received;
        } catch (IOException e) {
            io.printError("can't receive response from server, check your connection and try again later");
        }
        return null;
    }

    public SocketAddress getAddress() {
        return clientSocket.getLocalSocketAddress();
    }
}
