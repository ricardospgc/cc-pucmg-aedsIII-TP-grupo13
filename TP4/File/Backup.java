package File;

import java.io.*;
import java.util.ArrayList;

public class Backup {

    private static final String backupDir = ".\\Backups";
    private static final String dataDir = ".\\BaseDeDados";

    /*
    func: Backup
    Inicializa o objeto de backup, criando os diretórios de backup e dados se não existirem.
    Retornos: Nenhum
    */
    public Backup() {
        createDirectory(backupDir);
        createDirectory(dataDir);
    } 

    /*
    func: getBackupDir
    Retorna o caminho do diretório de backup.
    Retornos: String - caminho do diretório de backup
    */
    public String getBackupDir() {
        return backupDir;
    } 

    /*
    func: getDataDir
    Retorna o caminho do diretório de dados.
    Retornos: String - caminho do diretório de dados
    */
    public String getDataDir() {
        return dataDir;
    } 

    /*
    func: createBackup
    Cria um backup comprimido dos dados do diretório de dados.
    Retornos: Nenhum
    */
    public void createBackup(String backupFileName) {
        try {
            createDirectory(backupDir);
            File databaseDirectory = new File(dataDir);

            String subDirectoryPath = backupDir + "\\" + backupFileName.replace(".db", "");
            createDirectory(subDirectoryPath);

            if (!databaseDirectory.exists()) {
                System.err.println("Diretório de dados não encontrado.");
            } else {
                File[] dataFiles = databaseDirectory.listFiles();
                if (dataFiles != null) {
                    byte[] originalData = serializeFiles(dataFiles);
                    byte[] compressedData = LZW.codifica(originalData);

                    double compressionRate = calculateCompressionRate(originalData, compressedData);
                    System.out.printf("Taxa de compressão: %.2f%%\n", compressionRate);

                    writeFile(subDirectoryPath + "\\" + backupFileName, compressedData);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao realizar o backup: " + e.getMessage());
        }
    } 

    /*
    func: restoreBackup
    Restaura os dados de um arquivo de backup comprimido.
    Retornos: Nenhum
    */
    public void restoreBackup(String backupFileName) {
        File backupFile = new File(backupDir + "\\" + backupFileName);

        if (!backupFile.exists()) {
            File subDirectory = new File(backupDir + "\\" + backupFileName.replace(".db", ""));
            backupFile = new File(subDirectory, backupFileName);
        }

        if (!backupFile.exists()) {
            System.err.println("Arquivo de backup não encontrado.");
        } else {
            try {
                byte[] backupData = readFile(backupFile);
                backupData = LZW.decodifica(backupData);

                ByteArrayInputStream bais = new ByteArrayInputStream(backupData);
                DataInputStream dis = new DataInputStream(bais);
                clearDirectory(dataDir);

                while (dis.available() > 0) {
                    String fileName = dis.readUTF();
                    int fileSize = dis.readInt();
                    byte[] fileBytes = new byte[fileSize];
                    dis.readFully(fileBytes);

                    writeFile(dataDir + "\\" + fileName, fileBytes);
                }
            } catch (Exception e) {
                System.err.println("Erro ao recuperar o backup: " + e.getMessage());
            }
        }
    } 

    /*
    func: listBackups
    Lista todos os backups disponíveis no diretório de backup.
    Retornos: ArrayList<String> - lista de nomes dos backups
    */
    public ArrayList<String> listBackups() {
        ArrayList<String> backupList = new ArrayList<>();
        File backupDirectory = new File(backupDir);
        File[] subDirectories = backupDirectory.listFiles(File::isDirectory);

        if (subDirectories == null || subDirectories.length == 0) {
            System.out.println("Nenhum backup encontrado.");
        } else {
            System.out.println("\nBackups disponíveis:");
            for (int i = 0; i < subDirectories.length; i++) {
                System.out.println((i + 1) + ": " + subDirectories[i].getName());
                backupList.add(subDirectories[i].getName());
            }
        }
        return backupList;
    } 

    /*
    func: calculateCompressionRate
    Calcula a taxa de compressão entre os dados originais e comprimidos.
    Retornos: double - taxa de compressão em porcentagem
    */
    public double calculateCompressionRate(byte[] originalData, byte[] compressedData) {
        int originalSize = originalData.length;
        int compressedSize = compressedData.length;

        return (1 - ((double) compressedSize / originalSize)) * 100;
    } 

    /*
    func: serializeFiles
    Serializa os arquivos de um diretório em um array de bytes.
    Retornos: byte[] - os dados serializados
    */
    private byte[] serializeFiles(File[] files) {
        byte[] bytes = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            for (File file : files) {
                if (file.isFile()) {
                    dos.writeUTF(file.getName());
                    byte[] fileBytes = readFile(file);
                    dos.writeInt(fileBytes.length);
                    dos.write(fileBytes);
                }
            }
            bytes = baos.toByteArray();
        } catch (IOException e) {
            System.err.println("Erro ao serializar arquivos: " + e.getMessage());
        }
        return bytes;
    } 

    /*
    func: clearDirectory
    Remove todos os arquivos e subdiretórios de um diretório especificado.
    Retornos: Nenhum
    */
    private void clearDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        file.delete();
                    } else if (file.isDirectory()) {
                        clearDirectory(file.getPath());
                        file.delete();
                    }
                }
            }
        }
    } 

    /*
    func: createDirectory
    Cria um diretório no caminho especificado, se não existir.
    Retornos: Nenhum
    */
    private void createDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    } 

    /*
    func: readFile
    Lê os dados de um arquivo e os retorna como um array de bytes.
    Retornos: byte[] - os dados do arquivo
    */
    private byte[] readFile(File file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            return fileInputStream.readAllBytes();
        }
    } 

    /*
    func: writeFile
    Escreve um array de bytes em um arquivo especificado.
    Retornos: Nenhum
    */
    private void writeFile(String filePath, byte[] data) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            fileOutputStream.write(data);
        } catch (IOException e) {
            System.err.println("Erro ao escrever arquivo: " + e.getMessage());
        }
    } 

} 
