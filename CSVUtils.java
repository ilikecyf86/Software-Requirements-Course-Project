import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.http.HttpServletResponse;

/**
 * 文件操作
 */
public class CSVUtils {

    private static String DIRECTORY_PATH_OPEN = "C:\\Users\\yuego\\Desktop\\dataset\\DataOpen\\";
    private static int numberOfOpen = 566;
    private static String DIRECTORY_PATH_CLOSE = "C:\\Users\\yuego\\Desktop\\dataset\\DataClose\\";
    private static int numberOfClose = 2281;
    private static String DIRECTORY_PATH_TEST = "C:\\Users\\yuego\\Desktop\\dataset\\DataTest\\";
    private static int numberOfTest = 600;

    /**
     * 生成为CVS文件
     *
     * @param exportData 源数据List
     * @param map        csv文件的列表头map
     * @param outPutPath 文件路径
     * @param fileName   文件名称
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static File createCSVFile(List exportData, LinkedHashMap map,
                                     String outPutPath, String fileName) {
        File csvFile = null;
        BufferedWriter csvFileOutputStream = null;
        try {
            File file = new File(outPutPath);
            if (!file.exists()) {
                file.mkdir();
            }
            // 定义文件名格式并创建
            csvFile = File.createTempFile(fileName, ".csv", new File(outPutPath));
            System.out.println("csvFile：" + csvFile);
            // UTF-8使正确读取分隔符","
            csvFileOutputStream = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(csvFile), "UTF-8"), 1024);
            System.out.println("csvFileOutputStream：" + csvFileOutputStream);
            // 写入文件头部
            for (Iterator propertyIterator = map.entrySet().iterator();
                 propertyIterator.hasNext(); ) {
                java.util.Map.Entry propertyEntry =
                        (java.util.Map.Entry) propertyIterator.next();
                csvFileOutputStream.write(
                        "" + (String) propertyEntry.getValue() != null ?
                                (String) propertyEntry.getValue() : "" + "");
                if (propertyIterator.hasNext()) {
                    csvFileOutputStream.write(",");
                }
            }
            csvFileOutputStream.newLine();
            // 写入文件内容
            for (Iterator iterator = exportData.iterator(); iterator.hasNext(); ) {
                Object row = (Object) iterator.next();
                for (Iterator propertyIterator = map.entrySet().iterator();
                     propertyIterator.hasNext(); ) {
                    java.util.Map.Entry propertyEntry =
                            (java.util.Map.Entry) propertyIterator.next();
                    csvFileOutputStream.write((String) BeanUtils.getProperty(
                            row, (String) propertyEntry.getKey()));
                    if (propertyIterator.hasNext()) {
                        csvFileOutputStream.write(",");
                    }
                }
                if (iterator.hasNext()) {
                    csvFileOutputStream.newLine();
                }
            }
            csvFileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                csvFileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return csvFile;
    }

    /**
     * 下载文件
     *
     * @param response
     * @param csvFilePath 文件路径
     * @param fileName    文件名称
     * @throws IOException
     */
    public static void exportFile(HttpServletResponse response,
                                  String csvFilePath, String fileName)
            throws IOException {
        response.setContentType("application/csv;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" +
                URLEncoder.encode(fileName, "UTF-8"));

        InputStream in = null;
        try {
            in = new FileInputStream(csvFilePath);
            int len = 0;
            byte[] buffer = new byte[1024];
            response.setCharacterEncoding("UTF-8");
            OutputStream out = response.getOutputStream();
            while ((len = in.read(buffer)) > 0) {
                out.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
                out.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 删除该目录filePath下的所有文件
     *
     * @param filePath 文件目录路径
     */
    public static void deleteFiles(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    files[i].delete();
                }
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param filePath 文件目录路径
     * @param fileName 文件名称
     */
    public static void deleteFile(String filePath, String fileName) {
        File file = new File(filePath);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    if (files[i].getName().equals(fileName)) {
                        files[i].delete();
                        return;
                    }
                }
            }
        }
    }

    /**
     * 测试数据
     *
     * @param args
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void main(String[] args) throws Exception {
        ArrayList<Map> exportData = new ArrayList<Map>();

        for (int i = 0; i < numberOfOpen + numberOfClose; i++) {
            if (i < numberOfOpen) {
                readDataFromFile(exportData, DIRECTORY_PATH_OPEN + i + ".txt");
            } else {
                readDataFromFile(exportData, DIRECTORY_PATH_CLOSE + (i - numberOfOpen) + ".txt");
            }
        }
        for (int i = 0; i < numberOfTest; i++) {
            readDataFromFile(exportData, DIRECTORY_PATH_TEST + i + ".txt");
        }

        /*LinkedHashMap row = new LinkedHashMap<String, String>();
        row.put("1", "Authentication on workspace agent The main difference between wsmaster and wsagent authentification is that wsagent is a completely untrusted environment We can t store any system wide secrets here We have such options Reuse machine token authentication from Codenvy Dynamically register new OpenId clients for each workspace and grant access to only for a particular set of users who have permissions to use it ");
        row.put("2", "kind/planning");
        exportData.add(row);

        row = new LinkedHashMap<String, String>();
        row.put("1", "Add resend invitation option for team member invites From bmicklea on March 29 2017 13 16 When you invite a user to join your Codenvy team you should be able to choose to resend the same invitation in the event that they did not receive it Copied from original issue codenvy codenvy 2010 ");
        row.put("2", "kind/eclipse-che");
        exportData.add(row);*/

        LinkedHashMap map = new LinkedHashMap();
        map.put("1", "type");
        map.put("2", "content & title");

        String path = "C:\\Users\\yuego\\Desktop\\dataset\\";
        String fileName = "output";
        File file = CSVUtils.createCSVFile(exportData, map, path, fileName);
        String fileName2 = file.getName();
        System.out.println("文件名称：" + fileName2);
    }

    public static void readDataFromFile(ArrayList<Map> exportData, String filePath) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        String title = bufferedReader.readLine();
        String labels = bufferedReader.readLine();
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        while ((temp = bufferedReader.readLine()) != null) {
            stringBuffer.append(temp);
        }
        String content = stringBuffer.toString();
        String[] strings = content.split("\\W");
        String[] title_strings = title.split("\\W");
        StringBuffer stringBuffer1 = new StringBuffer();
        for (String str : title_strings) {
            if (!str.equals("")) {
                stringBuffer1.append(str + " ");
            }
        }
        for (String str : strings) {
            if (!str.equals("")) {
                if (str.length() > 3)
                    stringBuffer1.append(str + " ");
            }
        }
        if (!labels.equals("")) {
            if (labels.substring(0, 4).equals("kind")) {
                LinkedHashMap row = new LinkedHashMap<String, String>();
                row.put("1", labels.split(" ")[0]);
                row.put("2", stringBuffer1.toString());
                exportData.add(row);
            }
        }
    }
}