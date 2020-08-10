package org.apache.poi.dev;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Element;

public class RecordGenerator {
    public static void main(String[] args) throws Exception {
        Class.forName("org.apache.poi.generator.FieldIterator");
        if (args.length != 4) {
            System.out.println("Usage:");
            System.out.println("  java org.apache.poi.hssf.util.RecordGenerator RECORD_DEFINTIONS RECORD_STYLES DEST_SRC_PATH TEST_SRC_PATH");
            return;
        }
        generateRecords(args[0], args[1], args[2], args[3]);
    }

    private static void generateRecords(String defintionsDir, String recordStyleDir, String destSrcPathDir, String testSrcPathDir) throws Exception {
        File definitionsFile;
        String str = recordStyleDir;
        File definitionsFile2 = new File(defintionsDir);
        int i = 0;
        int i2 = 0;
        while (i2 < definitionsFile2.listFiles().length) {
            File file = definitionsFile2.listFiles()[i2];
            if (!file.isFile()) {
                String str2 = destSrcPathDir;
                String str3 = testSrcPathDir;
                definitionsFile = definitionsFile2;
            } else if (file.getName().endsWith("_record.xml") || file.getName().endsWith("_type.xml")) {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Element record = builder.parse(file).getDocumentElement();
                String extendstg = record.getElementsByTagName("extends").item(i).getFirstChild().getNodeValue();
                String suffix = record.getElementsByTagName("suffix").item(i).getFirstChild().getNodeValue();
                String recordName = record.getAttributes().getNamedItem("name").getNodeValue();
                String packageName = record.getAttributes().getNamedItem("package").getNodeValue().replace('.', '/');
                StringBuilder sb = new StringBuilder();
                sb.append(destSrcPathDir);
                sb.append("/");
                sb.append(packageName);
                String destinationPath = sb.toString();
                new File(destinationPath).mkdirs();
                definitionsFile = definitionsFile2;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(destinationPath);
                sb2.append("/");
                sb2.append(recordName);
                sb2.append(suffix);
                sb2.append(".java");
                String destinationFilepath = sb2.toString();
                File file2 = new File(destinationFilepath);
                String str4 = destinationPath;
                DocumentBuilderFactory documentBuilderFactory = factory;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str);
                DocumentBuilder documentBuilder = builder;
                sb3.append("/");
                sb3.append(extendstg.toLowerCase());
                sb3.append(".xsl");
                transform(file, file2, new File(sb3.toString()));
                PrintStream printStream = System.out;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Generated ");
                sb4.append(suffix);
                sb4.append(": ");
                sb4.append(destinationFilepath);
                printStream.println(sb4.toString());
                StringBuilder sb5 = new StringBuilder();
                sb5.append(testSrcPathDir);
                sb5.append("/");
                sb5.append(packageName);
                String destinationPath2 = sb5.toString();
                new File(destinationPath2).mkdirs();
                StringBuilder sb6 = new StringBuilder();
                sb6.append(destinationPath2);
                sb6.append("/Test");
                sb6.append(recordName);
                sb6.append(suffix);
                sb6.append(".java");
                String destinationFilepath2 = sb6.toString();
                if (!new File(destinationFilepath2).exists()) {
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append(str);
                    sb7.append("/");
                    sb7.append(extendstg.toLowerCase());
                    sb7.append("_test.xsl");
                    transform(file, new File(destinationFilepath2), new File(sb7.toString()));
                    PrintStream printStream2 = System.out;
                    StringBuilder sb8 = new StringBuilder();
                    String str5 = destinationPath2;
                    sb8.append("Generated test: ");
                    sb8.append(destinationFilepath2);
                    printStream2.println(sb8.toString());
                } else {
                    PrintStream printStream3 = System.out;
                    StringBuilder sb9 = new StringBuilder();
                    sb9.append("Skipped test generation: ");
                    sb9.append(destinationFilepath2);
                    printStream3.println(sb9.toString());
                }
            } else {
                String str6 = destSrcPathDir;
                String str7 = testSrcPathDir;
                definitionsFile = definitionsFile2;
            }
            i2++;
            definitionsFile2 = definitionsFile;
            str = recordStyleDir;
            String str8 = defintionsDir;
            i = 0;
        }
        String str9 = destSrcPathDir;
        String str10 = testSrcPathDir;
        File file3 = definitionsFile2;
    }

    private static void transform(File in, File out, File xslt) throws FileNotFoundException, TransformerException {
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer(new StreamSource(new FileReader(xslt)));
            Properties p = new Properties();
            p.setProperty("method", "text");
            t.setOutputProperties(p);
            t.transform(new StreamSource(in), new StreamResult(out));
        } catch (TransformerException ex) {
            PrintStream printStream = System.err;
            StringBuilder sb = new StringBuilder();
            sb.append("Error compiling XSL style sheet ");
            sb.append(xslt);
            printStream.println(sb.toString());
            throw ex;
        }
    }
}
